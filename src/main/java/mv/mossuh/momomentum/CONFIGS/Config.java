package mv.mossuh.momomentum.CONFIGS;

import mv.mossuh.mocore.UTILITIES.TimeFormat;
import mv.mossuh.momomentum.ENUMS.MomentumEventType;
import mv.mossuh.momomentum.MoMomentum;
import mv.mossuh.momomentum.UTILITIES.UtilMethods;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Config {
    private static final Map<String, String> intervalStringMap = new ConcurrentHashMap<>();
    private static final Map<String, Double> intervalDoubleMap = new ConcurrentHashMap<>();
    private static final Map<String, Long> intervalLongMap = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> intervalBooleanMap = new ConcurrentHashMap<>();
    private static final Map<String, List<String>> intervalListStringMap = new ConcurrentHashMap<>();
    private static final List<String> intervalsList = new ArrayList<>(Arrays.asList(
            "Config.save-in-database-interval",
            "Config.time-format.year", "Config.time-format.month",
            "Config.time-format.week", "Config.time-format.day",
            "Config.time-format.hour", "Config.time-format.minute",
            "Config.time-format.second",
            "Config.momentum-event",
            "Config.amount-to-subtract",
            "Config.subtract-every",
            "Config.starts-subtracting-at",
            "Config.update-boss-bar-every",
            "Config.boss-bar-default",
            "Config.player-leave-execute-reduced-actions",
            "Config.player-time-to-avoid-momentum-restart"
    ));

    public static long SAVE_IN_DATABASE_INTERVAL;
    public static MomentumEventType MOMENTUM_EVENT;
    public static double AMOUNT_TO_SUBTRACT;
    public static int SUBTRACT_EVERY;
    public static int STARTS_SUBTRACTING_AT;
    public static int UPDATE_BOSS_BAR_EVERY;
    public static String BOSS_BAR_DEFAULT;
    public static String TIME_FORMAT_YEAR;
    public static String TIME_FORMAT_MONTH;
    public static String TIME_FORMAT_WEEK;
    public static String TIME_FORMAT_DAY;
    public static String TIME_FORMAT_HOUR;
    public static String TIME_FORMAT_MINUTE;
    public static String TIME_FORMAT_SECOND;
    public static TimeFormat TIME_FORMAT;
    public static boolean PLAYER_LEAVE_EXECUTE_REDUCED_ACTIONS;
    public static int PLAYER_TIME_TO_AVOID_MOMENTUM_RESTART;
    public static boolean HAS_PLAYER_TIME_TO_AVOID_MOMENTUM_RESTART;
    public static String PREFIX = "&8[&bMoMomentum&8]";
    public static String PLUGIN_NAME = "MoMomentum";

    private static void loadConfig() {
        SAVE_IN_DATABASE_INTERVAL = new Config("Config.save-in-database-interval").getOrDefault(300L);
        MOMENTUM_EVENT = UtilMethods.getMomentumEventType(new Config("Config.momentum-event").getOrDefault("NONE"));
        AMOUNT_TO_SUBTRACT = new Config("Config.amount-to-subtract").getOrDefault(5.0);
        SUBTRACT_EVERY = new Config("Config.subtract-every").getOrDefault(2);
        STARTS_SUBTRACTING_AT = new Config("Config.starts-subtracting-at").getOrDefault(60);
        UPDATE_BOSS_BAR_EVERY = new Config("Config.update-boss-bar-every").getOrDefault(5);
        BOSS_BAR_DEFAULT = new Config("Config.boss-bar-default").getOrDefault("");
        TIME_FORMAT_YEAR = new Config("Config.time-format.year").getOrDefault("");
        TIME_FORMAT_MONTH = new Config("Config.time-format.month").getOrDefault("");
        TIME_FORMAT_WEEK = new Config("Config.time-format.week").getOrDefault("");
        TIME_FORMAT_DAY = new Config("Config.time-format.day").getOrDefault("");
        TIME_FORMAT_HOUR = new Config("Config.time-format.hour").getOrDefault("");
        TIME_FORMAT_MINUTE = new Config("Config.time-format.minute").getOrDefault("");
        TIME_FORMAT_SECOND = new Config("Config.time-format.second").getOrDefault("");
        TIME_FORMAT = new TimeFormat(TIME_FORMAT_YEAR, TIME_FORMAT_MONTH, TIME_FORMAT_WEEK, TIME_FORMAT_DAY, TIME_FORMAT_HOUR, TIME_FORMAT_MINUTE, TIME_FORMAT_SECOND);
        PLAYER_LEAVE_EXECUTE_REDUCED_ACTIONS = new Config("Config.player-leave-execute-reduced-actions").getOrDefault(true);
        PLAYER_TIME_TO_AVOID_MOMENTUM_RESTART = new Config("Config.player-time-to-avoid-momentum-restart").getOrDefault(0);
        HAS_PLAYER_TIME_TO_AVOID_MOMENTUM_RESTART = PLAYER_TIME_TO_AVOID_MOMENTUM_RESTART > 0;
    }

    private final String intervalPosition;

    public Config(String intervalPosition) {
        this.intervalPosition = intervalPosition;
    }


    public String getOrDefault(String defaultValue) {
        return intervalStringMap.getOrDefault(intervalPosition, defaultValue);
    }

    public int getOrDefault(int defaultValue) {
        if (intervalLongMap.containsKey(intervalPosition)) {
            return Math.toIntExact(intervalLongMap.get(intervalPosition));
        }
        return defaultValue;
    }

    public double getOrDefault(double defaultValue) {
        if (intervalDoubleMap.containsKey(intervalPosition)) {
            return intervalDoubleMap.get(intervalPosition);
        }
        return defaultValue;
    }

    public long getOrDefault(long defaultValue) {
        if (intervalLongMap.containsKey(intervalPosition)) {
            return intervalLongMap.get(intervalPosition);
        }
        return defaultValue;
    }

    public List<String> getOrDefault(List<String> defaultValue) {
        return intervalListStringMap.getOrDefault(intervalPosition, defaultValue);
    }

    public boolean getOrDefault(boolean defaultValue) {
        return intervalBooleanMap.getOrDefault(intervalPosition, defaultValue);
    }
    public static void load(MoMomentum instance) {
        FileConfiguration mainConfig = instance.getConfigs().getMainConfig().getConfig();
        intervalStringMap.clear();
        intervalListStringMap.clear();
        intervalBooleanMap.clear();
        intervalLongMap.clear();
        intervalDoubleMap.clear();

        for (String intervalConfig : intervalsList) {
            if (mainConfig.isString(intervalConfig)) {
                intervalStringMap.put(intervalConfig, mainConfig.getString(intervalConfig));
            } else if (mainConfig.isList(intervalConfig)) {
                intervalListStringMap.put(intervalConfig, mainConfig.getStringList(intervalConfig));
            } else if (mainConfig.isBoolean(intervalConfig)) {
                intervalBooleanMap.put(intervalConfig, mainConfig.getBoolean(intervalConfig));
            } else if (mainConfig.isLong(intervalConfig) || mainConfig.isInt(intervalConfig)) {
                intervalLongMap.put(intervalConfig, mainConfig.getLong(intervalConfig));
            } else if (mainConfig.isDouble(intervalConfig)) {
                intervalDoubleMap.put(intervalConfig, mainConfig.getDouble(intervalConfig));
            }
        }

        loadConfig();
    }
}
