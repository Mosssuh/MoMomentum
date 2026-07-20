package mv.mossuh.momomentum.UTILITIES.Momentum;

import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.UTILITIES.UtilMethods;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public class Cooldown {

    private static final Map<UUID, Long> tasksCurrentTime = new ConcurrentHashMap<>();

    public static void setCooldown(UUID uuid) {
        tasksCurrentTime.put(uuid, System.currentTimeMillis());
    }

    public static void cancelCooldown(UUID uuid) {
        tasksCurrentTime.put(uuid, 0L);
    }

    public static String showCooldownFormatted(UUID uuid) {
        long currentTime = tasksCurrentTime.getOrDefault(uuid, 0L);
        int cooldown = Config.STARTS_SUBTRACTING_AT;
        return UtilMethods.showCooldownFormatted(cooldown, currentTime);
    }

    public static String showCooldown(UUID uuid) {
        long currentTime = tasksCurrentTime.getOrDefault(uuid, 0L);
        int cooldown = Config.STARTS_SUBTRACTING_AT;
        return UtilMethods.showCooldown(cooldown, currentTime)+"";
    }

    public static boolean isActive(UUID uuid) {
        long currentTime = tasksCurrentTime.getOrDefault(uuid, 0L);
        int cooldown = Config.STARTS_SUBTRACTING_AT;
        return UtilMethods.showCooldown(cooldown, currentTime) > 0;
    }
}
