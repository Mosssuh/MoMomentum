package mv.mossuh.momomentum.UTILITIES.MoBoosters;

import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.momomentum.CONFIGS.Config;

public class LocalBooster {

    private BoosterIdentifier identifier = new BoosterIdentifier(Config.PLUGIN_NAME, null, null, null);
    private double boost = 0;

    public LocalBooster(BoosterIdentifier identifier, Double boost) {
        if (identifier != null) { this.identifier = identifier; }
        if (boost != null) { this.boost = boost; }
    }

    public BoosterIdentifier getIdentifier() { return identifier; }
    public double getBoost() { return boost; }

    public boolean isValid() {
        return identifier.getBoosterType() != BoosterType.NONE
                && identifier.getApplicatorType() != ApplicatorType.NONE
                && !identifier.getBoosted().isEmpty()
                && boost > 0;
    }
}
