package dev.azn9.honoriabotplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LinkCommand implements CommandExecutor {

    private final HonoriabotApi honoriabotApi;

    public LinkCommand(HonoriabotApi honoriabotApi) {
        this.honoriabotApi = honoriabotApi;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String commandName, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }

        ResponseStatus responseStatus;
        if ("link".equalsIgnoreCase(commandName)) {
            if (args.length != 1 || !args[0].matches("[0-9]{6}")) {
                commandSender.sendMessage("§cUtilisation : /link <code>\nVous pouvez obtenir votre code de link en utilisant la commande /link sur le serveur Discord !");
                return false;
            }

            responseStatus = this.honoriabotApi.linkUser(args[0], ((Player) commandSender).getUniqueId().toString(), commandSender.getName());
        } else {
            responseStatus = this.honoriabotApi.unlinkUser(((Player) commandSender).getUniqueId().toString());
        }

        switch (responseStatus) {
            case UNKNOWN_CODE:
                commandSender.sendMessage("§cLe code spécifié est invalide ou expiré !");
                break;

            case INVALID_API_KEY:
            case INVALID_DATA:
            case DB_ERROR:
            case COMMUNICATION_FAILURE:
                commandSender.sendMessage("§cUne erreur est survenue, merci de signaler ce problème !");
                break;

            case SUCCESS:
                commandSender.sendMessage("§aVos comptes Discord et Minecraft sont maintenant liés !");
                break;

            case UNLINK_SUCCESS:
                commandSender.sendMessage("§aVos comptes Discord et Minecraft sont maintenant déliés !");
                break;

            default:
                break;
        }

        return true;
    }

}
