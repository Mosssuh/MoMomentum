package mv.mossuh.momomentum.API.Events;

import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MomentumCompletedLevelEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private MomentumPlayer player = new MomentumPlayer(null, null, null, null, null);
    private Level newLevel = new Level(null, null, null, null, null, null, null);
    private Level oldLevel = new Level(null, null, null, null, null, null, null);
    public MomentumCompletedLevelEvent(MomentumPlayer player, Level newLevel, Level oldLevel) {
        if (player != null) { this.player = player; }
        if (newLevel != null) { this.newLevel = newLevel; }
        if (oldLevel != null) { this.oldLevel = oldLevel; }
    }

    public MomentumPlayer getPlayer() { return player; }
    public Level getNewLevel() { return newLevel; }
    public Level getOldLevel() { return oldLevel; }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
