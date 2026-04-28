package ohne.name.GUI;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class CustomChestGui extends AbstractContainerScreen<CustomChest> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("totem-on-death","textures/gui/container/custom_chest.png");
    private final int containerRows;

    public CustomChestGui(CustomChest handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.containerRows = handler.getRowCount();
        this.inventoryLabelY = this.imageHeight - 94;
    }

    protected void renderBg(GuiGraphics guiGraphics, float f, int i, int j) {
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, k, l, 0.0F, 0.0F, this.imageWidth, this.containerRows * 18 + 17, 256, 256);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, k, l + this.containerRows * 18 + 17, 0.0F, 126.0F, this.imageWidth, 96, 256, 256);
    }

    @Override
    protected void renderLabels(final GuiGraphics graphics, final int xm, final int ym) {
        graphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, -12566464, false);
    }


    protected void renderSlot(final GuiGraphics graphics, final Slot slot, final int mouseX, final int mouseY) {
        if (this.souldRenderSlot(slot)) {
            return;
        }
        super.renderSlot(graphics, slot, mouseX, mouseY);
    }
    protected void renderTooltip(final GuiGraphics graphics, final int mouseX, final int mouseY) {
        if(this.hoveredSlot != null) {
            if(this.souldRenderSlot(this.hoveredSlot)) {
                return;
            }
        }

        super.renderTooltip(graphics, mouseX, mouseY);
    }

    private boolean souldRenderSlot(final Slot slot) {
        int SlotId = slot.getContainerSlot();
        return SlotId == 42 || SlotId == 41;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}