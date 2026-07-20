package mv.mossuh.momomentum.CONFIGS.ConfigLevels;

import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoActions;
import mv.mossuh.momomentum.ENUMS.ActionType;
import mv.mossuh.momomentum.UTILITIES.MoBoosters.LocalBooster;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private int level = 0;
    private double cost = 0;
    private String bossBarText = "";
    private List<LocalBooster> boosters = new ArrayList<>();
    private MoActions completedActions = new MoActions(null);
    private MoActions reducedActions = new MoActions(null);
    private boolean max = false;

    public Level(Integer level, Double cost, String bossBarText, List<LocalBooster> boosters, MoActions completedActions, MoActions reducedActions, Boolean max) {
        if (level != null) { this.level = level; }
        if (cost != null) { this.cost = cost; }
        if (bossBarText != null) { this.bossBarText = bossBarText; }
        if (boosters != null) { this.boosters = boosters; }
        if (completedActions != null) { this.completedActions = completedActions; }
        if (reducedActions != null) { this.reducedActions = reducedActions; }
        if (max != null) { this.max = max; }
    }
    public int getLevel() { return level; }
    public double getCost() { return cost; }
    public String getBossBarText() { return bossBarText; }
    public List<LocalBooster> getBoosters() { return boosters; }
    public LocalBooster getBooster(BoosterIdentifier identifier) {
        for (LocalBooster b : boosters) {
            if (b.getIdentifier().equals(identifier)) {
                return b;
            }
        }
        return new LocalBooster(null, null);
    }
    public MoActions getCompletedActions() { return completedActions; }
    public MoActions getReducedActions() { return reducedActions; }
    public MoActions getActions(ActionType actionType) {
        if (actionType.equals(ActionType.COMPLETED)) {
            return completedActions;
        } else if (actionType.equals(ActionType.REDUCED)) {
            return reducedActions;
        }
        return new MoActions(null);
    }
    public boolean isMaxLevel() { return max; }

    public boolean isValid() {
        return level > 0;
    }
}
