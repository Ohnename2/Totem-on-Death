package ohne.name.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import ohne.name.TotemOnDeathClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiPlayerGameMode.class)
public class PreventDroppingWhileDead {
    @Inject(method = "dropItem", at = @At("HEAD"), cancellable = true)
    public void dropItem(LocalPlayer player, boolean all, CallbackInfo ci) {
        if(TotemOnDeathClient.IsDead) {
            ci.cancel();
        }
    }
}
