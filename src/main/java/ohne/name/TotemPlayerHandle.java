package ohne.name;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.Vec3;
import ohne.name.networking.Status;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.UUID;
import static ohne.name.TotemEventHandle.SERVER;

public class TotemPlayerHandle {
    static TotemPlayerHandle[] PlayerHandleObjects = new TotemPlayerHandle[0]; //Saved

    final static int NeededTimeOnRespawner = 100;

    final static Long TimeToRespawn = TotemConfigHandle.CONFIG.TimeToRespawnInSeconds;


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

    public static boolean IsTotemDead(@NonNull ServerPlayer player) {
        UUID playeruuid = player.getUUID();
        for (TotemPlayerHandle PlayerHandleObject : PlayerHandleObjects) {
            if(PlayerHandleObject == null) {continue;}
            UUID currentObjectUuid = PlayerHandleObject.getUUID();
            if(currentObjectUuid == null) {continue;}
            if (currentObjectUuid.compareTo(playeruuid) == 0) {
                return true;
            }
        }
        return false;
    }

    public static @Nullable TotemPlayerHandle getPlayerHandle(@NonNull ServerPlayer player) {
        UUID playeruuid = player.getUUID();
        for (TotemPlayerHandle PlayerHandleObject : PlayerHandleObjects) {
            if (PlayerHandleObject == null) {continue;}
            UUID currentObjectUuid = PlayerHandleObject.getUUID();
            if (currentObjectUuid == null) {continue;}
            if (currentObjectUuid.compareTo(playeruuid) == 0) {
                return PlayerHandleObject;
            }
        }
        return null;
    }

    public static void addPlayerObject(TotemPlayerHandle playerObject) {
        for (int i = 0; i < PlayerHandleObjects.length; i++) {
            if (PlayerHandleObjects[i] == null) {
                PlayerHandleObjects[i] = playerObject;
                TotemPlayerHandle.SendPlayerUpdateToAll(SERVER);
                return;
            }
        }
        PlayerHandleObjects = Arrays.copyOf(PlayerHandleObjects, PlayerHandleObjects.length + 5);
        addPlayerObject(playerObject);
    }

    public static void removePlayerObject(TotemPlayerHandle PlayerObject) {
        for (int i = 0; i < PlayerHandleObjects.length; i++) {
            if(PlayerHandleObjects[i] == null) {continue;}
            if (PlayerHandleObjects[i] == PlayerObject) {
                PlayerHandleObjects[i] = null;
                TotemPlayerHandle.SendPlayerUpdateToAll(SERVER);
            }
        }
    }

    public static UUID[] getArrayOfDeadPlayers() {
        UUID[] returnArray = new UUID[PlayerHandleObjects.length];
        for (int i = 0; i < PlayerHandleObjects.length; i++) {
            if(PlayerHandleObjects[i] == null) {
                returnArray[i] = new UUID(0L, 0L);
                continue;
            }
            returnArray[i] = PlayerHandleObjects[i].getUUID();
        }
        return returnArray;
    }

    public static void SendPlayerUpdateToAll(MinecraftServer server) {
        if(server == null) {return;}
        Status payload = new Status(Arrays.asList(getArrayOfDeadPlayers()), !TotemEventHandle.AlwaysRespawn);
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(player, payload);
        }
    }

    public static void SendPlayerUpdate(ServerPlayer player) {
        if(player == null) {return;}
        Status payload = new Status(Arrays.asList(getArrayOfDeadPlayers()), !TotemEventHandle.AlwaysRespawn);
        ServerPlayNetworking.send(player, payload);
    }


