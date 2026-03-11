package ohne.name.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import ohne.name.TotemPlayerHandle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PreventSpearAttack {
    @Inject(method = "cannotAttackWithItem", at = @At("HEAD"), cancellable = true)
    private void init(ItemStack itemStack, int i, CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if(player instanceof ServerPlayer serverPlayer) {
            if(TotemPlayerHandle.IsTotemDead(serverPlayer)) {
                cir.setReturnValue(true);
                return;
            }
        }
    }

}
