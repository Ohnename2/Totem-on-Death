package ohne.name.mixin;

import net.minecraft.server.level.ServerPlayer;
import ohne.name.TotemPlayerHandle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class PreventhandThrow {
    @Inject(method = "drop(Z)V", at = @At("HEAD"), cancellable = true)
    protected void dropEquipmentHandle(boolean all, CallbackInfo ci) {
        ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
        TotemPlayerHandle playerHandleObject = TotemPlayerHandle.getPlayerHandle(serverPlayer);
        if (playerHandleObject != null) {
            ci.cancel();
        }
    }
}
