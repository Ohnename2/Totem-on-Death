package ohne.name.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import ohne.name.TotemOnDeath;
import ohne.name.TotemPlayerHandle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class EntityDataSave {

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void readAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
        ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
        boolean IsDead = valueInput.getBooleanOr(TotemOnDeath.MOD_ID + ":IsDead", false);
        if(IsDead) {
            new TotemPlayerHandle(serverPlayer, valueInput.getLongOr(TotemOnDeath.MOD_ID + ":RespawnTime", -1L), valueInput.getBooleanOr(TotemOnDeath.MOD_ID + ":IsInDeathScreen", false));
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void addAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
        TotemPlayerHandle playerHandleObject = TotemPlayerHandle.getPlayerHandle(serverPlayer);
        if(playerHandleObject == null) {
            valueOutput.putBoolean(TotemOnDeath.MOD_ID + ":IsDead", false);
            valueOutput.putBoolean(TotemOnDeath.MOD_ID + ":IsInDeathScreen", false);
            valueOutput.putLong(TotemOnDeath.MOD_ID + ":RespawnTime", 0L);
            return;
        }
        valueOutput.putBoolean(TotemOnDeath.MOD_ID + ":IsDead", true);
        valueOutput.putBoolean(TotemOnDeath.MOD_ID + ":IsInDeathScreen", playerHandleObject.IsInDeathScreen());
        assert playerHandleObject != null;
        valueOutput.putLong(TotemOnDeath.MOD_ID + ":RespawnTime", playerHandleObject.getRespawnTime());
        playerHandleObject.setSaved();
    }
}