package mv.mossuh.momomentum.ACTIONS;

import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.ENUMS.ActionType;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;

import java.util.ArrayList;
import java.util.List;

public class ActionResult {
    private MomentumPlayer player = new MomentumPlayer(null, null, null, null, null);
    private Level level = new Level(null, null, null, null, null, null, null);
    private ActionType actionType = ActionType.NONE;
    private List<VariableArg> variables = new ArrayList<>();
    private List<MoRewards> approvedRewards = new ArrayList<>();
    private boolean hasRewards = false;
    public ActionResult(MomentumPlayer player, Level level, ActionType actionType, List<VariableArg> variables, List<MoRewards> approvedRewards) {
        if (player != null) { this.player = player; }
        if (level != null) { this.level = level; }
        if (actionType != null) { this.actionType = actionType; }
        if (variables != null) { this.variables = variables; }
        if (approvedRewards != null) { this.approvedRewards = approvedRewards; }
        if (approvedRewards != null && !approvedRewards.isEmpty()) { this.hasRewards = true; }
    }

    public MomentumPlayer getPlayer() { return player; }
    public Level getLevel() { return level; }
    public ActionType getActionType() { return actionType; }
    public List<VariableArg> getVariables() { return variables; }
    public List<MoRewards> getApprovedRewards() { return approvedRewards; }
    public boolean hasApprovedRewards() { return hasRewards; }
}
