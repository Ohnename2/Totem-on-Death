package ohne.name.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ServerboundClientCommandPacket;
import net.minecraft.stats.Stats;
import ohne.name.TotemOnDeathClient;
import ohne.name.networking.TotemRespawnRequest;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.UUID;

@Mixin(DeathScreen.class)
public abstract class AddDeathUI extends Screen {
    @Shadow
    @Final
    private List<Button> exitButtons;

    @Shadow
    @Final
    private boolean hardcore;

    @Shadow
    @Final
    private LocalPlayer player;

    @Shadow
    protected abstract void setButtonsActive(boolean isActive);

    protected AddDeathUI(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci) {
        if (!TotemOnDeathClient.souldShowDeathUI || player.level().getLevelData().isHardcore()) {
            return;
        }
        Component message = Component.translatable("deathScreen.totem-on-death.respawn");
        this.exitButtons.add((Button) this.addRenderableWidget(Button.builder(message, (button) -> {
            TotemRespawnRequest payload = new TotemRespawnRequest(UUID.randomUUID());
            ClientPlayNetworking.send(payload);
            this.player.respawn();
            button.active = false;
        }).bounds(this.width / 2 - 100, this.height / 4 + 48, 200, 20).build()));
        this.setButtonsActive(false);
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/Component;translatable(Ljava/lang/String;)Lnet/minecraft/network/chat/MutableComponent;", ordinal = 1))
    public MutableComponent changeRespawnInfo(String key) {
        this.minecraft.getConnection().send(new ServerboundClientCommandPacket(ServerboundClientCommandPacket.Action.REQUEST_STATS));
        return Component.translatable("ui.totem-on-death.custom.respawn.text", TotemOnDeathClient.ItemsToDestroy);
    }
}