//Class Specific

    UUID id;
    ServerPlayer Serverplayer;
    TotemEntity entity;
    boolean isInVoid = false;
    TotemBossbar bossbar;
    int TicksOnRespawner;
    Long RespawnTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC) + TimeToRespawn; // Saved
    Long TimeNow = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
    int TickCounter;
    boolean removed = false;
    boolean IsSaved = false;
    boolean hasTicked = false;
    public ServerPlayer.RespawnConfig defaultRespawnPos = null;
    ServerPlayer oldPlayer = null;
    Inventory oldInventory = null;

    public TotemPlayerHandle(@NotNull ServerPlayer playerentity) {
        id = playerentity.getUUID();
        oldPlayer = playerentity;
        defaultRespawnPos = playerentity.getRespawnConfig();
        BlockPos deathPos = playerentity.getLastDeathLocation().isPresent() ? playerentity.getLastDeathLocation().get().pos(): BlockPos.ZERO;
        ServerLevel deathLevel = playerentity.level();
        SetRespawnPos(playerentity,deathPos, deathLevel);
        TotemPlayerHandle.addPlayerObject(this);
    }

    public TotemPlayerHandle(@NotNull ServerPlayer playerentity, Long respawnTime) {
        if(respawnTime != -1) {this.RespawnTime = respawnTime;}
        id = playerentity.getUUID();
        TotemPlayerHandle.addPlayerObject(this);
    }

    private void SetRespawnPos(ServerPlayer player, BlockPos deathPos, ServerLevel deathlevel) {
        if(deathlevel.dimension().toString().equals(ServerLevel.END.toString()) && deathPos.getY() <= 0) {
            player.setRespawnPosition(new ServerPlayer.RespawnConfig(new LevelData.RespawnData(new GlobalPos(deathlevel.dimension(), deathPos.atY(1)),0,0),true),false);
            return;
        }
        player.setRespawnPosition(new ServerPlayer.RespawnConfig(new LevelData.RespawnData(new GlobalPos(deathlevel.dimension(), deathPos),0,0),true),false);
    }

    private UUID getUUID() {
        return this.id;
    }

    public void preparePlayer(MinecraftServer server) {
        for(ServerLevel Level : server.getAllLevels()) {
            if(Level.getPlayerByUUID(id) != null) {
                Serverplayer = (ServerPlayer) Level.getPlayerByUUID(id);
                assert Serverplayer != null;
                Serverplayer.setGameMode(GameType.ADVENTURE);
                Serverplayer.setInvulnerable(true);
                Serverplayer.setInvisible(true);
                break;
            }
        }
    }

    public void prepareBossbar(ServerPlayer player) {
        bossbar = new TotemBossbar(Serverplayer);
    }

    private void prepareTotemEntity() {
        if(entity != null) {entity.setRemoved(Entity.RemovalReason.DISCARDED);}
        ItemStack stack = new ItemStack(Items.TOTEM_OF_UNDYING);
        entity = new TotemEntity(Serverplayer.level(), Serverplayer.getX(), Serverplayer.getY(), Serverplayer.getZ(), stack, Serverplayer.getName());
        Serverplayer.level().addFreshEntity(entity);
        entity.setDeltaMovement(Vec3.ZERO);
    }

    public boolean shouldTickHandle(MinecraftServer server) {
        if(Serverplayer == null|| Serverplayer.isRemoved()) {
            preparePlayer(server);
            return false;
        }
        if(bossbar == null) {
            prepareBossbar(Serverplayer);
            return false;
        }
        if(entity == null || entity.isRemoved()) {
            prepareTotemEntity();
            return false;
        }
        return true;
    }

    public void tick(MinecraftServer server) {
        if(this.RemoveOnSave()) {return;}
        if(!this.shouldTickHandle(server)) {return;}

        if(Serverplayer.isAlive()) {
            FirstTick();
            Serverplayer.setInvisible(true);
            entity.totemTick(Serverplayer.level(), Serverplayer.position(), Serverplayer.getYRot(), Serverplayer.getXRot());
            PreventPlayerFromFallingIntoTheVoid();
            CheckForRespawnConditions();
            bossbar.tick();
        }
    }

    private void FirstTick() {
        if(hasTicked) {return;}
        Serverplayer.setRespawnPosition(defaultRespawnPos,false);
        if(oldInventory != null) {
            Serverplayer.getInventory().replaceWith(oldInventory);
        }
    }

    private void CheckForRespawnConditions() {
        this.CheckOnRespawner();
        this.RespawnOnTime();
    }

    private void RespawnOnTime() {
        TickCounter++;
        if(TickCounter % 20 == 0) {
            TimeNow++;
            double RemainingTimeSecs = RespawnTime - TimeNow;
            double RemainingTimeMins = RemainingTimeSecs / 60;
            double RemainingTimeHours = RemainingTimeMins / 60;
            double RemainingTimeDays = RemainingTimeHours / 24;
            if(RemainingTimeDays > 1L) {
                bossbar.setRemainingTime(Math.round(RemainingTimeDays), 3);
            } else if(RemainingTimeHours > 1L) {
                bossbar.setRemainingTime(Math.round(RemainingTimeHours), 2);
            } else if(RemainingTimeMins > 1L) {
                bossbar.setRemainingTime(Math.round(RemainingTimeMins), 1);
            } else if(RemainingTimeSecs > 1L) {
                bossbar.setRemainingTime(Math.round(RemainingTimeSecs), 0);
            }
            if(RemainingTimeSecs <= 0L) {
                Respawn();
            }
            bossbar.setProgressTime((float) (((double) TimeToRespawn - RemainingTimeSecs) / (double) TimeToRespawn));
        }
        if(TickCounter % 1200 == 0) {
            TimeNow = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        }
    }

    private void CheckOnRespawner() {
        BlockPos blockOn = Serverplayer.getBlockPosBelowThatAffectsMyMovement();
        ServerLevel level = Serverplayer.level();
        Block block = level.getBlockState(blockOn).getBlock();
        bossbar.showOnRespawner(TicksOnRespawner != 0);
        if(block instanceof TotemRespawner) {
            TicksOnRespawner++;
        } else {
            if(TicksOnRespawner <= 0) {return;}
            TicksOnRespawner--;
        }
        bossbar.setProgressRespawner((float) TicksOnRespawner / NeededTimeOnRespawner);
        if(TicksOnRespawner >= NeededTimeOnRespawner) {
            if(block instanceof TotemRespawner respawner) {
                respawner.decay(level.getBlockState(blockOn), level, blockOn);
                Respawn();
            }
        }
    }

    public void Respawn() {
        this.RemoveObjects();
        bossbar.removeBossbar();
        Serverplayer.setGameMode(GameType.DEFAULT_MODE);
        Serverplayer.setInvulnerable(false);
        Serverplayer.setInvisible(false);
    }

    public void RemoveObjects() {
        if(entity != null) {entity.setRemoved(Entity.RemovalReason.DISCARDED);}
        TotemPlayerHandle.removePlayerObject(this);
    }

    public boolean RemoveOnSave() {
        if(IsRemoved()) {
            if(IsSaved) {
                this.RemoveObjects();
            }
            return true;
        }
        return false;
    }

    private void PreventPlayerFromFallingIntoTheVoid() {
        if(Serverplayer.level().dimension().identifier().toString().equals("minecraft:the_end")) {
            if(Serverplayer.getY() <= 1 && !isInVoid) {
                isInVoid = true;
                if(Serverplayer.getDeltaMovement().y < 0) {
                    Serverplayer.setDeltaMovement(Serverplayer.getDeltaMovement().multiply(1, -1.9, 1));
                    Serverplayer.hurtMarked = true;
                    Serverplayer.needsSync = true;
                } else if (Serverplayer.getDeltaMovement().y == 0) {
                    Serverplayer.setDeltaMovement(Serverplayer.getDeltaMovement().add(0, 40,0));
                }
            } else {
                isInVoid = false;
            }
        }
    }

    public Long getRespawnTime() {
        return this.RespawnTime;
    }

    public boolean IsRemoved() {
        return this.removed;
    }

    public void setRemoved() {
        this.removed = true;
    }

    public void setSaved() {
        if(this.IsRemoved()) {
            this.IsSaved = true;
        }
    }

    public void setInventory(Inventory inventory) {
        oldInventory = inventory;
    }
}