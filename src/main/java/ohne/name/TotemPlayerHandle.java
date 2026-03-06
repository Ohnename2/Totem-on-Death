package ohne.name;

import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.UUID;

public class TotemPlayerHandle {
    static TotemPlayerHandle[] PlayerHandleObjects = new TotemPlayerHandle[1];

    UUID id;
    Player player;
    ServerPlayer Serverplayer;
    TotemEntity entity;
    BlockPos DeathPos;
    ServerLevel DeathLevel;
    boolean playerIsRespawned = false;
    boolean isInVoid = false;
    TotemBossbar bossbar;

    public static void initialize() {}

    public static TotemPlayerHandle[] getPlayerHandleObjects() {
        return PlayerHandleObjects;
    }

    public static boolean IsTotemDead(TotemPlayerHandle playerObject) {
        for (TotemPlayerHandle currentPlayerObject : PlayerHandleObjects) {
            if(currentPlayerObject == null) {continue;}
            if (currentPlayerObject == playerObject) {
                return true;
            }
        }
        return false;
    }

    public static boolean IsTotemDead(ServerPlayer player) {
        UUID playeruuid = player.getUUID();
        for (TotemPlayerHandle PlayerHandleObject : PlayerHandleObjects) {
            if(PlayerHandleObject == null) {continue;}
            UUID currentObjectUuid = PlayerHandleObject.getUUID();
            if(currentObjectUuid == null) {continue;}
            if (currentObjectUuid.compareTo(playeruuid) == 0) {
                System.out.println(1);
                return true;
            }
        }
        System.out.println(0);
        return false;
    }

    private static void addPlayerObject(TotemPlayerHandle playerObject) {
        for (int i = 0; i < PlayerHandleObjects.length; i++) {
            if (PlayerHandleObjects[i] == null) {
                PlayerHandleObjects[i] = playerObject;
                return;
            }
        }
        PlayerHandleObjects = Arrays.copyOf(PlayerHandleObjects, PlayerHandleObjects.length + 5);
        addPlayerObject(playerObject);
    }

    private static void removePlayerObject(TotemPlayerHandle PlayerObject) {
        for (int i = 0; i < PlayerHandleObjects.length; i++) {
            if(PlayerHandleObjects[i] != null) {
                if (PlayerHandleObjects[i] == PlayerObject) {
                    PlayerHandleObjects[i] = null;
                }
            }
        }
    }

    TotemPlayerHandle(@NotNull Player playerentity) {
        player = playerentity;
        id = playerentity.getUUID();
        DeathPos = player.getLastDeathLocation().isPresent() ? player.getLastDeathLocation().get().pos(): BlockPos.ZERO;
        DeathLevel = (ServerLevel) player.level();
        if(player instanceof ServerPlayer i) {
            Serverplayer = i;
        }
        Serverplayer.setRespawnPosition(new ServerPlayer.RespawnConfig(new LevelData.RespawnData(new GlobalPos(DeathLevel.dimension(), DeathPos),0,0),true),false);
        TotemPlayerHandle.addPlayerObject(this);
    }

    private UUID getUUID() {
        return this.id;
    }

    public void preparePlayer(ServerPlayer newPlayer) {
        if(playerIsRespawned) {return;}
        if(newPlayer.getUUID() == id) {
            playerIsRespawned = true;
            Serverplayer = newPlayer;
            bossbar = new TotemBossbar(Serverplayer);
            createAndSpawnTotemEntity();
            Serverplayer.setGameMode(GameType.ADVENTURE);
            Serverplayer.setInvulnerable(true);
            Serverplayer.setInvisible(true);
        }
    }

    private void setPlayer(ServerPlayer Player) {
        Serverplayer = Player;
    }

    private void createAndSpawnTotemEntity() {
        ItemStack stack = new ItemStack(Items.TOTEM_OF_UNDYING);
        entity = new TotemEntity(Serverplayer.level(), player.getX(), player.getY(), player.getZ(), stack);
        entity.setNeverPickUp();
        entity.setNoGravity(true);
        entity.setUnlimitedLifetime();
        entity.setGlowingTag(true);
        entity.setInvulnerable(true);
        entity.canUsePortal(false);
        Serverplayer.level().addFreshEntity(entity);
    }

    public void tick(MinecraftServer server) {
        if (Serverplayer == null || entity == null) {return;}
        if(!playerIsRespawned) {return;}
        Serverplayer.setInvisible(true);
        entity.setPortalCooldown(20000);
        Vec3 targetPos = Serverplayer.position();
        entity.teleportTo(Serverplayer.level(), targetPos.x, targetPos.y, targetPos.z,  EnumSet.noneOf(Relative.class) , Serverplayer.getYRot(), Serverplayer.getXRot(), false);
        entity.needsSync = true;
        PreventPlayerFromFallingIntoTheVoid();
        CheckForRespawnConditions();
    }

    public void OnDimensionChange(@NonNull ServerLevel Level) {
        entity.setRemoved(Entity.RemovalReason.DISCARDED);
        Serverplayer = (ServerPlayer) Level.getPlayerByUUID(id);
        createAndSpawnTotemEntity();
    }

    private void CheckForRespawnConditions() {
        BlockPos blockOn = Serverplayer.getBlockPosBelowThatAffectsMyMovement();
        Level level = Serverplayer.level();
        if(level.getBlockState(blockOn).getBlock().getDescriptionId().equals("block.totem-on-death.respawner")) {
               bossbar.setProgress(1f);
               bossbar.removeBossbar();
               entity.setRemoved(Entity.RemovalReason.DISCARDED);
               TotemPlayerHandle.removePlayerObject(this);
               Serverplayer.setGameMode(GameType.DEFAULT_MODE);
               Serverplayer.setInvulnerable(false);
               Serverplayer.setInvisible(false);
        }
    }

    private void PreventPlayerFromFallingIntoTheVoid() {
        if(Serverplayer.level().dimension().identifier().toString().equals("minecraft:the_end")) {
            if(Serverplayer.getY() <= 10 && !isInVoid) {
                isInVoid = true;
                if(Serverplayer.getDeltaMovement().y < 0) {
                    Serverplayer.setDeltaMovement(Serverplayer.getDeltaMovement().multiply(1, -1.9, 1));
                    Serverplayer.hurtMarked = true;
                    Serverplayer.needsSync = true;
                } else if (Serverplayer.getDeltaMovement().y == 0) {
                    Serverplayer.setDeltaMovement(Serverplayer.getDeltaMovement().add(0, 40,0));
                }
                System.out.println("dont jump!!!!");
            } else {
                isInVoid = false;
            }
        }
    }
}