package mv.mossuh.momomentum.ACTIONS;

import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoActions;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ENUMS.RequirementType;
import mv.mossuh.mocore.UTILITIES.ARGS.ArgType;
import mv.mossuh.mocore.UTILITIES.ARGS.Args;
import mv.mossuh.mocore.UTILITIES.ARGS.CommandArgs.CommandArgs;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.ENUMS.ActionType;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.UTILITIES.DefaultVariables;
import mv.mossuh.momomentum.UTILITIES.UtilString;

import java.util.*;

public class Requirements {
    private List<MoRewards> approvedRewards = new ArrayList<>();
    private MomentumPlayer player = new MomentumPlayer(null, null, null, null, null);
    private Level level = new Level(null, null, null, null, null, null, null);
    private ActionType actionType = ActionType.NONE;
    private List<VariableArg> variables = new ArrayList<>();
    private ActionResult actionResult = new ActionResult(null, null, null, null, null);

    public Requirements(MomentumPlayer player, Level level, ActionType actionType) {
        if (player != null) { this.player = player; }
        if (level != null) { this.level = level; }
        if (actionType != null) { this.actionType = actionType; }
    }

    public Requirements addVariables(VariableArg... variables) {
        this.variables.addAll(Arrays.asList(variables));
        return this;
    }

    public Requirements addVariables(List<VariableArg> variables) {
        this.variables.addAll(variables);
        return this;
    }

    public Requirements addAllDefaultVariables() {
        addDefaultPlayerVariables();
        return this;
    }

    public Requirements addDefaultPlayerVariables() {
        this.variables.addAll(DefaultVariables.player(player.getUUID()));
        return this;
    }

    public Requirements addArgsVariables(Args args) {
        if (args.getArgType().equals(ArgType.COMMAND) && args.hasArgs()) {
            CommandArgs commandArgs = (CommandArgs) args;
            int size = commandArgs.getArgs().size();
            for (int i = 0; i < size; i++) {
                this.variables.add(new VariableArg("%args_" + (i+1)  + "%", commandArgs.getArg(i)));
            }
        }
        return this;
    }

    public Requirements check() {
        UUID uuid = player.getUUID();
        MoActions actions = level.getActions(actionType);
        List<MoAction> actionList = actions.getActions();

        if (!actionList.isEmpty()) {
            for (MoAction moAction : actionList) {
                if (!moAction.isCancelled()) {
                    MoRequirements requirements = moAction.getRequirements();
                    List<MoRequirement> requirementList = requirements.getRequirements();
                    int requirementsAmount = requirementList.size();
                    int requirementsAccepted = 0;

                    if (!requirementList.isEmpty()) {
                        for (MoRequirement moRequirement : requirementList) {
                            if (moRequirement.isRequirement(RequirementType.EVAL)) {
                                RequirementEval requirement = (RequirementEval) moRequirement.getRequirement();
                                for (String eval : requirement.getRequirements()) {
                                    boolean condition = UtilString.get(eval).hex().setVariables(variables)
                                            .setDefaultNumberRandomVariable().setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder()
                                            .setTimeFormatter().evaluateString();
                                    if (condition) {
                                        requirementsAccepted = requirementsAccepted + 1;
                                        break;
                                    }
                                }
                            }
                        }
                    }

                    if (requirementsAccepted == requirementsAmount) {
                        MoRewards vRewards = moAction.getRewards();
                        this.approvedRewards.add(vRewards);
                    } else {
                        MoRewards vRewards = moAction.getElseRewards();
                        this.approvedRewards.add(vRewards);
                    }
                }
            }
        }
        actionResult = new ActionResult(player, level, actionType, new ArrayList<>(variables), approvedRewards);
        return this;
    }

    public ActionResult getActionResult() { return actionResult; }
}
