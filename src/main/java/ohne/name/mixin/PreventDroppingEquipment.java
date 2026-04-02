package ohne.name.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public class PreventDroppingEquipment {
    @Inject(method = "dropEquipment", at = @At("HEAD"), cancellable = true)
    protected void dropEquipmentHandle(ServerLevel level, CallbackInfo ci) {
        ci.cancel();
    }

}