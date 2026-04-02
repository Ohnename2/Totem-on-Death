package ohne.name.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import ohne.name.TotemPlayerHandle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class DropItem {
    @Inject(method = "respawn", at = @At("HEAD"))
    public void dropItems(ServerPlayer serverPlayer, boolean keepAllPlayerData, Entity.RemovalReason removalReason, CallbackInfoReturnable<ServerPlayer> cir) {
        TotemPlayerHandle.dropInventory(serverPlayer);
    }
}
