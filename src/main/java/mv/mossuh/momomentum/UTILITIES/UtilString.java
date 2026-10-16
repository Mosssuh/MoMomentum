package mv.mossuh.momomentum.UTILITIES;

import mv.mossuh.mocore.UTILITIES.UsefulString;
import mv.mossuh.momomentum.CONFIGS.Config;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.LEVELS.MomentumPlayer;
import org.bukkit.entity.Player;

public class UtilString extends UsefulString<UtilString> {
    protected UtilString(String string) {
        super(string);
    }
    public static UtilString get(String string) {
        return new UtilString(string);
    }

    public UtilString setVariables(MomentumPlayer player) {
        setVariables(DefaultVariables.momentumPlayer(player));
        return this;
    }

    public UtilString setVariables(Level level) {
        setVariables(DefaultVariables.level(level));
        return this;
    }

    public UtilString setVariables(Player player) {
        if (isString()) {
            setVariables(DefaultVariables.player(player));
        }
        return this;
    }

    public UtilString setTimeFormatter() {
        if (isString()) {
            setTimeFormatter(Config.TIME_FORMAT);
        }
        return this;
    }

}
