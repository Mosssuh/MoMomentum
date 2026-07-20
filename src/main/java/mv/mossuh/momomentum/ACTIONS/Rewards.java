package mv.mossuh.momomentum.ACTIONS;

import mv.mossuh.mocore.ACTIONS.ActionUtil.MoAction;
import mv.mossuh.mocore.ACTIONS.ActionUtil.MoActions;
import mv.mossuh.mocore.ACTIONS.RewardUtil.AvailableRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoReward;
import mv.mossuh.mocore.ACTIONS.RewardUtil.MoRewards;
import mv.mossuh.mocore.ACTIONS.RewardUtil.SelectedReward;
import mv.mossuh.mocore.ENUMS.RewardReceiverType;
import mv.mossuh.mocore.ENUMS.RewardType;
import mv.mossuh.mocore.UTILITIES.ARGS.VariableArgs.VariableArg;
import mv.mossuh.mocore.UTILITIES.REWARDS.RewardReceiver;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.ENUMS.ActionType;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.MoMomentum;
import mv.mossuh.momomentum.UTILITIES.RewardMethods;
import mv.mossuh.momomentum.UTILITIES.UtilMethods;
import mv.mossuh.momomentum.UTILITIES.UtilString;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;

public class Rewards {
    private final MoMomentum instance = MoMomentum.getInstance();
    private boolean executeDefaultRewards = false;

    private ActionResult actionResult = new ActionResult(null,null, null, null, null);
    private int times = 1;


    public Rewards(ActionResult actionResult) {
        if (actionResult != null) { this.actionResult = actionResult; }
    }

    public Rewards(ActionResult actionResult, Integer times) {
        if (actionResult != null) { this.actionResult = actionResult; }
        if (times != null) { this.times = times; }
    }

    public Rewards executeDefault() {
        this.executeDefaultRewards = true;
        return this;
    }

    public Rewards check() {
        if(!Bukkit.isPrimaryThread()){
            new BukkitRunnable(){
                @Override
                public void run() {
                    for (int i = 0; i < times; i++) {
                        rewards();
                    }
                }
            }.runTask(instance);
        } else{
            for (int i = 0; i < times; i++) {
                rewards();
            }
        }
        return this;
    }

    private void rewards() {
        MomentumPlayer momentumPlayer = actionResult.getPlayer();
        UUID uuid = momentumPlayer.getUUID();
        Level level = actionResult.getLevel();
        ActionType actionType = actionResult.getActionType();
        MoActions actions = level.getActions(actionType);
        List<VariableArg> variables = actionResult.getVariables();

        List<MoRewards> approvedRewards = actionResult.getApprovedRewards();

        if (actionResult.hasApprovedRewards()) {
            for (MoRewards moRewards : approvedRewards) {
                List<MoReward> rewardList = moRewards.getRewards();
                for (MoReward moReward : rewardList) {

                    String rewardChance = moReward.getChance();
                    //ChanceType chanceType = vReward.getChanceType();
                    double chance = UtilMethods.transformChance(uuid, rewardChance);
                    double random = UtilMethods.randomNumber();

                    if (random >= chance) {
                        List<AvailableRewards> availableRewardsList = moReward.getAvailableRewards();
                        if (!availableRewardsList.isEmpty()) {
                            for (AvailableRewards availableRewards : availableRewardsList) {
                                if (availableRewards.hasReward()) {
                                    LivingEntity entityReceiver = null;
                                    SelectedReward randomSelectedReward = availableRewards.getRandomReward();
                                    RewardReceiver rewardReceiver = randomSelectedReward.getRewardReceiver();
                                    RewardReceiverType rewardReceiverType = rewardReceiver.getRewardReceiverType();
                                    String reward = null;
                                    if (rewardReceiverType.equals(RewardReceiverType.PLACEHOLDER)) {
                                        if (rewardReceiver.hasRewardReceiver()) {
                                            String rewardReceiverString = UtilString.get(rewardReceiver.getRewardReceiver()).setDefaultNumberRandomVariable().setVariables(variables)
                                                    .setPlaceholders(uuid).setChangeOutputPlaceholder().setMathPlaceholder().setTimeFormatter().apply();
                                            Player rewardPlayer = Bukkit.getPlayer(rewardReceiverString);
                                            if (rewardPlayer != null && rewardPlayer.isOnline()) {
                                                entityReceiver = rewardPlayer;
                                            }
                                        }
                                    } else {
                                        Player player = Bukkit.getPlayer(uuid);
                                        if (UtilMethods.isOnline(player)) {
                                            entityReceiver = player;
                                        }
                                    }
                                    reward = UtilString.get(randomSelectedReward.getReward()).hex().setDefaultNumberRandomVariable()
                                            .setVariables(variables).setPlaceholders(entityReceiver).setChangeOutputPlaceholder().setMathPlaceholder()
                                            .setTimeFormatter().apply();

                                    EntityType entityReceiverType = EntityType.UNKNOWN;
                                    if (entityReceiver != null) {
                                        entityReceiverType = entityReceiver.getType();
                                    }

                                    RewardType rewardType = randomSelectedReward.getRewardType();
                                    if (reward == null) {
                                        continue;
                                    }

                                    if (executeDefaultRewards) {
                                        if (rewardType.equals(RewardType.CONSOLE_COMMAND)) {
                                            RewardMethods.consoleCommand(reward);
                                        } else if (rewardType.equals(RewardType.PLAYER_COMMAND)) {
                                            RewardMethods.playerCommand(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.PLAYER_COMMAND_AS_OP)) {
                                            RewardMethods.playerCommandAsOP(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.MESSAGE)) {
                                            RewardMethods.playerMessage(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.TITLE)) {
                                            RewardMethods.playerTitle(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.SOUND)) {
                                            RewardMethods.playerSound(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.BROADCAST_MESSAGE)) {
                                            RewardMethods.broadcastMessage(reward);
                                        } else if (rewardType.equals(RewardType.BROADCAST_TITLE)) {
                                            RewardMethods.broadcastTitle(reward);
                                        } else if (rewardType.equals(RewardType.JSON)) {
                                            RewardMethods.json(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.JSON_BROADCAST)) {
                                            RewardMethods.jsonBroadcast(reward);
                                        } else if (rewardType.equals(RewardType.EFFECT)) {
                                            RewardMethods.effect(entityReceiver, reward);
                                        } else if (rewardType.equals(RewardType.EXECUTE_ACTION)) {
                                            if (entityReceiverType.equals(EntityType.PLAYER)) {
                                                MoAction rewardAction = actions.getAction(reward);
                                                new ExecuteAction(rewardAction, momentumPlayer, level, actionType, variables)
                                                        .addAllDefaultVariables().check();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
