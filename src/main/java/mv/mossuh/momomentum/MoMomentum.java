package mv.mossuh.momomentum;

import mv.mossuh.mocore.UTILITIES.UsefulString;
import mv.mossuh.momomentum.COMMANDS.Commands;
import mv.mossuh.momomentum.COMMANDS.TabCompleter;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.MANAGERS.ConfigsManager;
import mv.mossuh.momomentum.CONFIGS.Messages;
import mv.mossuh.momomentum.COOLDOWN.LevelStateCooldown;
import mv.mossuh.momomentum.DATA.LevelStateData;
import mv.mossuh.momomentum.DATA.TimerStateData;
import mv.mossuh.momomentum.ENUMS.MomentumEventType;
import mv.mossuh.momomentum.EVENTS.ExecuteActions.CompletedLevelActions;
import mv.mossuh.momomentum.EVENTS.ExecuteActions.ReducedLevelActions;
import mv.mossuh.momomentum.EVENTS.LevelDown.PlayerLeave;
import mv.mossuh.momomentum.EVENTS.LevelUp.BlockBreak;
import mv.mossuh.momomentum.EVENTS.RegisterMomentumPlayer;
import mv.mossuh.momomentum.EVENTS.MoBoosters.MoBoosterApplicator;
import mv.mossuh.momomentum.UTILITIES.PluginChecker;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public final class MoMomentum extends JavaPlugin {

    PluginDescriptionFile pdffile = getDescription();
    public String version = pdffile.getVersion();

    private static MoMomentum instance;
    private static ConfigsManager configsManager;

    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic
        configsManager = new ConfigsManager(this);
        configsManager.configure();
        Messages.load(this);


        MomentumEventType momentumEventType = Config.MOMENTUM_EVENT;
        if (momentumEventType == MomentumEventType.BLOCKBREAK) {
            getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        }
        registerEvents();
        registerCommands();

        TimerStateData.registerDatabaseInMaps();

        if (Config.HAS_PLAYER_TIME_TO_AVOID_MOMENTUM_RESTART) {
            LevelStateData.registerDatabaseInPlayers();
            LevelStateCooldown.initializeCooldowns();
        }

        startIntervalSave();

        UsefulString.get(Config.PREFIX+" &aHas been enabled. Created by &bMossuh&a.").hex().sendMessageInConsole();
        UsefulString.get(Config.PREFIX+" &aVersion: " + version).hex().sendMessageInConsole();
    }

    @Override
    public void onDisable() {
        stopIntervalSave();
        if (Config.HAS_PLAYER_TIME_TO_AVOID_MOMENTUM_RESTART) {
            LevelStateData.registerPlayersInDatabase();
        }

        TimerStateData.registerTimersInMaps();
        TimerStateData.registerMapsInDatabase();
        UsefulString.get(Config.PREFIX+" &aHas been disabled. Created by &bMossuh&a.").hex().sendMessageInConsole();
        UsefulString.get(Config.PREFIX+" &aVersion: " + version).hex().sendMessageInConsole();
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new RegisterMomentumPlayer(), this);

        getServer().getPluginManager().registerEvents(new MoBoosterApplicator(), this);
        getServer().getPluginManager().registerEvents(new CompletedLevelActions(), this);

        if (Config.PLAYER_LEAVE_EXECUTE_REDUCED_ACTIONS) {
            getServer().getPluginManager().registerEvents(new ReducedLevelActions(), this);
        }

        getServer().getPluginManager().registerEvents(new PlayerLeave(), this);

        getServer().getPluginManager().registerEvents(new PluginChecker(), this);
    }

    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("momomentum")).setExecutor(new Commands());
        Objects.requireNonNull(this.getCommand("momomentum")).setTabCompleter(new TabCompleter());
    }

    public static MoMomentum getInstance() { return instance; }

    public ConfigsManager getConfigs() {
        return configsManager;
    }




    private static Timer intervalSave;
    public static void startIntervalSave() {
        intervalSave = new Timer();
        intervalSave.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                TimerStateData.registerTimersInMaps();
                TimerStateData.registerMapsInDatabase();
                UsefulString.get(Config.PREFIX+" &aSaving player data in files...").hex().sendMessageInConsole();
            }
        }, 60000, Config.SAVE_IN_DATABASE_INTERVAL * 1000);
    }

    public static void stopIntervalSave() {
        if (intervalSave != null) {
            intervalSave.cancel();
        }
    }
}
