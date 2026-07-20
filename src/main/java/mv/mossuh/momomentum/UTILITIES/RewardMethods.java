package mv.mossuh.momomentum.UTILITIES;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class RewardMethods {

    public static void consoleCommand(String command) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        Bukkit.dispatchCommand(console, command);
    }

    public static void playerCommand(LivingEntity entity, String command) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.performCommand(command);
        }
    }

    public static void playerCommandAsOP(LivingEntity entity, String command) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.isOp()) {
                player.performCommand(command);
            } else {
                player.setOp(true);
                player.performCommand(command);
                player.setOp(false);
            }
        }
    }

    public static void playerMessage(LivingEntity entity, String message) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            player.sendMessage(message);
        }
    }

    public static void playerTitle(LivingEntity entity, String titleSubtitle) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String[] space;
            space = titleSubtitle.split("::");
            String title = space[0];
            String subtitle = space[1];
            player.sendTitle(title, subtitle);
        }
    }

    public static void playerSound(LivingEntity entity, String soundString) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            String[] space;
            space = soundString.split("::", 3);
            String sound = space[0];
            float volume = Float.parseFloat(space[1]);
            float pitch = Float.parseFloat(space[2].replace(" ", ""));
            player.playSound(player.getLocation(), Sound.valueOf(sound), volume, pitch);
        }
    }

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage(message);
    }

    public static void broadcastTitle(String titleSubtitle) {
        String title = "";
        String subtitle = "";
        String[] space;
        space = titleSubtitle.split("::");
        title = space[0];
        subtitle = space[1];

        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
            playerOnline.sendTitle(title, subtitle);
        }
    }

    public static void json(LivingEntity entity, String json) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
            Bukkit.dispatchCommand(console, ("tellraw " + player.getName() + " " + json));
        }
    }

    public static void jsonBroadcast(String json) {
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        for (Player playerOnline : Bukkit.getOnlinePlayers()) {
            Bukkit.dispatchCommand(console, ("tellraw " + playerOnline.getName() + " " + json));
        }
    }

    public static void effect(LivingEntity entity, String effectString) {
        if (entity != null) {
            String[] effectSeparate = effectString.replace(" ", "").split("::", 3);
            String effect = effectSeparate[0];
            int duration = Integer.parseInt(effectSeparate[1]) * 20;
            int amplifier = Integer.parseInt(effectSeparate[2]);
            PotionEffect poison = new PotionEffect(Objects.requireNonNull(PotionEffectType.getByName(effect)), duration, amplifier);
            entity.addPotionEffect(poison, true);
        }
    }

}
