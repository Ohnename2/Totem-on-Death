package ohne.name.GUI;

import net.fabricmc.fabric.api.menu.v1.ExtendedMenuProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jspecify.annotations.Nullable;

public class CustomChestMenuProvider implements ExtendedMenuProvider<Integer> {

    private final int DeathCount;

    public CustomChestMenuProvider(int deathCount) {
        DeathCount = deathCount;
    }

    @Override
    public Integer getScreenOpeningData(ServerPlayer player) {
        return DeathCount;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("ui.totem-on-death.custom.chest", DeathCount);
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new CustomChest(containerId, inventory, DeathCount);
    }
}
