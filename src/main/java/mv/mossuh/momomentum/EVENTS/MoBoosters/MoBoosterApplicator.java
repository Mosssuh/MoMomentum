package mv.mossuh.momomentum.EVENTS.MoBoosters;


import mv.mossuh.moboosters.API.Events.PlayerApplyBoostEvent;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class MoBoosterApplicator implements Listener {

    @EventHandler
    public void onApplyBooster(PlayerApplyBoostEvent event) {
        UUID uuid = event.getUUID();
        MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);

        ApplicatorType applicatorType = event.getApplicatorType();
        String boosted = event.getBoosted();

        BoosterIdentifier globalIdentifier = new BoosterIdentifier(Config.PLUGIN_NAME, BoosterType.GLOBAL, applicatorType, boosted);
        BoosterIdentifier superiorSkyblock2Identifier = new BoosterIdentifier(Config.PLUGIN_NAME, BoosterType.SUPERIORSKYBLOCK2, applicatorType, boosted);
        BoosterIdentifier personalIdentifier = new BoosterIdentifier(Config.PLUGIN_NAME, BoosterType.PERSONAL, applicatorType, boosted);

        double global = momentumPlayer.getBoosts(globalIdentifier);
        double superiorSkyblock2 = momentumPlayer.getBoosts(superiorSkyblock2Identifier);
        double personal = momentumPlayer.getBoosts(personalIdentifier);


        double total = personal+global+superiorSkyblock2;
        event.addBoost(total);
    }
}
