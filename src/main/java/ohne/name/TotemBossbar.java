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
        bossbar = new ServerBossEvent(entity.getUUID() ,Component.literal(" "),BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
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

    public void setRemainingTime(Long time, int Unit) {
        switch (Unit) {
            case 0:
                bossbar.setName(Component.translatable("text.totem-on-death.bossbar.seconds", time ));
                break;
            case 1:
                bossbar.setName(Component.translatable("text.totem-on-death.bossbar.minutes", time ));
                break;
            case 2:
                bossbar.setName(Component.translatable("text.totem-on-death.bossbar.hours", time ));
                break;
            case 3:
                bossbar.setName(Component.translatable("text.totem-on-death.bossbar.days", time ));
            default:
                bossbar.setName(Component.translatable("text.totem-on-death.bossbar.seconds", time ));
        }
    }
}
