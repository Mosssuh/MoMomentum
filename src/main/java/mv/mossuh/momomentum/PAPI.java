package mv.mossuh.momomentum;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.moboosters.ENUMS.ApplicatorType;
import mv.mossuh.moboosters.ENUMS.BoosterType;
import mv.mossuh.moboosters.UTILITIES.UtilMethods;
import mv.mossuh.momomentum.API.MomentumAPI;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.ConfigLevels;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.DATA.TimerStateData;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import mv.mossuh.momomentum.LEVELS.MomentumState.TimerState;
import mv.mossuh.momomentum.UTILITIES.UtilString;
import mv.mossuh.momomentum.UTILITIES.MoBoosters.LocalBooster;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PAPI extends PlaceholderExpansion {
    private final MoMomentum instance = MoMomentum.getInstance();

    /**
     * Because this is an internal class,
     * you must override this method to let PlaceholderAPI know to not unregister your expansion class when
     * PlaceholderAPI is reloaded
     *
     * @return true to persist through reloads
     */
    @Override
    public boolean persist(){
        return true;
    }
    /**
     * Since this expansion requires api access to the plugin "SomePlugin"
     * we must check if said plugin is on the server or not.
     *
     * @return true or false depending on if the required plugin is installed.
     */
    @Override
    public boolean canRegister(){
        return true;
    }

    /**
     * The name of the person who created this expansion should go here.
     *
     * @return The name of the author as a String.
     */
    @Override
    public String getAuthor(){
        return "Mossuh";
    }

    /**
     * The placeholder identifier should go here.
     * <br>This is what tells PlaceholderAPI to call our onRequest
     * method to obtain a value if a placeholder starts with our
     * identifier.
     * <br>This must be unique and can not contain % or _
     *
     * @return The identifier in {@code %<identifier>_<value>%} as String.
     */
    @Override
    public String getIdentifier(){
        return "momomentum";
    }

    /**
     * This is the version of this expansion.
     * <br>You don't have to use numbers, since it is set as a String.
     *
     * @return The version as a String.
     */
    @Override
    public String getVersion(){
        return instance.getDescription().getVersion();
    }

    /**
     * This is the method called when a placeholder with our identifier
     * is found and needs a value.
     * <br>We specify the value identifier in this method.
     * <br>Since version 2.9.1 can you use OfflinePlayers in your requests.
     *
     * @param  player
     *         A {@link Player Player}.
     * @param  identifier
     *         A String containing the identifier/value.
     *
     * @return possibly-null String of the requested identifier.
     */

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        UUID uuid = player.getUniqueId();

        /*

          - '%momomentum_cost_<level>%' # Config
          - '%momomentum_bossbar_message_<level>%' # Config
          - '%momomentum_boost_<level>_<booster type>_<applicator type>_<boosted>%' # Config

          - '%momomentum_actual_level%'
          - '%momomentum_actual_cost%'
          - '%momomentum_actual_bossbar_message%'
          - '%momomentum_actual_boost_<booster type>_<applicator>_<boosted>%'
          - '%momomentum_actual_time_formatted%'
          - '%momomentum_actual_time%'

          - '%momomentum_highest_time_formatted%'
          - '%momomentum_highest_time%'

          - '%momomentum_total_boost_<booster type>_<applicator>_<boosted>%'

         */

        if (identifier.toLowerCase().startsWith("cost_")) {
            // %momomentum_cost_<level>%

            String levelString = identifier.replaceAll(" ", "").replace("cost_", "");
            int levelInteger = Integer.parseInt(levelString);
            Level level = ConfigLevels.getLevel(levelInteger);
            return String.valueOf(level.getCost());
        } else if (identifier.toLowerCase().startsWith("bossbar_message_")) {
            // %momomentum_bossbar_message_<level>%

            String levelString = identifier.replaceAll(" ", "").replace("bossbar_message_", "");
            int levelInteger = Integer.parseInt(levelString);
            Level level = ConfigLevels.getLevel(levelInteger);
            if (level.isValid()) {
                return UtilString.get(level.getBossBarText()).hex().setPlaceholders(player).setPlaceholders(player).setDefaultPlayerVariables(player).setDefaultLevelVariables(level)
                        .setDefaultNumberRandomVariable().setTimeFormatter().setMathPlaceholder().setChangeOutputPlaceholder().apply();
            }
        } else if (identifier.toLowerCase().startsWith("boost_")) {
            // %momomentum_boost_<level>_<booster type>_<applicator type>_<boosted>%
            String[] stringSplit = identifier.replaceAll(" ", "").replace("boost_", "").split("_", 4);
            if (stringSplit.length == 4) {
                int levelInteger = Integer.parseInt(stringSplit[0]);
                BoosterType boosterType = UtilMethods.getBoosterType(stringSplit[1]);
                ApplicatorType applicatorType = ApplicatorType.convert(stringSplit[2]);
                String boosted = stringSplit[3];

                Level level = ConfigLevels.getLevel(levelInteger);
                LocalBooster booster = level.getBooster(new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted));
                return String.valueOf(booster.getBoost());
            }
        } else if (identifier.toLowerCase().startsWith("actual_level")) {
            // %momomentum_actual_level%
            MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);
            if (momentumPlayer.isPlayer()) {
                Level level = momentumPlayer.getActualLevel();
                return String.valueOf(level.getLevel());
            }
        } else if (identifier.toLowerCase().startsWith("actual_cost")) {
            // %momomentum_actual_cost%
            MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);
            if (momentumPlayer.isPlayer()) {
                Level level = momentumPlayer.getActualLevel();
                return String.valueOf(level.getCost());
            }
        } else if (identifier.toLowerCase().startsWith("actual_bossbar_message")) {
            // %momomentum_actual_bossbar_message%
            MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);
            if (momentumPlayer.isPlayer()) {
                Level level = momentumPlayer.getActualLevel();
                if (level.isValid()) {
                    return UtilString.get(level.getBossBarText()).hex().setPlaceholders(player).setPlaceholders(player).setDefaultPlayerVariables(player).setDefaultMomentumPlayerVariables(momentumPlayer)
                            .setDefaultNumberRandomVariable().setTimeFormatter().setMathPlaceholder().setChangeOutputPlaceholder().apply();
                }
            }
        } else if (identifier.toLowerCase().startsWith("actual_boost_")) {
            // %momomentum_actual_boost_<booster type>_<applicator>_<boosted>%
            MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);

            String[] stringSplit = identifier.replaceAll(" ", "").replace("actual_boost_", "").split("_", 3);
            if (stringSplit.length == 3) {
                BoosterType boosterType = UtilMethods.getBoosterType(stringSplit[0]);
                ApplicatorType applicatorType = ApplicatorType.convert(stringSplit[1]);
                String boosted = stringSplit[2];

                Level level = momentumPlayer.getActualLevel();
                LocalBooster booster = level.getBooster(new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted));
                return String.valueOf(booster.getBoost());
            }
        } else if (identifier.toLowerCase().startsWith("actual_time_formatted")) {
            // %momomentum_actual_time_formatted%
            MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);
            TimerState timer = momentumPlayer.getTimerState();
            return timer.getElapsedTime(true);
        } else if (identifier.toLowerCase().startsWith("actual_time")) {
            // %momomentum_actual_time%
            MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);
            TimerState timer = momentumPlayer.getTimerState();
            long time = timer.getElapsedTime();
            return String.valueOf(time);
        } else if (identifier.toLowerCase().startsWith("total_boost_")) {
            // %momomentum_total_boost_<booster type>_<applicator>_<boosted>%
            MomentumPlayer momentumPlayer = MomentumAPI.getManager().getPlayer(uuid);

            String[] stringSplit = identifier.replaceAll(" ", "").replace("total_boost_", "").split("_", 3);
            if (stringSplit.length == 3) {
                BoosterType boosterType = UtilMethods.getBoosterType(stringSplit[0]);
                ApplicatorType applicatorType = ApplicatorType.convert(stringSplit[1]);
                String boosted = stringSplit[2];

                BoosterIdentifier boosterIdentifier = new BoosterIdentifier(Config.PLUGIN_NAME, boosterType, applicatorType, boosted);
                return String.valueOf(momentumPlayer.getBoosts(boosterIdentifier));
            }
        } else if (identifier.toLowerCase().startsWith("highest_time_formatted")) {
            // %momomentum_highest_time_formatted%
            TimerState timer = TimerStateData.getTimer(uuid);
            return timer.getElapsedTime(true);
        } else if (identifier.toLowerCase().startsWith("highest_time")) {
            // %momomentum_highest_time%
            TimerState timer = TimerStateData.getTimer(uuid);
            long time = timer.getElapsedTime();
            return String.valueOf(time);
        }

        return "";
    }
}
