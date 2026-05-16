package ohne.name.mixin;

import net.minecraft.client.player.LocalPlayer;
import ohne.name.TotemOnDeathClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class PreventDroppingHotbar {
    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void preventHotbarDrop(boolean all, CallbackInfoReturnable<Boolean> cir) {
        if(TotemOnDeathClient.IsDead) {
            cir.setReturnValue(false);
        }
    }
}
