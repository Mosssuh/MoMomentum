package mv.mossuh.momomentum.CONFIGS.ConfigLevels;

import java.util.*;

public class ConfigLevels {

    private static final Set<Level> levels = new HashSet<>();

    public static void addLevel(Level information) {
        levels.add(information);
    }
    public static void addLevel(List<Level> information) {
        levels.addAll(information);
    }
    public static void addLevel(Level... information) {
        levels.addAll(Arrays.asList(information));
    }

    public static Level getLevel(Integer level) {
        for (Level l : levels) {
            if (l.getLevel() == level) {
                return l;
            }
        }
        return new Level(null, null, null, null, null, null, null);
    }

    public static int getAvailableLevels() {
        return levels.size();
    }

}
