package ohne.name;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.player.Inventory;
import ohne.name.GUI.CustomChest;
import ohne.name.GUI.CustomChestMenuProvider;

import java.awt.*;

public class RespawnHandle {
    ServerPlayer Player;
    ServerPlayer newPlayer;

    public RespawnHandle(ServerPlayer player, ServerPlayer newPlayer) {
        this.Player = player;
        this.newPlayer = newPlayer;

        if (!TotemPlayerHandle.IsTotemDead(player)) {
            newPlayer.destroyVanishingCursedItems();
            newPlayer.openMenu(new CustomChestMenuProvider(newPlayer.getStats().getValue(Stats.CUSTOM.get(Stats.DEATHS))));
            if (newPlayer.containerMenu instanceof CustomChest menu) {
                menu.SetRespawnHandle(this);
            }
            newPlayer.getInventory().replaceWith(new Inventory(newPlayer, new EntityEquipment()));
        } else if(TotemPlayerHandle.IsTotemDead(player)) {
            TotemPlayerHandle.getPlayerHandle(player).setInventory(player.getInventory());
        }
    }

    public void dropItems(Container container) {
        //container.get
    }
}
