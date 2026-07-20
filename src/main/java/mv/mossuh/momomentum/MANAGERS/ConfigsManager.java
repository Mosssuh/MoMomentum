package mv.mossuh.momomentum.MANAGERS;

import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoActions;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirement;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.MoRequirements;
import mv.mossuh.mocore.ACTIONS.RequirementUtil.RequirementEval;
import mv.mossuh.mocore.ACTIONS.RewardUtil.AvailableRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoReward;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.SelectedReward;
import mv.mossuh.mocore.CONFIG.FolderConfigs;
import mv.mossuh.mocore.CONFIG.MoConfig;
import mv.mossuh.mocore.ENUMS.ChanceType;
import mv.mossuh.mocore.ENUMS.PluginType;
import mv.mossuh.mocore.ENUMS.RequirementType;
import mv.mossuh.mocore.UTILITIES.PluginsChecker;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.ConfigLevels;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.CONFIGS.Messages;
import mv.mossuh.momomentum.ENUMS.ActionType;
import mv.mossuh.momomentum.MoMomentum;
import mv.mossuh.momomentum.UTILITIES.UtilString;
import mv.mossuh.momomentum.UTILITIES.MoBoosters.LocalBooster;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConfigsManager {
    private MoMomentum instance;
    private static MoConfig mainConfig;
    private static MoConfig messagesConfig;
    private static MoConfig informationConfig;
    private static MoConfig timerStateData;
    private static MoConfig levelStateData;
    private static FolderConfigs levels;
    public ConfigsManager(MoMomentum main) {
        this.instance = main;
        mainConfig = new MoConfig("config.yml", main, null);
        messagesConfig = new MoConfig("messages.yml", main, null);
        informationConfig = new MoConfig("information.yml", main, null);
        timerStateData = new MoConfig("timerstatedata.db", main, "datas");
        levelStateData = new MoConfig("levelstatedata.db", main, "datas");
        levels = new FolderConfigs(true, main, "levels");
    }

    public void configure() {
        mainConfig.registerConfig(true);
        messagesConfig.registerConfig(true);
        informationConfig.registerConfig(true);
        timerStateData.registerConfig(false);
        levelStateData.registerConfig(false);
        levels.configure("levels1-3.yml", "levels4-6.yml");
        Config.load(instance);

        configureLevels();
    }
    public MoConfig getMainConfig() {
        return mainConfig;
    }
    public MoConfig getMessages() { return messagesConfig; }

    public MoConfig getPlayerTimerData() { return timerStateData; }
    public MoConfig getPlayerLevelData() { return levelStateData; }

    public List<MoConfig> getLevelConfigs() { return levels.getConfigs(); }

    public List<MoConfig> getConfigs() {
        List<MoConfig> configs = new ArrayList<>();

        configs.add(mainConfig);
        configs.addAll(levels.getConfigs());

        return configs;
    }

    public void reload(){
        mainConfig.reloadConfig();
        messagesConfig.reloadConfig();
        levels.reloadConfigs();
        Config.load(instance);
        Messages.load(instance);

        configureLevels();
    }


    public void configureLevels() {
        List<MoConfig> MoConfigs = getConfigs();

        int amount = 0;
        List<Level> levels  = new ArrayList<>();

        for (MoConfig MoConfig : MoConfigs) {
            FileConfiguration config = MoConfig.getConfig();

            if (config.contains("Levels")) {
                for (String section : config.getConfigurationSection("Levels").getKeys(false)) {
                    if (!config.contains("Levels." + section + ".level") || config.getInt("Levels." + section + ".level") <= 0) {
                        continue;
                    }
                    int level = config.getInt("Levels." + section + ".level");

                    if (!config.contains("Levels." + section + ".cost") || config.getDouble("Levels." + section + ".cost") <= 0) {
                        continue;
                    }
                    double cost = config.getInt("Levels." + section + ".cost");

                    String bossBarText = "";
                    if (config.contains("Levels." + section + ".boss-bar-text")) {
                        bossBarText = config.getString("Levels." + section + ".boss-bar-text");
                    }

                    List<LocalBooster> boosters = new ArrayList<>();
                    if (PluginsChecker.isPluginEnabled(PluginType.MoBoosters)) {
                        for (String boosterSection : config.getConfigurationSection("Levels." + section + ".boosters").getKeys(false)) {
                            BoosterType boosterType = UtilMethods.getBoosterType(config.getString("Levels." + section + ".boosters." + boosterSection + ".type"));
                            ApplicatorType applicatorType = UtilMethods.getApplicatorType(config.getString("Levels." + section + ".boosters." + boosterSection + ".applicator"));
                            String boosted = config.getString("Levels." + section + ".boosters." + boosterSection + ".boosted");
                            Double boost = config.getDouble("Levels." + section + ".boosters." + boosterSection + ".boost");

                            LocalBooster booster = new LocalBooster(new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted), boost);
                            if (!booster.isValid()) {
                                continue;
                            }

                            boosters.add(booster);
                        }
                    }

                    MoActions completedActions = new MoActions(transformToActions(section, ActionType.COMPLETED, config));
                    MoActions reducedActions = new MoActions(transformToActions(section, ActionType.REDUCED, config));

                    levels.add(new Level(level, cost, bossBarText, boosters, completedActions, reducedActions, false));
                    amount += 1;
                }

                Double maxCost = mainConfig.getConfig().getDouble("Config.max-level-cost");
                Level maxLevel = new Level(levels.size() + 1, maxCost, null, null, null, null, true);
                levels.add(maxLevel);
            }
        }

        UtilString.get(Config.PREFIX+" &aLoaded " + amount +  " levels of the configurations!").hex().sendMessageInConsole();
        ConfigLevels.addLevel(levels);
    }


    private static List<MoAction> transformToActions(String section, ActionType actionType, FileConfiguration config) {
        String actionTypeString = actionType.name().toLowerCase();
        List<MoAction> actionList = new ArrayList<>();
        if (config.contains("Levels." + section + ".actions." + actionTypeString) && !config.getString("Levels." + section + ".actions." + actionTypeString).isEmpty()) {
            for (String actionName : config.getConfigurationSection("Levels." + section + ".actions." + actionTypeString).getKeys(false)) {

                List<MoRequirement> requirementList = new ArrayList<>();
                boolean cancelAction = config.getBoolean("Levels." + section + ".actions." + actionTypeString + "." + actionName + ".cancel_action");

                if (config.contains("Levels." + section + ".actions."  + actionTypeString + "." + actionName + ".requirements")) {
                    List<String> requirementsAsString = config.getStringList("Levels." + section + ".actions." + actionTypeString + "." + actionName + ".requirements");
                    for (String requirementString : requirementsAsString) {
                        String[] requirementSplit = requirementString.split(" ", 2);
                        String requirementTypeAsString = requirementSplit[0].replace("]", "").replace("[", "").toUpperCase();
                        RequirementType requirementType = RequirementType.valueOf(requirementTypeAsString.toUpperCase());
                        String requirement = requirementSplit[1];
                        MoRequirement requirementClass = new MoRequirement(null, null);
                        if (requirementType.equals(RequirementType.EVAL)){
                            requirementClass = new MoRequirement(requirementType, RequirementEval.separateEvals(requirement));
                        }
                        requirementList.add(requirementClass);
                    }
                }

                List<MoReward> rewardList = new ArrayList<>();

                if (config.contains("Levels." + section + ".actions." + actionTypeString + "." + actionName + ".rewards")) {
                    List<String> rewardsAsString = config.getStringList("Levels." + section + ".actions." + actionTypeString + "." + actionName + ".rewards");
                    for (String rewardString : rewardsAsString) {
                        String[] rewardSplit1 = rewardString.split("] ", 2);

                        String[] rewardSplit2 = rewardSplit1[0].replaceAll(" ", "").replace("]", "").replace("[", "").split("->", 2);
                        ChanceType chanceType = ChanceType.valueOf(rewardSplit2[0].toUpperCase());
                        String chance = "100";
                        if (rewardSplit2.length == 2) {
                            chance = rewardSplit2[1];
                        }

                        List<AvailableRewards> availableRewards = transformRandomRewards(rewardSplit1[1]);

                        MoReward reward = new MoReward(chanceType, chance, availableRewards);
                        rewardList.add(reward);
                    }
                }

                List<MoReward> elseRewardList = new ArrayList<>();

                if (config.contains("Levels." + section + ".actions." + actionTypeString + "." + actionName + ".else")) {
                    List<String> rewardsAsString = config.getStringList("Levels." + section + ".actions." + actionTypeString + "." + actionName + ".else");
                    for (String rewardString : rewardsAsString) {
                        String[] rewardSplit1 = rewardString.split("] ", 2);

                        String[] rewardSplit2 = rewardSplit1[0].replaceAll(" ", "").replace("]", "").replace("[", "").split("->", 2);
                        ChanceType chanceType = ChanceType.valueOf(rewardSplit2[0].toUpperCase());
                        String chance = "100";
                        if (rewardSplit2.length == 2) {
                            chance = rewardSplit2[1];
                        }

                        List<AvailableRewards> availableRewards = transformRandomRewards(rewardSplit1[1]);

                        MoReward reward = new MoReward(chanceType, chance, availableRewards);
                        elseRewardList.add(reward);
                    }
                }

                MoRequirements requirements = new MoRequirements(requirementList, null);
                MoRewards rewards = new MoRewards(rewardList, null);
                MoRewards elseRewards = new MoRewards(elseRewardList, null);
                MoAction action = new MoAction(actionName, cancelAction, null, null, requirements, rewards, elseRewards);
                actionList.add(action);
            }
        }
        return actionList;
    }

    @NotNull
    private static List<AvailableRewards> transformRandomRewards(String reward) {
        List<AvailableRewards> availableRewards = new ArrayList<>();

        String[] rewardSplitY = reward.split(" && ");

        for (String rewardOptions : rewardSplitY) {
            String[] separatedRandomReward = rewardOptions.split(" \\|\\| ");
            AvailableRewards randomRewards = new AvailableRewards(SelectedReward.transformToRewards(separatedRandomReward));
            availableRewards.add(randomRewards);
        }
        return availableRewards;
    }
}
