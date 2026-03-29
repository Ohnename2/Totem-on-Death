package ohne.name.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import ohne.name.TotemOnDeathClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class HideItemInHands {

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    public void abortRenderring(LivingEntity mob, ItemStack itemStack, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci) {
        if(mob instanceof LocalPlayer) {
            if(TotemOnDeathClient.IsDead) {
                ci.cancel();
            }
        }
    }
}
