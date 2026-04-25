package ohne.name.GUI;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityEquipment;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import ohne.name.RespawnHandle;

import java.util.Objects;

public class CustomChest extends ChestMenu {
    private final ItemStack Empty_Item = new ItemStack(Items.LIGHT_GRAY_STAINED_GLASS_PANE);
    private final ItemStack Confirm_Item = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
    private final ItemStack Confirm_Item_Confirmable = new ItemStack(Items.LIME_STAINED_GLASS_PANE);
    private final ItemStack Reset_Item = new ItemStack(Items.RED_STAINED_GLASS_PANE);
    private int exsistingStacks;
    private final int neededStacks;
    private int selectedStacks = 0;
    private Container processedContainer;
    private final Component Reset_Item_Name = Component.literal("Reset");
    private final Component Confirm_Item_Name = Component.literal("Confirm");
    public RespawnHandle RespawnHandle;

    public CustomChest(int containerId, Inventory inventory, int DeathCount) {
        super(TotemCustomMenuType.CUSTOM_TOTEM_CHEST, containerId, inventory, new SimpleContainer(9 * 5), 5);
        exsistingStacks = 0;
        neededStacks = DeathCount;
        this.SetUpChestItem(Empty_Item, Component.literal(""));
        this.SetUpChestItem(Reset_Item, Reset_Item_Name);
        CreateContainer(inventory);
        this.SetUpChestItem(Confirm_Item, Confirm_Item_Name);
        this.SetUpChestItem(Confirm_Item_Confirmable, Confirm_Item_Name);
        this.SaveContainer();
        this.UpdateConfirmButton();
    }

    public void SetRespawnHandle(RespawnHandle RespawnHandle) {
        this.RespawnHandle = RespawnHandle;
    }

    private void SetUpChestItem(ItemStack itemStack, Component component) {
        itemStack.set(DataComponents.CUSTOM_NAME, component);
        itemStack.set(TotemCustomMenuType.IS_FROM_CUSTOM_CHEST, true);
    }

    private void CreateContainer(Inventory inventory) {
        exsistingStacks = 0;
        for (int i = 0; i < this.getContainer().getContainerSize()-2; i++) {
            this.getContainer().setItem(i, Empty_Item);
        }
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (!inventory.getItem(i).isEmpty()) {
                exsistingStacks++;
                this.getContainer().setItem(i, inventory.getItem(i));
            }
        }
        this.getContainer().setItem(this.getContainer().getContainerSize()-1, Confirm_Item);
        this.getContainer().setItem(this.getContainer().getContainerSize()-2, Reset_Item);
        this.getContainer().setChanged();
        this.broadcastChanges();
    }

    private void SaveContainer() {
        Container container = this.getContainer();
        Container container1 = new SimpleContainer(container.getContainerSize());
        for(int i = 0; i < container.getContainerSize(); i++) {
            container1.setItem(i, container.getItem(i));
        }
        this.processedContainer = container1;
    }

    private void LoadContainer() {
        Container container = this.getContainer();
        for(int i = 0; i < processedContainer.getContainerSize(); i++) {
            container.setItem(i, processedContainer.getItem(i));
        }
        selectedStacks = 0;
        this.UpdateConfirmButton();
    }

    @Override
    public void clicked(int slotIndex, int buttonNum, ContainerInput containerInput, Player player) {
        if(slotIndex == -999) {return;}
        ItemStack item = this.getContainer().getItem(slotIndex);
        if (item == Empty_Item) {
            return;
        }
        if (item.getOrDefault(TotemCustomMenuType.IS_FROM_CUSTOM_CHEST, false)) {
            if (Objects.equals(item.get(DataComponents.CUSTOM_NAME), Reset_Item_Name)) {
                this.LoadContainer();
            } else if (Objects.equals(item.get(DataComponents.CUSTOM_NAME), Confirm_Item_Name)) {
                if(this.IsComplete() && player instanceof ServerPlayer) {
                    if(RespawnHandle != null) {
                        RespawnHandle.dropItems(this.getContainer());
                    }
                }
            }
            return;
        }

        if(this.IsComplete()) {
            return;
        }

        ItemStack Selected_Item = new ItemStack(Items.BARRIER);
        Selected_Item.set(DataComponents.CUSTOM_NAME, item.getDisplayName());
        Selected_Item.set(TotemCustomMenuType.IS_FROM_CUSTOM_CHEST, true);
        selectedStacks++;
        this.UpdateConfirmButton();
        this.getContainer().setItem(slotIndex, Selected_Item);
    }

    private void UpdateConfirmButton() {
        if(this.IsComplete()) {
            this.getContainer().setItem(44, Confirm_Item_Confirmable);
        } else {
            this.getContainer().setItem(44, Confirm_Item);
        }

    }

    public boolean IsComplete() {
        return selectedStacks >= neededStacks || exsistingStacks <= neededStacks;
    }

    @Override
    public ItemStack quickMoveStack(final Player player, final int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void addStandardInventorySlots(final Container container, final int left, final int top) {}

    @Override
    public boolean stillValid(Player player) {return true;}
}