package mv.mossuh.momomentum.EVENTS.LevelDown;

import mv.mossuh.momomentum.API.Events.MomentumReducedLevelEvent;
import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.DATA.TimerStateData;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.LEVELS.MomentumState.TimerState;
import mv.mossuh.momomentum.MoMomentum;
import mv.mossuh.momomentum.UTILITIES.Momentum.Cooldown;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TaskRestorer {
    private static MoMomentum instance = MoMomentum.getInstance();
    private static final Map<UUID, BukkitRunnable> tasks = new ConcurrentHashMap<>();

    public static void initializeTaskRestorer(UUID uuid) {
        double amountToSubtract = Config.AMOUNT_TO_SUBTRACT;
        long subtractInterval = Config.SUBTRACT_EVERY * 20L;
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!Cooldown.isActive(uuid)) {
                    MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);
                    // Execute if the level is higher than 0
                    if (momentumPlayer.getLevel() >= 0) {
                        double amount = momentumPlayer.getAmount();

                        if (amount > 0) {
                            // Remove the amount obtained in the level
                            momentumPlayer.removeAmount(amountToSubtract);
                        } else {
                            if (momentumPlayer.getLevel() != 0) {
                                // Remove level when the amount is 0, and if level is higher than 0
                                momentumPlayer.removeLevel();
                                double newAmount = momentumPlayer.getNextLevel().getCost();
                                momentumPlayer.setAmount(newAmount);

                                MomentumReducedLevelEvent levelEvent = new MomentumReducedLevelEvent(momentumPlayer, momentumPlayer.getActualLevel(), momentumPlayer.getNextLevel());
                                instance.getServer().getPluginManager().callEvent(levelEvent);
                            }
                        }
                    }

                    TimerState timer = momentumPlayer.getTimerState();
                    if (timer.isActive()) {
                        // Method to save time in database
                        // Data.saveTimer(uuid);
                        TimerStateData.saveTimer(uuid);
                        timer.reset();
                    }
                }


            }
        };
        task.runTaskTimer(instance, 0, subtractInterval);
        tasks.put(uuid, task);
    }

    public static boolean hasActiveTaskRestorer(UUID uuid) {
        BukkitRunnable task = tasks.get(uuid);
        return task != null;
    }

    public static void cancelTaskRestorer(UUID uuid) {
        BukkitRunnable task = tasks.get(uuid);
        if (task != null) {
            task.cancel();
            tasks.remove(uuid);
        }
    }
}
