package therealpant.thaumicattempts.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import therealpant.thaumicattempts.golemnet.container.ContainerResourceRequester;
import therealpant.thaumicattempts.golemnet.tile.TileResourceRequester;

public class GuiResourceRequester extends GuiContainer {

    private static final int PANEL_BORDER_COLOR = 0xFF2F2B27;
    private static final int PANEL_INNER_COLOR = 0xFF131214;
    private static final int SLOT_BORDER_COLOR = 0xFF6F675E;
    private static final int SLOT_FILL_COLOR = 0xFF0B0A0C;

    private final TileResourceRequester tile;

    public GuiResourceRequester(InventoryPlayer playerInv, TileResourceRequester tile) {
        super(new ContainerResourceRequester(playerInv, tile));
        this.tile = tile;
        this.xSize = 194;
        this.ySize = 230;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String title = "Resource Requester";
        if (tile != null && tile.getWorld() != null) {
            title = tile.getWorld().getBlockState(tile.getPos()).getBlock().getLocalizedName();
        }
        this.fontRenderer.drawString(title, 8, 6, 0xE0E0E0);
        this.fontRenderer.drawString(I18n.format("thaumicattempts.gui.resource_requester.patterns"), 8, 20, 0xC8C8C8);
        this.fontRenderer.drawString(I18n.format("thaumicattempts.gui.resource_requester.buffer"), 8, 86, 0xC8C8C8);
        this.fontRenderer.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 0xC8C8C8);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        int left = this.guiLeft;
        int top = this.guiTop;

        drawPanel(left + ContainerResourceRequester.PATTERN_LEFT,
                top + ContainerResourceRequester.PATTERN_TOP,
                ContainerResourceRequester.PATTERN_COLS,
                ContainerResourceRequester.PATTERN_ROWS);

        drawPanel(left + ContainerResourceRequester.BUFFER_LEFT,
                top + ContainerResourceRequester.BUFFER_TOP,
                ContainerResourceRequester.BUFFER_COLS,
                ContainerResourceRequester.BUFFER_ROWS);

        drawPanel(left + ContainerResourceRequester.PLAYER_INV_LEFT,
                top + ContainerResourceRequester.PLAYER_INV_TOP,
                9, 3);

        drawPanel(left + ContainerResourceRequester.PLAYER_INV_LEFT,
                top + ContainerResourceRequester.HOTBAR_TOP,
                9, 1);
    }

    private void drawPanel(int left, int top, int cols, int rows) {
        int width = cols * ContainerResourceRequester.CELL;
        int height = rows * ContainerResourceRequester.CELL;

        Gui.drawRect(left - 4, top - 4, left + width + 4, top + height + 4, PANEL_BORDER_COLOR);
        Gui.drawRect(left - 3, top - 3, left + width + 3, top + height + 3, 0xFF3F3934);
        Gui.drawRect(left - 2, top - 2, left + width + 2, top + height + 2, 0xFF544E47);
        Gui.drawRect(left - 1, top - 1, left + width + 1, top + height + 1, PANEL_BORDER_COLOR);
        Gui.drawRect(left, top, left + width, top + height, PANEL_INNER_COLOR);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int slotX = left + col * ContainerResourceRequester.CELL;
                int slotY = top + row * ContainerResourceRequester.CELL;
                Gui.drawRect(slotX, slotY, slotX + 16, slotY + 16, SLOT_BORDER_COLOR);
                Gui.drawRect(slotX + 1, slotY + 1, slotX + 15, slotY + 15, SLOT_FILL_COLOR);
            }
        }
    }

}