package ohne.name;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

public class TotemBossbar {
    final String BaseText = "Du bist Tot! Respawn in";
    ServerPlayer target;
    ServerBossEvent bossbar;

    TotemBossbar(ServerPlayer entity) {
        target = entity;
        bossbar = new ServerBossEvent(Component.literal(BaseText),BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
        bossbar.addPlayer(target);
        bossbar.setProgress(0);
    }

    public void setProgress(float Progress) {
        bossbar.setProgress(Progress);
    }

    public float getProgress() {return bossbar.getProgress();}

    public void removeBossbar() {
        bossbar.removePlayer(target);
        bossbar.setVisible(false);
    }

    public void setRemainingTime(String text) {
        bossbar.setName(Component.literal(BaseText + " " + text));
    }
}
