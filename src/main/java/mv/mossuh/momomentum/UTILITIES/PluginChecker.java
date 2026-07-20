package mv.mossuh.momomentum.UTILITIES;

import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.EVENTS.PluginCheckerEvent;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.EVENTS.MoBoosters.MoBoosterApplicator;
import mv.mossuh.momomentum.MoMomentum;
import mv.mossuh.momomentum.PAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PluginChecker implements Listener {
    private final MoMomentum instance = MoMomentum.getInstance();

    @EventHandler
    public void onPluginChecker(PluginCheckerEvent event) {
        PluginType pluginType = event.getPluginType();

        switch (pluginType) {
            case PlaceholderAPI:
                new PAPI().register();
                UtilString.get(Config.PREFIX+" &aDetected PlaceholderAPI, used as soft-depend.").hex().sendMessageInConsole();
                break;
            case MoBoosters:
                instance.getServer().getPluginManager().registerEvents(new MoBoosterApplicator(), instance);
                UtilString.get(Config.PREFIX+" &aDetected MoBoosters, used as soft-depend. Enabling classes.").hex().sendMessageInConsole();
                break;
        }
    }
}
