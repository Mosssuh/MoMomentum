package mv.mossuh.momomentum.DATA;

import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.ENUMS.TimeType;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.LEVELS.MomentumState.TimerState;
import mv.mossuh.momomentum.MoMomentum;

import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TimerStateData {
    private static MoMomentum instance = MoMomentum.getInstance();

    //PlayerTimer in Seconds
    private static final Map<UUID, TimerState> saveTimer = new ConcurrentHashMap<>();

    private static void setTimer(UUID uuid, long timeInSeconds) {
        TimerState timerState = saveTimer.getOrDefault(uuid, new TimerState(0L, TimeType.SECONDS));
        long time = timerState.getElapsedTime();
        if (timeInSeconds > time) {
            saveTimer.put(uuid, timerState);
        }
    }
    private static long getSavedTimeForUUID(Connection conn, UUID uuid) throws SQLException {
        String query = "SELECT time FROM PlayerTimer WHERE uuid = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, uuid.toString());
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong("time");
                }
            }
        }
        return 0;
    }


    public static void registerDatabaseInMaps() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + instance.getConfigs().getPlayerTimerData().getFile().getAbsolutePath());
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS PlayerTimer (uuid TEXT PRIMARY KEY, time INTEGER)");

            ResultSet rsTimer = stmt.executeQuery("SELECT * FROM PlayerTimer");
            while (rsTimer.next()) {
                UUID uuid = UUID.fromString(rsTimer.getString("uuid"));
                long time = rsTimer.getLong("time");
                setTimer(uuid, time);
            }

            rsTimer.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void registerMapsInDatabase() {
        try {
            Map<UUID, TimerState> savedTimers = saveTimer;
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + instance.getConfigs().getPlayerTimerData().getFile().getAbsolutePath());
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS PlayerTimer (uuid TEXT PRIMARY KEY, time INTEGER)");
            for (UUID uuid : savedTimers.keySet()) {
                TimerState timerState = savedTimers.getOrDefault(uuid, new TimerState(0L, TimeType.SECONDS));
                long time = timerState.getElapsedTime();
                long savedTime = getSavedTimeForUUID(conn, uuid);

                if (time > savedTime) {
                    stmt.execute("INSERT OR REPLACE INTO PlayerTimer (uuid, time) VALUES('" + uuid + "', '" + time + "')");
                }
            }

            stmt.execute("VACUUM");

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void registerTimersInMaps() {
        Map<UUID, TimerState> savedTimers = MomentumAPI.getManager().getTimers();
        for (UUID uuid : savedTimers.keySet()) {
            TimerState timerState = savedTimers.getOrDefault(uuid, new TimerState(0L, TimeType.MILLISECONDS));
            long time = timerState.getElapsedTime();

            TimerState savedTimerState = saveTimer.getOrDefault(uuid, new TimerState(0L, TimeType.SECONDS));
            long savedTime = savedTimerState.getElapsedTime();

            if (time > savedTime) {
                setTimer(uuid, time);
            }
        }
    }

    public static void saveTimer(UUID uuid) {
        MomentumPlayer player = MomentumAPI.getManager().getPlayer(uuid);
        if (player.isPlayer()) {
            TimerState timerState = player.getTimerState();
            long timeInSeconds = timerState.getElapsedTime();

            TimerState savedTimerState = saveTimer.getOrDefault(uuid, new TimerState(0L, TimeType.SECONDS));
            long savedTime = savedTimerState.getElapsedTime();
            if (timeInSeconds > savedTime) {
                TimerState newTimerState = new TimerState(timerState.getElapsedTime(), TimeType.SECONDS);
                saveTimer.put(uuid, newTimerState);
            }
        }
    }

    public static Map<UUID, TimerState> getSavedTimers() {
        return saveTimer;
    }

    public static TimerState getTimer(UUID uuid) {
        return saveTimer.getOrDefault(uuid, new TimerState(0L, TimeType.MILLISECONDS));
    }
}
