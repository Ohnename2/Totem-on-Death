package ohne.name.mixin;

import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.entity.Avatar;
import ohne.name.TotemOnDeathClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(AvatarRenderer.class)
public class HidePlayer<AvatarlikeEntity extends Avatar & ClientAvatarEntity> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("HEAD"), cancellable = true)
    public void stopPlayerRenderer(AvatarlikeEntity entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if(entity instanceof LocalPlayer) {
            if(TotemOnDeathClient.IsDead) {
                ci.cancel();
            }
        } else if (entity instanceof RemotePlayer remotePlayer) {
            UUID entityUUID = entity.getUUID();
            for (UUID uuid : TotemOnDeathClient.DeadPlayers) {
                if (uuid.equals(entityUUID)) {
                    ci.cancel();
                    return;
                }
            }
        }
    }
}