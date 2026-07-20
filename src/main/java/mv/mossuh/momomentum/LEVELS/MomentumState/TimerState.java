package mv.mossuh.momomentum.LEVELS.MomentumState;

import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.ENUMS.TimeType;
import mv.mossuh.momomentum.UTILITIES.UtilMethods;
import mv.mossuh.momomentum.UTILITIES.UtilString;

public class TimerState {
    long startTime = 0;
    private TimeType timeType = TimeType.MILLISECONDS;

    public TimerState(Long startTime, TimeType timeType) {
        if (startTime != null) { this.startTime = startTime; }
        if (timeType != null) { this.timeType = timeType; }
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public boolean isActive() {
        return startTime > 0;
    }

    public void setTimeType(TimeType timeType) {
        if (timeType != null) { this.timeType = timeType; }
    }
    public TimeType getTimeType() { return timeType; }

    public long getElapsedTime() {
        if (timeType == TimeType.MILLISECONDS) {
            if (startTime > 0) {
                return (System.currentTimeMillis() - startTime) / 1000;
            }
        } else if (timeType == TimeType.SECONDS) {
            return startTime;
        }
        return 0;
    }

    public String getElapsedTime(boolean formatted) {
        if (formatted) {
            if (timeType == TimeType.MILLISECONDS) {
                if (startTime > 0) {
                    return UtilMethods.showTimerFormatted(getElapsedTime());
                }
                return "0" + UtilString.get(Config.TIME_FORMAT_SECOND).hex().apply();
            } else if (timeType == TimeType.SECONDS) {
                return UtilMethods.showTimerFormatted(startTime);
            }
            return "0" + UtilString.get(Config.TIME_FORMAT_SECOND).hex().apply();
        } else {
            return String.valueOf(getElapsedTime());
        }
    }

    public void reset() {
        startTime = 0;
    }
}
