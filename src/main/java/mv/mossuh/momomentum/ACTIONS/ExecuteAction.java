package mv.mossuh.momomentum.ACTIONS;

import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ENUMS.RequirementType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.ENUMS.ActionType;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.UTILITIES.DefaultVariables;
import mv.mossuh.momomentum.UTILITIES.UtilString;

import java.util.*;

public class ExecuteAction {
    private MoAction moAction = new MoAction(null, null, null, null, null, null, null);
    private MomentumPlayer player = new MomentumPlayer(null, null, null, null, null);
    private Level level = new Level(null, null, null, null, null, null, null);
    private List<VariableArg> variables = new ArrayList<>();
    private ActionType actionType = ActionType.NONE;
    private ActionResult actionResult = new ActionResult(null, null, null, null, null);
    public ExecuteAction(MoAction moAction, MomentumPlayer player, Level level, ActionType actionType, List<VariableArg> variables) {
        if (moAction != null) { this.moAction = moAction; }
        if (player != null) { this.player = player; }
        if (level != null) { this.level = level; }
        if (actionType != null) { this.actionType = actionType; }
        if (variables != null) { this.variables = new ArrayList<>(variables); }
    }

    public ExecuteAction addVariables(VariableArg... variables) {
        this.variables.addAll(Arrays.asList(variables));
        return this;
    }

    public ExecuteAction addVariables(List<VariableArg> variables) {
        this.variables.addAll(variables);
        return this;
    }

    public ExecuteAction addAllDefaultVariables() {
        addDefaultPlayerVariables();
        return this;
    }

    public ExecuteAction addDefaultPlayerVariables() {
        this.variables.addAll(DefaultVariables.player(player.getUUID()));
        return this;
    }
    public ExecuteAction check() {

        UUID uuid = player.getUUID();

        MoRequirements vRequirements = moAction.getRequirements();
        List<MoRequirement> vRequirementList = vRequirements.getRequirements();
        int requirementsAmount = vRequirementList.size();
        int requirementsAccepted = 0;

        for (MoRequirement vRequirement : vRequirementList) {
            if (vRequirement.isRequirement(RequirementType.EVAL)) {
                RequirementEval requirement = (RequirementEval) vRequirement.getRequirement();
                for (String eval : requirement.getRequirements()) {
                    boolean condition = UtilString.get(eval).setVariables(variables).setVariables(player)
                            .setPlaceholders(uuid).setTimeFormatter().hex().evaluateString();
                    if (condition) {
                        requirementsAccepted = requirementsAccepted + 1;
                        break;
                    }
                }
            }
        }

        if (requirementsAccepted == requirementsAmount) {
            MoRewards moRewards = moAction.getRewards();
            actionResult = new ActionResult(player, level, actionType, new ArrayList<>(variables), new ArrayList<>(Collections.singletonList(moRewards)));
            new Rewards(actionResult).executeDefault().check();
        } else {
            MoRewards moRewards = moAction.getElseRewards();
            actionResult = new ActionResult(player, level, actionType, new ArrayList<>(variables), new ArrayList<>(Collections.singletonList(moRewards)));
            new Rewards(actionResult).executeDefault().check();
        }
        return this;
    }

    public ActionResult getActionResult() { return actionResult; }
}
