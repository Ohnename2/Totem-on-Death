package ohne.name;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.gamerules.GameRules;
import ohne.name.GUI.CustomChest;
import ohne.name.GUI.CustomChestMenuProvider;
import ohne.name.GUI.TotemCustomMenuType;
import ohne.name.networking.ItemRemoveAmount;

public class RespawnHandle {
    ServerPlayer Player;
    ServerPlayer newPlayer;

    public RespawnHandle(ServerPlayer player, ServerPlayer newPlayer) {
        this.Player = player;
        this.newPlayer = newPlayer;
        newPlayer.getInventory().replaceWith(player.getInventory());
        if (!TotemPlayerHandle.IsTotemDead(player) && !player.level().getGameRules().get(GameRules.KEEP_INVENTORY)) {
            newPlayer.destroyVanishingCursedItems();
            int itemDeathCount = PlayerItemRespawnCount.get(newPlayer.getUUID());
            newPlayer.openMenu(new CustomChestMenuProvider(itemDeathCount));
            if (newPlayer.containerMenu instanceof CustomChest menu) {
                menu.SetRespawnHandle(this);
            }
            PlayerItemRespawnCount.set(newPlayer.getUUID(), itemDeathCount + 1);
            ItemRemoveAmount payload = new ItemRemoveAmount(itemDeathCount + 1);
            ServerPlayNetworking.send(newPlayer, payload);
            newPlayer.getInventory().replaceWith(new Inventory(newPlayer, new EntityEquipment()));

        }
    }

    public void dropItems(Container container) {
        Inventory newInventory = new Inventory(Player, new EntityEquipment());
        for (int i = 0; i < newInventory.getContainerSize(); i++) {
            if(!container.getItem(i).getOrDefault(TotemCustomMenuType.IS_FROM_CUSTOM_CHEST, false)) {
                newInventory.setItem(i, container.getItem(i));
            }
        }
        newPlayer.getInventory().replaceWith(newInventory);
        newPlayer.closeContainer();
    }
}
