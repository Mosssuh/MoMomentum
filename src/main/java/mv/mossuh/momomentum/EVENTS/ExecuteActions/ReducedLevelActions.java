package mv.mossuh.momomentum.EVENTS.ExecuteActions;

import mv.mossuh.momomentum.API.Events.MomentumReducedLevelEvent;
import mv.mossuh.momomentum.ACTIONS.ActionResult;
import mv.mossuh.momomentum.ACTIONS.Requirements;
import mv.mossuh.momomentum.ACTIONS.Rewards;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.ENUMS.ActionType;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ReducedLevelActions implements Listener {

    @EventHandler
    public void reducedLevel(MomentumReducedLevelEvent event) {
        MomentumPlayer player = event.getPlayer();

        Level level = event.getOldLevel();

        Requirements requirements = new Requirements(player, level, ActionType.REDUCED).addAllDefaultVariables().check();
        ActionResult result = requirements.getActionResult();
        if (result.hasApprovedRewards()) {
            new Rewards(result).executeDefault().check();
        }
    }
}
