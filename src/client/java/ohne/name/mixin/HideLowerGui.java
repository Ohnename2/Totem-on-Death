package ohne.name.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import ohne.name.TotemOnDeathClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class HideLowerGui {

    @Inject(method = "renderHotbarAndDecorations", at = @At("HEAD"), cancellable = true)
    public void HideGui(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(TotemOnDeathClient.IsDead) {
            ci.cancel();
        }
    }

    @Inject(method = "renderCameraOverlays", at = @At("HEAD"), cancellable = true)
    public void HideHand(GuiGraphics graphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if(TotemOnDeathClient.IsDead) {
            ci.cancel();
        }
    }
}
