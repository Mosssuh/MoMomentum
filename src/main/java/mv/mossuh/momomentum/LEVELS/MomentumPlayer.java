package mv.mossuh.momomentum.LEVELS;

import mv.mossuh.moboosters.CONFIGS.Booster.BoosterIdentifier;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.ConfigLevels;
import mv.mossuh.momomentum.CONFIGS.ConfigLevels.Level;
import mv.mossuh.momomentum.LEVELS.MomentumState.TimerState;
import mv.mossuh.momomentum.UTILITIES.MoBoosters.LocalBooster;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MomentumPlayer {

    private UUID uuid;
    private boolean isOnline = false;
    private int level = 0;
    private Level levelInformation = new Level(null, null, null, null, null, null, null);
    private double amount = 0;
    private List<Level> levels = new ArrayList<>();
    private TimerState timerState = new TimerState(null, null);
    public MomentumPlayer(UUID uuid, Integer level, Double amount, List<Level> levels, TimerState timerState) {
        this.uuid = uuid;
        if (uuid != null) {
            OfflinePlayer player = Bukkit.getPlayer(uuid);
            if (player != null && player.isOnline()) { this.isOnline = true; }
        }
        if (level != null) { this.level = level; }
        if (amount != null) { this.amount = amount; }
        if (levels != null) { this.levels = levels; }
        if (timerState != null) { this.timerState = timerState; }
    }

    public boolean isActive() {
        return level > 0 && amount > 0;
    }
    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean status) {
        this.isOnline = status;
    }
    public boolean isPlayer() { return uuid != null; }


    public UUID getUUID() { return uuid; }
    public Level getActualLevel() {
        return levelInformation;
    }

    public int getLevel() { return level; }
    public Level getLevel(Integer level) {
        for (Level l : levels) {
            if (l.getLevel() == level) {
                return l;
            }
        }
        return new Level(null, null, null, null, null, null, null);
    }
    public Double getAmount() { return amount; }

    public Level getNextLevel() {
        return ConfigLevels.getLevel(level + 1);
    }

    public Level getOldLevel() {
        return ConfigLevels.getLevel(level - 1);
    }

    public void updateNextLevel() {
        // Si el índice es mayor que el número de secciones no se sube de nivel
        if (level >= ConfigLevels.getAvailableLevels()) {
            return;
        }

        level++;

        Level l = ConfigLevels.getLevel(level);
        levelInformation = l;
        levels.add(l);
    }

    public void updateLevel(int level) {
        int newLevel = level;
        if (level > ConfigLevels.getAvailableLevels()) {
            newLevel = ConfigLevels.getAvailableLevels();
        } else if (level <= 0) {
            newLevel = 0;
        }

        if (level == 0) { levels.clear(); return; }

        this.level = 0;

        for (int i = 0; i < newLevel; i++) {
            this.level++;

            Level l = ConfigLevels.getLevel(this.level);
            levelInformation = l;
            levels.add(l);
        }
    }


    public void removeLevel() {
        if (level > 0) {
            Level l = ConfigLevels.getLevel(level);
            levels.remove(l);
            level--;

            levelInformation = ConfigLevels.getLevel(level);
        }
    }

    public void addAmount(Double amount) {
        Level nextLevel = getNextLevel();
        if (nextLevel.isValid()) {
            double newAmount = this.amount + amount;
            if (newAmount > nextLevel.getCost()) {
                this.amount = nextLevel.getCost();
                return;
            }
            this.amount += amount;
        }
    }

    public void setAmount(Double amount) {
        Level nextLevel = getNextLevel();
        if (nextLevel.isValid()) {
            if (amount > nextLevel.getCost()) {
                this.amount = nextLevel.getCost();
                return;
            }
            if (amount < 0) {
                this.amount = 0;
                return;
            }
            this.amount = amount;
        }
    }

    public void removeAmount(Double amount) {
        double newAmount = this.amount - amount;
        if (newAmount < 0) {
            this.amount = 0;
            return;
        }
        this.amount = newAmount;
    }

    public List<Level> getLevels() { return levels; }
    public TimerState getTimerState() { return timerState; }


    public double getBoosts(BoosterIdentifier identifier) {
        double boost = 0;
        for (Level l : levels) {

            for (LocalBooster booster : l.getBoosters()) {
                if (booster.isValid() && booster.getIdentifier().equals(identifier)) {
                    boost += booster.getBoost();
                }
            }
        }
        return boost;
    }
}
