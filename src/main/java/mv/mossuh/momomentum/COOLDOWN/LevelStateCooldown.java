package mv.mossuh.momomentum.COOLDOWN;

import mv.mossuh.momomentum.API.Events.MomentumReducedLevelEvent;
import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.MoMomentum;
import mv.mossuh.momomentum.UTILITIES.Momentum.BossBar.BossBar;
import mv.mossuh.momomentum.UTILITIES.Momentum.BossBar.BossBarTask;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LevelStateCooldown {
    private static MoMomentum instance = MoMomentum.getInstance();
    private static ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    private static final Map<UUID, ScheduledFuture<?>> tasks = new ConcurrentHashMap<>();
    private static final Map<UUID, AtomicBoolean> verification = new ConcurrentHashMap<>();

    private static final int cooldown =Config.PLAYER_TIME_TO_AVOID_MOMENTUM_RESTART;

    public static void startCooldown(MomentumPlayer player) {
        if (player.isActive()) {
            UUID uuid = player.getUUID();

            ScheduledFuture<?> task = tasks.get(uuid);
            AtomicBoolean isVerificationInProgress = verification.computeIfAbsent(uuid, k -> new AtomicBoolean(false));


            if (task != null) {
                task.cancel(true);
                tasks.remove(uuid);
                isVerificationInProgress.set(false);
            }

            if (isVerificationInProgress.compareAndSet(false, true)) {
                task = executor.schedule(() -> {
                    Bukkit.getScheduler().runTask(MoMomentum.getInstance(), () -> {
                        List<Level> levels = new ArrayList<>(player.getLevels());
                        for (Level level : levels) {
                            if (level.isMaxLevel()) {
                                player.removeLevel();
                                continue;
                            }

                            player.removeLevel();

                            MomentumReducedLevelEvent levelEvent = new MomentumReducedLevelEvent(player, player.getActualLevel(), player.getNextLevel());
                            instance.getServer().getPluginManager().callEvent(levelEvent);
                        }

                        player.setAmount(0.0);
                        BossBarTask.cancelTask(uuid);
                        BossBar.cancelBossBar(uuid);
                        MomentumAPI.getManager().removePlayer(uuid);
                    });
                    isVerificationInProgress.set(false);
                }, cooldown, TimeUnit.SECONDS);

                tasks.put(uuid, task);
            }
        }
    }

    public static void cancelCooldown(UUID uuid) {
        ScheduledFuture<?> task = tasks.get(uuid);
        AtomicBoolean isVerificationInProgress = verification.computeIfAbsent(uuid, k -> new AtomicBoolean(false));

        if (task != null) {
            task.cancel(true);
            tasks.remove(uuid);
            isVerificationInProgress.set(false);

        }
    }

    public static void initializeCooldowns() {
        List<MomentumPlayer> players = MomentumAPI.getManager().getPlayers();

        if (!players.isEmpty()) {
            for (MomentumPlayer player : players) {
                startCooldown(player);
            }
        }

    }
}
