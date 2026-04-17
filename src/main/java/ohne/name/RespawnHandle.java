package ohne.name;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.player.Inventory;
import ohne.name.GUI.CustomChestMenuProvider;

import java.awt.*;

public class RespawnHandle {
    public static void dropInventory(ServerPlayer player, ServerPlayer newPlayer) {
        if (!TotemPlayerHandle.IsTotemDead(player)) {
            player.destroyVanishingCursedItems();
            newPlayer.openMenu(new CustomChestMenuProvider(30));
        } else if(TotemPlayerHandle.IsTotemDead(player)) {
            TotemPlayerHandle.getPlayerHandle(player).setInventory(player.getInventory());
        }
    }
}
