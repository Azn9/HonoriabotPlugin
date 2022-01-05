package dev.azn9.honoriabotplugin;

import com.google.gson.Gson;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public final class HonoriabotApi extends JavaPlugin {

    private static final Gson GSON = new Gson();
    private String apiUrl;
    private String apiKey;

    @Override
    public void onEnable() {
        Sentry.init("");

        super.saveDefaultConfig();

        FileConfiguration configuration = super.getConfig();
        String apiKey = configuration.getString("api_key", "");
        String apiUrl = configuration.getString("api_url", "");

        if (apiKey.isEmpty() || !apiKey.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")) {
            super.getLogger().severe("Veuillez spécifier la clé \"api_key\" de la configuration !");
            super.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (apiUrl.isEmpty()) {
            super.getLogger().severe("Veuillez spécifier la clé \"api_url\" de la configuration !");
            super.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.apiUrl = apiUrl;
        this.apiKey = apiKey;

        LinkCommand linkCommand = new LinkCommand(this);
        super.getCommand("link").setExecutor(linkCommand);
        super.getCommand("unlink").setExecutor(linkCommand);
    }

    public ResponseStatus linkUser(String code, String uuid, String username) {
        LinkData linkData = new LinkData(this.apiKey, code, uuid, username);

        try {
            return this.sendRequest(this.apiUrl + "/link", HonoriabotApi.GSON.toJson(linkData)).getStatus();
        } catch (IOException exception) {
            Sentry.captureException(exception);
            return ResponseStatus.COMMUNICATION_FAILURE;
        }
    }

    public ResponseStatus unlinkUser(String uuid) {
        LinkData linkData = new LinkData("", "", uuid, "");

        try {
            return this.sendRequest(this.apiUrl + "/link", HonoriabotApi.GSON.toJson(linkData)).getStatus();
        } catch (IOException exception) {
            Sentry.captureException(exception);
            return ResponseStatus.COMMUNICATION_FAILURE;
        }
    }

    public ResponseStatus addCoins(String uuid, int coins) {
        CoinsData coinsData = new CoinsData(this.apiKey, uuid, coins);

        try {
            return this.sendRequest(this.apiUrl + "/coins", HonoriabotApi.GSON.toJson(coinsData)).getStatus();
        } catch (IOException exception) {
            Sentry.captureException(exception);
            return ResponseStatus.COMMUNICATION_FAILURE;
        }
    }

    private Response sendRequest(String apiUrl, String data) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setDoOutput(true);
        DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
        outputStream.write(data.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();

        int status = connection.getResponseCode();
        boolean error = status > 299;
        Reader streamReader;

        if (error) {
            streamReader = new InputStreamReader(connection.getErrorStream());
        } else {
            streamReader = new InputStreamReader(connection.getInputStream());
        }

        BufferedReader bufferedReader = new BufferedReader(streamReader);
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = bufferedReader.readLine()) != null) {
            content.append(inputLine);
        }
        bufferedReader.close();

        if (error) {
            Sentry.captureMessage(content.toString(), SentryLevel.FATAL);
            return new Response(ResponseStatus.COMMUNICATION_FAILURE);
        }

        return HonoriabotApi.GSON.fromJson(content.toString(), Response.class);
    }
}
