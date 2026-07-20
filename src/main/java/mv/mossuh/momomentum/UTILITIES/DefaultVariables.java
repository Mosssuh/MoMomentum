package mv.mossuh.momomentum.UTILITIES;


import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DefaultVariables {
    
    public static List<VariableArg> player(Player player) {
        return playerVariables(player.getUniqueId());
    }

    public static List<VariableArg> player(UUID uuid) {
        return playerVariables(uuid);
    }

    private static List<VariableArg> playerVariables(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        List<VariableArg> variables = new ArrayList<>();
        if (player != null && player.isOnline()) {
        variables.add(new VariableArg("%player%", player.getName()));
        }
        return variables;
    }
}
