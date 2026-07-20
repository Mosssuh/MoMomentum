package mv.mossuh.momomentum.COMMANDS;

import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.CONFIGS.Messages;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.UTILITIES.UtilString;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BoosterCommands {
    public static void onCommand(CommandSender sender, String[] strings) {
        if (strings.length > 0) {
            if (strings[0].equalsIgnoreCase("boosters") || strings[0].equalsIgnoreCase("booster") || strings[0].equalsIgnoreCase("boost")) {
                // /momomentum boosters
                List<String> boostersMessage = Messages.BOOSTERS;

                Player player = null;
                if (sender instanceof Player) {
                    player = (Player) sender;
                }
                MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(player.getUniqueId());

                for (String line : boostersMessage) {
                    UtilString.get(line).hex().setPlaceholders(player).setPlaceholders(player).setDefaultPlayerVariables(player).setDefaultMomentumPlayerVariables(momentumPlayer)
                            .setDefaultNumberRandomVariable().setTimeFormatter().setMathPlaceholder().setChangeOutputPlaceholder().sendMessage(player);
                }
            }
        }
    }
}
