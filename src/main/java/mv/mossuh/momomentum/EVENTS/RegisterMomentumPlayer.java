package mv.mossuh.momomentum.EVENTS;

import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.COOLDOWN.LevelStateCooldown;
import mv.mossuh.momomentum.ENUMS.TimeType;
import mv.mossuh.momomentum.EVENTS.LevelDown.TaskRestorer;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.LEVELS.MomentumState.TimerState;
import mv.mossuh.momomentum.UTILITIES.Momentum.BossBar.BossBar;
import mv.mossuh.momomentum.UTILITIES.Momentum.BossBar.BossBarTask;
import mv.mossuh.momomentum.UTILITIES.Momentum.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.UUID;

public class RegisterMomentumPlayer implements Listener {
    private static final boolean hasCooldown = Config.HAS_PLAYER_TIME_TO_AVOID_MOMENTUM_RESTART;

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        if (hasCooldown) {
            LevelStateCooldown.cancelCooldown(uuid);

            MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);
            if (momentumPlayer.isActive()) {
                if (!BossBar.hasActiveBossBar(uuid)) {
                    BossBar.createBossBar(player);
                }

                if (!TaskRestorer.hasActiveTaskRestorer(uuid)) {
                    TaskRestorer.initializeTaskRestorer(uuid);
                    Cooldown.setCooldown(uuid);
                }
            }

            TimerState timer = momentumPlayer.getTimerState();

            if (!timer.isActive()) {
                momentumPlayer.getTimerState().start();
            }
        }

        BossBarTask.initializeBossBarCheck(uuid);

        if (!hasCooldown) {
            MomentumPlayer momentumPlayer = new MomentumPlayer(uuid, 0, 0.0, new ArrayList<>(), new TimerState(0L, TimeType.MILLISECONDS));
            MomentumAPI.getManager().addPlayer(momentumPlayer);
        }
    }
}
