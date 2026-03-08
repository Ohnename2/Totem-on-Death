package ohne.name;

import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.criterion.SpearMobsTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.ai.behavior.SpearRetreat;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.shapes.EntityCollisionContext;

import java.awt.event.ItemEvent;
import java.util.Arrays;


public class TotemEventHandle {

    TotemEventHandle() {
        ServerLivingEntityEvents.AFTER_DEATH.register((entity, source) -> {
            if (entity instanceof ServerPlayer ServerPlayer) {
                if(!TotemPlayerHandle.IsTotemDead(ServerPlayer)) {
                    new TotemPlayerHandle(ServerPlayer);
                }
            }
        });

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            for (TotemPlayerHandle playerHandleObject : TotemPlayerHandle.getPlayerHandleObjects()) {
                if(playerHandleObject == null) {continue;}
                playerHandleObject.tick(server);
            }
        });

        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            for (TotemPlayerHandle playerHandleObject : TotemPlayerHandle.getPlayerHandleObjects()) {
                if(playerHandleObject == null) {continue;}
                playerHandleObject.preparePlayer(newPlayer);
            }
        });

        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(((serverPlayer, origin, destination) -> {
            for (TotemPlayerHandle playerHandleObject : TotemPlayerHandle.getPlayerHandleObjects()) {
                if(playerHandleObject == null) {continue;}
                if (playerHandleObject.Serverplayer == serverPlayer) {
                    playerHandleObject.OnDimensionChange(destination);
                }
            }
        }));

        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> FailInteractionWhenDead(player));
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> FailInteractionWhenDead(player));
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> FailInteractionWhenDead(player));
        UseItemCallback.EVENT.register((player, world, hand) -> FailInteractionWhenDead(player));
    }

    private InteractionResult FailInteractionWhenDead(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            if (TotemPlayerHandle.IsTotemDead(serverPlayer)) {
                return InteractionResult.FAIL;
            }
        }
        return InteractionResult.PASS;
    }

}
