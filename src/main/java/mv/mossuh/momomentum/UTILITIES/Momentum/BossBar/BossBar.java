package mv.mossuh.momomentum.UTILITIES.Momentum.BossBar;

import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.ConfigLevels;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.MoMomentum;
import mv.mossuh.momomentum.UTILITIES.Momentum.BossBar.reflection.MBossBar;
import mv.mossuh.momomentum.UTILITIES.UtilString;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BossBar {
    private static MoMomentum instance = MoMomentum.getInstance();
    private static final Map<UUID, BukkitRunnable> tasks = new ConcurrentHashMap<>();
    private static final Map<UUID, MBossBar<?, ?>> bossBarMap = new ConcurrentHashMap<>();

    public static void createBossBar(Player player) {
        UUID uuid = player.getUniqueId();
        long updateInterval = Config.UPDATE_BOSS_BAR_EVERY * 20L;

        MBossBar<?, ?> boss = MBossBar.build("", MBossBar.Color.BLUE, MBossBar.Overlay.PROGRESS, 1.0f);
        boss.show(player);
        bossBarMap.put(uuid, boss);


        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);

                Level nextLevelInformation = momentumPlayer.getNextLevel();
                Level actualLevelInformation = momentumPlayer.getActualLevel();
                int nextLevel = nextLevelInformation.getLevel();
                int actualLevel = momentumPlayer.getLevel();
                double amountObtained = momentumPlayer.getAmount();

                if (nextLevel < ConfigLevels.getAvailableLevels()) {
                    double cost = nextLevelInformation.getCost();
                    float progress = (float) (amountObtained / cost);

                    String message;
                    if (actualLevel == 0) {
                        message = UtilString.get(Config.BOSS_BAR_DEFAULT).hex().setDefaultPlayerVariables(player).setPlaceholders(player).apply();
                    } else {
                        message = UtilString.get(actualLevelInformation.getBossBarText()).hex().setDefaultPlayerVariables(player).setPlaceholders(player).apply();
                    }
                    message = UtilString.get(message).setDefaultMomentumPlayerVariables(momentumPlayer).apply();

                    boss.setTitle(message);
                    boss.setPercentage(progress);
                    bossBarMap.put(uuid, boss);
                } else {
                    double cost = actualLevelInformation.getCost();
                    float progress = (float) (amountObtained / cost);

                    String message = UtilString.get(actualLevelInformation.getBossBarText()).hex().setPlaceholders(player).setDefaultPlayerVariables(player)
                            .setDefaultMomentumPlayerVariables(momentumPlayer).apply();

                    boss.setTitle(message);
                    boss.setPercentage(progress);
                    bossBarMap.put(uuid, boss);
                }
            }
        };
        task.runTaskTimer(instance, 0, updateInterval);
        tasks.put(uuid, task);
    }

    public static MBossBar<?, ?> getBossBar(UUID uuid) {
        if (bossBarMap.get(uuid) != null) {
            return bossBarMap.get(uuid);
        }
        return null;
    }

    public static boolean hasActiveBossBar(UUID uuid) {
        MBossBar<?, ?> bossBar = bossBarMap.get(uuid);
        return bossBar != null;
    }

    public static void cancelBossBar(UUID uuid) {

        BukkitRunnable task = tasks.get(uuid);

        MBossBar<?, ?> bossBar = bossBarMap.get(uuid);
        if (bossBar != null) {
            bossBarMap.remove(uuid);
            bossBar.destroy();
        }
        if (task != null) {
            task.cancel();
            tasks.remove(uuid);
        }
    }
}
