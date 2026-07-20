package mv.mossuh.momomentum.DATA;

import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.MoMomentum;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class LevelStateData {
    private static MoMomentum instance = MoMomentum.getInstance();

    public static void registerDatabaseInPlayers() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + instance.getConfigs().getPlayerLevelData().getFile().getAbsolutePath());
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS LevelState (uuid TEXT PRIMARY KEY, level INTEGER, amount REAL)");

            ResultSet rs = stmt.executeQuery("SELECT * FROM LevelState");
            while (rs.next()) {
                UUID uuid = UUID.fromString(rs.getString("uuid"));
                int level = rs.getInt("level");
                double amount = rs.getDouble("amount");

                MomentumPlayer player = MomentumAPI.getManager().getPlayer(uuid);
                player.updateLevel(level);
                player.setAmount(amount);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void registerPlayersInDatabase() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + instance.getConfigs().getPlayerLevelData().getFile().getAbsolutePath());
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS LevelState (uuid TEXT PRIMARY KEY, level INTEGER, amount REAL)");

            List<MomentumPlayer> players = MomentumAPI.getManager().getPlayers();
            for (MomentumPlayer player : players) {
                if (!player.isActive()) { continue; }
                UUID uuid = player.getUUID();
                int level = player.getLevel();
                double amount = player.getAmount();
                stmt.execute("INSERT OR REPLACE INTO LevelState (uuid, level, amount) VALUES('" + uuid + "', '" + level + "', '" + amount + "')");
            }

            stmt.execute("VACUUM");

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
