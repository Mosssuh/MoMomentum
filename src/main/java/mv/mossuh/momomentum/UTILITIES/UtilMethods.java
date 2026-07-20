package mv.mossuh.momomentum.UTILITIES;

import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.ENUMS.MomentumEventType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UtilMethods {

    public static double transformChance(UUID uuid, String chance) {
        chance = UtilString.get(chance).setDefaultNumberRandomVariable().setChangeOutputPlaceholder().setMathPlaceholder()
                .setTimeFormatter().setPlaceholders(uuid).apply();
        double chanceFormat = 100 * 100;
        if (UtilString.get(chance).isNumeric()) {
            chanceFormat = Double.parseDouble(chance) * 100;
        }
        return 10000 - chanceFormat;
    }

    public static double randomNumber() {
        double lowest = 1.000000000000000F;
        double highest = 10000.000000000000000F;
        return Math.floor(Math.random() * (highest - lowest + 1) + lowest);
    }

    public static boolean isOnline(Player player) {
        if (player != null) {
            return player.isOnline();
        }
        return false;
    }

    public static MomentumEventType getMomentumEventType(String momentumEventTypeString) {
        if (momentumEventTypeString != null) {
            for (MomentumEventType e : MomentumEventType.values()) {
                if (e.name().equalsIgnoreCase(momentumEventTypeString)) {
                    return e;
                }
            }
        }
        return MomentumEventType.NONE;
    }

    public static long showCooldown(long cooldownInSeconds, long startedTimeInMilliSeconds) {
        long currentTimeInMilliSeconds = System.currentTimeMillis();
        long wait = currentTimeInMilliSeconds - startedTimeInMilliSeconds;
        long waitDiv = wait / 1000;
        long seconds = cooldownInSeconds - waitDiv;

        long durationInMilliSeconds = cooldownInSeconds * 1000;

        if (((startedTimeInMilliSeconds + durationInMilliSeconds) > currentTimeInMilliSeconds) && (startedTimeInMilliSeconds != 0)) {
            return seconds;
        } else {
            return 0;
        }
    }

    public static String showTimerFormatted(long timeInSeconds) {
        String time = "";

        String year = UtilString.get(Config.TIME_FORMAT_YEAR).hex().apply();
        String month = UtilString.get(Config.TIME_FORMAT_MONTH).hex().apply();
        String week = UtilString.get(Config.TIME_FORMAT_WEEK).hex().apply();
        String day = UtilString.get(Config.TIME_FORMAT_DAY).hex().apply();
        String hour = UtilString.get(Config.TIME_FORMAT_HOUR).hex().apply();
        String minute = UtilString.get(Config.TIME_FORMAT_MINUTE).hex().apply();
        String second = UtilString.get(Config.TIME_FORMAT_SECOND).hex().apply();

        long elapsedTotalMin = timeInSeconds / 60;
        long elapsedTotalHour = elapsedTotalMin / 60;
        long elapsedTotalDays = elapsedTotalHour / 24;
        long elapsedTotalWeeks = elapsedTotalDays / 7;
        long elapsedTotalMonths = elapsedTotalWeeks / 4;
        long elapsedTotalYears = elapsedTotalMonths / 12;

        long secondsPart = timeInSeconds % 60;
        long minutesPart = elapsedTotalMin % 60;
        long hoursPart = elapsedTotalHour % 24;
        long daysPart = elapsedTotalDays % 7;
        long weeksPart = elapsedTotalWeeks % 4;
        long monthsPart = elapsedTotalMonths % 12;
        long yearsPart = elapsedTotalYears;

        if (timeInSeconds > 0) {
            if (secondsPart != 0) {
                time = secondsPart + second;
            }

            if (minutesPart > 0) {
                time = minutesPart + minute + " " + time;
            }

            if (hoursPart > 0) {
                time = hoursPart + hour + " " + time;
            }

            if (daysPart > 0) {
                time = daysPart + day + " " + time;
            }

            if (weeksPart > 0) {
                time = weeksPart + week + " " + time;
            }

            if (monthsPart > 0) {
                time = monthsPart + month + " " + time;
            }

            if (yearsPart > 0) {
                time = yearsPart + year + " " + time;
            }

            return time;
        } else {
            // No time has elapsed
            return "0";
        }
    }


    public static String showCooldownFormatted(long cooldownInSeconds, long startedTimeInMilliSeconds) {
        String time = "";

        String year = UtilString.get(Config.TIME_FORMAT_YEAR).hex().apply();
        String month = UtilString.get(Config.TIME_FORMAT_MONTH).hex().apply();
        String week = UtilString.get(Config.TIME_FORMAT_WEEK).hex().apply();
        String day = UtilString.get(Config.TIME_FORMAT_DAY).hex().apply();
        String hour = UtilString.get(Config.TIME_FORMAT_HOUR).hex().apply();
        String minute = UtilString.get(Config.TIME_FORMAT_MINUTE).hex().apply();
        String second = UtilString.get(Config.TIME_FORMAT_SECOND).hex().apply();

        long millis = System.currentTimeMillis();
        long cooldownmil = Math.round(cooldownInSeconds * 1000);

        long wait = millis - startedTimeInMilliSeconds;
        long waitDiv = wait / 1000;
        long waittotalsec = cooldownInSeconds - waitDiv;

        long waittotalmin = waittotalsec / 60;
        long waittotalhour = waittotalmin / 60;
        long waittotaldays = waittotalhour / 24;
        long waittotalweeks = waittotaldays / 7;
        long waittotalmonths = waittotalweeks / 4;
        long waittotalyears = waittotalmonths / 12;

        if (((startedTimeInMilliSeconds + cooldownmil) > millis) && (startedTimeInMilliSeconds != 0)) {
            if (waittotalsec > 59) {
                waittotalsec = waittotalsec - 60 * waittotalmin;
            }

            if (waittotalsec != 0) {
                time = waittotalsec + second;
            }

            if (waittotalmin > 59) {
                waittotalmin = waittotalmin - 60 * waittotalhour;
            }
            if (waittotalmin > 0) {
                time = waittotalmin + minute + " " + time;
            }

            if (waittotalhour > 23) {
                waittotalhour = waittotalhour - 24 * waittotaldays;
            }

            if (waittotalhour > 0) {
                time = waittotalhour + hour + " " + time;
            }

            if (waittotaldays > 6) {
                waittotaldays = waittotaldays - 7 * waittotalweeks;
            }

            if (waittotaldays > 0) {
                time = waittotaldays + day + " " + time;
            }

            if (waittotalweeks > 3) {
                waittotalweeks = waittotalweeks - 4 * waittotalmonths;
            }

            if (waittotalweeks > 0) {
                time = waittotalweeks + week + " " + time;
            }

            if (waittotalmonths > 11) {
                waittotalmonths = waittotalmonths - 12 * waittotalyears;
            }

            if (waittotalmonths > 0) {
                time = waittotalmonths + month + " " + time;
            }

            if (waittotalyears > 0) {
                time = waittotalyears + year + " " + time;
            }

            return time;
        } else {
            return "0" + second;
        }
    }
}
