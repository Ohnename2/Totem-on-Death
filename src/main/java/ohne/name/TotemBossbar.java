package ohne.name;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

public class TotemBossbar {
        ServerPlayer target;
        ServerBossEvent bossbar;

        TotemBossbar(ServerPlayer entity) {
            target = entity;
            bossbar = new ServerBossEvent(Component.literal("Du bist Tot!"),BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
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
}
