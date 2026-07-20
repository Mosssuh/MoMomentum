package mv.mossuh.momomentum.EVENTS.LevelUp;

import mv.mossuh.momomentum.API.Events.MomentumCompletedLevelEvent;
import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.ConfigLevels;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.LEVELS.MomentumState.TimerState;
import mv.mossuh.momomentum.MoMomentum;
import mv.mossuh.momomentum.UTILITIES.Momentum.BossBar.BossBar;
import mv.mossuh.momomentum.UTILITIES.Momentum.Cooldown;
import mv.mossuh.momomentum.EVENTS.LevelDown.TaskRestorer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class BlockBreak implements Listener {
    private MoMomentum instance = MoMomentum.getInstance();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        double blocks = 1;

        if (!BossBar.hasActiveBossBar(uuid)) {
            BossBar.createBossBar(player);
        }

        if (!TaskRestorer.hasActiveTaskRestorer(uuid)) {
            TaskRestorer.initializeTaskRestorer(uuid);
        }

        MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);
        int availableLevels = ConfigLevels.getAvailableLevels();

        Level nextLevelInfo = momentumPlayer.getNextLevel();
        int nextLevel = nextLevelInfo.getLevel();
        int actualLevel = momentumPlayer.getLevel();

        TimerState timer = momentumPlayer.getTimerState();

        if (!timer.isActive()) {
            momentumPlayer.getTimerState().start();
        }

        if (nextLevel < availableLevels) {
            Cooldown.setCooldown(uuid);
            momentumPlayer.addAmount(blocks);

            //Add amount for the next level
            double cost = nextLevelInfo.getCost();
            double amount = momentumPlayer.getAmount();

            if (amount >= cost) {
                // If amount is higher than cost, increase the level

                double excessAmount = amount - cost;

                momentumPlayer.updateNextLevel();
                momentumPlayer.setAmount(excessAmount);

                MomentumCompletedLevelEvent levelEvent = new MomentumCompletedLevelEvent(momentumPlayer, momentumPlayer.getActualLevel(), momentumPlayer.getOldLevel());
                instance.getServer().getPluginManager().callEvent(levelEvent);
            }
        } else {
            // If the level is the max level, only update the amount obtained
            Level actualLevelInfo = ConfigLevels.getLevel(actualLevel);
            double cost = actualLevelInfo.getCost();
            double amount = momentumPlayer.getAmount();

            if (amount >= cost) {
                if (amount != cost) {
                    momentumPlayer.setAmount(cost);
                }
            } else {
                momentumPlayer.addAmount(blocks);
            }
            Cooldown.setCooldown(uuid);
        }
    }
}
