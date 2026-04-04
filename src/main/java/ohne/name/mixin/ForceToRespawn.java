package ohne.name.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import ohne.name.TotemPlayerHandle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerList.class)
public class ForceToRespawn {
    @Redirect(method = "respawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;findRespawnPositionAndUseSpawnBlock(ZLnet/minecraft/world/level/portal/TeleportTransition$PostTeleportTransition;)Lnet/minecraft/world/level/portal/TeleportTransition;"))
    private TeleportTransition replaceFirstLine(ServerPlayer player, boolean keepAllPlayerData, TeleportTransition.PostTeleportTransition postTeleportTransition) {
        if (TotemPlayerHandle.IsTotemDead(player)) {
            ServerPlayer.RespawnConfig config = player.getRespawnConfig();
            if(config != null) {
                ServerLevel level = player.level().getServer().getLevel(config.respawnData().dimension());
                if(level != null) {
                    BlockPos position = config.respawnData().pos();
                    ServerPlayer.RespawnPosAngle respawnAngle = new ServerPlayer.RespawnPosAngle(new Vec3(position.getX() + 0.5, position.getY() + 0.1, position.getZ() + 0.5), config.respawnData().yaw(), config.respawnData().pitch());

                    return new TeleportTransition(level, respawnAngle.position(), Vec3.ZERO, respawnAngle.yaw(), respawnAngle.pitch(), postTeleportTransition);
                }
            }
        }
        return player.findRespawnPositionAndUseSpawnBlock(keepAllPlayerData, postTeleportTransition);
    }
}
