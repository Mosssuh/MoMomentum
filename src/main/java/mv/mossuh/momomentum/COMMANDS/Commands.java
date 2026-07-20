package mv.mossuh.momomentum.COMMANDS;

import mv.mossuh.mocore.UTILITIES.UsefulMethods;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.MoMomentum;
import mv.mossuh.momomentum.UTILITIES.UtilString;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {
    /*

    /momomentum reload

    /momomentum boosters

     */

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] strings) {
        if (strings.length > 0) {
            switch (strings[0].toLowerCase()) {
                case "boosters":
                case "booster":
                case "boost":
                    BoosterCommands.onCommand(sender, strings);
                    break;
                case "reload":
                    MoMomentum.getInstance().getConfigs().reload();
                    UtilString.get(Config.PREFIX+" &aConfiguration reloaded!").hex().sendMessage(sender);
                    break;
            }
        } else {
            if (UsefulMethods.hasPermission(sender, "momomentum.admin")) {
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/momomentum boosters").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&b/momomentum reload").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
                UtilString.get("&8---------------------------------------------------").hex().sendMessage(sender);
                UtilString.get("&r").hex().sendMessage(sender);
            }
        }
        return false;
    }
}
