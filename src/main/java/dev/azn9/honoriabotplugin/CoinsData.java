package dev.azn9.honoriabotplugin;

public class CoinsData {

    private final String apiKey;
    private final String uuid;
    private final String coins;

    public CoinsData(String apiKey, String uuid, Integer coins) {
        this.apiKey = apiKey;
        this.uuid = uuid;
        this.coins = "" + coins;
    }

}

