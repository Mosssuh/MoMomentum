package mv.mossuh.momomentum.UTILITIES.Momentum.BossBar.reflection;

import mv.mossuh.momomentum.MoMomentum;
import org.bukkit.entity.Player;
import vingma.adventure.adventure.bossbar.BossBar;
import vingma.adventure.adventure.platform.bukkit.BukkitAudiences;
import vingma.adventure.adventure.platform.bukkit.BukkitComponentSerializer;
import vingma.adventure.adventure.text.Component;
import vingma.adventure.adventure.text.TextComponent;

public class MBossBarUtil implements MBossBar<BossBar.Color, BossBar.Overlay> {

    private static final BukkitAudiences AUDIENCES;

    static {
        AUDIENCES = BukkitAudiences.create(MoMomentum.getInstance());
    }

    private final BossBar bossBar;

    public MBossBarUtil(Component title, Color color, Overlay overlay, float percentage) {
        this.bossBar = BossBar.bossBar(title, percentage ,map(color), map(overlay));
    }

    @Override
    public void setTitle(String title) {
        if(this.bossBar == null)
            return;

        TextComponent deserialize = BukkitComponentSerializer.legacy().deserialize(title);
        this.bossBar.name(deserialize);
    }

    @Override
    public void setColor(Color color) {
        if(this.bossBar == null)
            return;

        this.bossBar.color(map(color));
    }

    @Override
    public void setOverlay(Overlay overlay) {
        if(this.bossBar == null)
            return;

        this.bossBar.overlay(map(overlay));
    }

    @Override
    public void show(Player player) {
        if(bossBar == null)
            return;


        AUDIENCES.player(player).showBossBar(bossBar);
    }

    @Override
    public void hide(Player player) {
        if(bossBar == null)
            return;

        AUDIENCES.player(player).hideBossBar(bossBar);
    }

    @Override
    public void showAll() {
        if(bossBar == null)
            return;

        AUDIENCES.players().showBossBar(bossBar);
    }

    @Override
    public void destroy() {
        if(this.bossBar == null)
            return;

        AUDIENCES.players().hideBossBar(bossBar);
    }

    @Override
    public void setPercentage(float percentage) {
        if(this.bossBar == null)
            return;

        this.bossBar.progress(percentage);
    }

    @Override
    public BossBar.Color map(Color color) {
        switch (color) {
            case RED:
                return BossBar.Color.RED;
            case BLUE:
                return BossBar.Color.BLUE;
            case YELLOW:
                return BossBar.Color.YELLOW;
            case PINK:
                return BossBar.Color.PINK;
            case WHITE:
                return BossBar.Color.WHITE;
            case PURPLE:
                return BossBar.Color.PURPLE;
            case GREEN:
            default:
                return BossBar.Color.GREEN;
        }
    }

    @Override
    public BossBar.Overlay map(Overlay overlay) {
        switch (overlay) {
            case NOTCHED_6:
                return BossBar.Overlay.NOTCHED_6;
            case NOTCHED_10:
                return BossBar.Overlay.NOTCHED_10;
            case NOTCHED_12:
                return BossBar.Overlay.NOTCHED_12;
            case NOTCHED_20:
                return BossBar.Overlay.NOTCHED_20;
            case PROGRESS:
            default:
                return BossBar.Overlay.PROGRESS;
        }
    }
}

