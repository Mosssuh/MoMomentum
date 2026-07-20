package mv.mossuh.momomentum.UTILITIES.Momentum.BossBar;

import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.MoMomentum;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossBarTask {
    private static MoMomentum instance = MoMomentum.getInstance();
    private static final Map<UUID, BukkitRunnable> tasks = new ConcurrentHashMap<>();

    public static void initializeBossBarCheck(UUID uuid) {

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);
                int level = momentumPlayer.getLevel();
                double amount = momentumPlayer.getAmount();
                if (level == 0 && amount == 0) {
                    BossBar.cancelBossBar(uuid);
                }
            }
        };
        task.runTaskTimer(instance, 0, 100);

        tasks.put(uuid, task);
    }

    public static void cancelTask(UUID uuid) {
        BukkitRunnable task = tasks.get(uuid);

        if (task != null) {
            task.cancel();
            tasks.remove(uuid);
        }
    }
}
