package mv.mossuh.momomentum.EVENTS.LevelDown;

import mv.mossuh.momomentum.API.Events.MomentumReducedLevelEvent;
import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.COOLDOWN.LevelStateCooldown;
import mv.mossuh.momomentum.DATA.TimerStateData;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.MoMomentum;
import mv.mossuh.momomentum.UTILITIES.Momentum.BossBar.BossBar;
import mv.mossuh.momomentum.UTILITIES.Momentum.BossBar.BossBarTask;
import mv.mossuh.momomentum.UTILITIES.Momentum.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerLeave implements Listener {
    private MoMomentum instance = MoMomentum.getInstance();

    private static final boolean hasCooldown = Config.HAS_PLAYER_TIME_TO_AVOID_MOMENTUM_RESTART;

    @EventHandler
    public void playerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);

        if (hasCooldown) {
            LevelStateCooldown.startCooldown(momentumPlayer);

            Cooldown.cancelCooldown(uuid);
            TaskRestorer.cancelTaskRestorer(uuid);
        } else {
            List<Level> levels = new ArrayList<>(momentumPlayer.getLevels());
            for (Level level : levels) {
                if (level.isMaxLevel()) {
                    momentumPlayer.removeLevel();
                    continue;
                }

                momentumPlayer.removeLevel();

                MomentumReducedLevelEvent levelEvent = new MomentumReducedLevelEvent(momentumPlayer, momentumPlayer.getActualLevel(), momentumPlayer.getNextLevel());
                instance.getServer().getPluginManager().callEvent(levelEvent);
            }
        }

        TimerStateData.saveTimer(uuid);
        momentumPlayer.getTimerState().reset();

        BossBarTask.cancelTask(uuid);
        BossBar.cancelBossBar(uuid);

        if (!hasCooldown) {
            MomentumAPI.getManager().removePlayer(uuid);
        }
    }
}
