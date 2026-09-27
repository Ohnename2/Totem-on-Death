package ohne.name.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import ohne.name.TotemOnDeathClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class HideLowerGui {

    @Inject(method = "extractHotbarAndDecorations", at = @At("HEAD"), cancellable = true)
    public void HideGui(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(TotemOnDeathClient.IsDead) {
            ci.cancel();
        }
    }

    @Inject(method = "extractCameraOverlays", at = @At("HEAD"), cancellable = true)
    public void HideHand(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(TotemOnDeathClient.IsDead) {
            ci.cancel();
        }
    }
}
