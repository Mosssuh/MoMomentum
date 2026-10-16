package mv.mossuh.momomentum.UTILITIES;


import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
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
    
    public static List<VariableArg> momentumPlayer(MomentumPlayer player) {
        List<VariableArg> variables = new ArrayList<>();
        if (!player.isPlayer()) return variables;
        
        Level nextLevel = player.getNextLevel();
        Level actualLevel = player.getActualLevel();

        double amount = player.getAmount();

        variables.add(new VariableArg("%amount%", amount + ""));
        variables.add(new VariableArg("%actual_level%", actualLevel.getLevel() + ""));
        variables.add(new VariableArg("%actual_cost%", actualLevel.getCost() + ""));
        variables.add(new VariableArg("%actual_message%", actualLevel.getBossBarText()));
        variables.add(new VariableArg("%next_level%", nextLevel.getLevel() + ""));
        variables.add(new VariableArg("%next_cost%", nextLevel.getCost() + ""));
        variables.add(new VariableArg("%next_message%", nextLevel.getBossBarText()));
        return variables;
    }

    public static List<VariableArg> level(Level level) {
        List<VariableArg> variables = new ArrayList<>();
        if (level == null) return variables;

        variables.add(new VariableArg("%actual_level%", level.getLevel() + ""));
        variables.add(new VariableArg("%actual_cost%", level.getCost() + ""));
        variables.add(new VariableArg("%actual_message%", level.getBossBarText()));
        return variables;
    }
}
