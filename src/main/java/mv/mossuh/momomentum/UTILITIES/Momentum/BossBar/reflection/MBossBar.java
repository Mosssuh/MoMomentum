package mv.mossuh.momomentum.UTILITIES.Momentum.BossBar.reflection;

import org.bukkit.entity.Player;
import vingma.adventure.adventure.platform.bukkit.BukkitComponentSerializer;
import vingma.adventure.adventure.text.TextComponent;

public interface MBossBar<TColor, TOverlay> {

    static MBossBar<?, ?> build(String title, Color color, Overlay overlay, float percentage) {
//        System.out.println(PaperLib.getMinecraftVersion());
//        if(PaperLib.getMinecraftVersion() == 8)
//            return new CommunityBossBar_1_8(title, color, overlay, percentage);

        TextComponent deserialize = BukkitComponentSerializer.legacy().deserialize(title);
        return new MBossBarUtil(deserialize, color, overlay, percentage);
    }

    enum Color {
        PINK,
        BLUE,
        RED,
        GREEN,
        YELLOW,
        PURPLE,
        WHITE;
    }

    enum Overlay {
        PROGRESS,
        NOTCHED_6,
        NOTCHED_10,
        NOTCHED_12,
        NOTCHED_20;

    }

    void setTitle(String title);

    void setColor(Color color);

    void setOverlay(Overlay overlay);

    void show(Player player);

    void hide(Player player);

    void showAll();

    void destroy();

    void setPercentage(float percentage);


    TColor map(Color color);

    TOverlay map(Overlay overlay);

}
