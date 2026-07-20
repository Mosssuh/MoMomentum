package mv.mossuh.momomentum.MANAGERS;

import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.LEVELS.MomentumState.TimerState;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MomentumManager {
    private static final List<MomentumPlayer> players = new ArrayList<>();

    public void addPlayer(MomentumPlayer player) {
        players.removeIf(p -> p.getUUID().equals(player.getUUID()));
        players.add(player);
    }

    public void removePlayer(MomentumPlayer player) {
        players.removeIf(p -> p.getUUID().equals(player.getUUID()));
    }

    public void removePlayer(UUID uuid) {
        players.removeIf(p -> p.getUUID().equals(uuid));
    }

    public MomentumPlayer getPlayer(UUID uuid) {
        if (uuid != null) {
            for (MomentumPlayer player : players) {
                if (player.getUUID().equals(uuid)) {
                    return player;
                }
            }

            players.add(new MomentumPlayer(uuid, null, null, null, null));
        }
        return new MomentumPlayer(null, null, null, null, null);
    }

    public List<MomentumPlayer> getPlayers() {
        return players;
    }

    public Map<UUID, TimerState> getTimers() {
        Map<UUID, TimerState> timers = new ConcurrentHashMap<>();
        for (MomentumPlayer player : players) {
            UUID uuid = player.getUUID();
            TimerState timer = player.getTimerState();
            timers.put(uuid, timer);
        }
        return timers;
    }
}
