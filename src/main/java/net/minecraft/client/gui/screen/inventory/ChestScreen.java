package net.minecraft.client.gui.screen.inventory;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.Main;
import sertyo.events.utility.Utility;
import sertyo.events.ui.ab.AutoBuy;

public class ChestScreen extends ContainerScreen<ChestContainer> implements IHasContainer<ChestContainer>, Utility {
    /**
     * The ResourceLocation containing the chest GUI texture.
     */
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");

    /**
     * Window height is calculated with these values; the more rows, the higher
     */
    private final int inventoryRows;
    private final ITextComponent title;

    public ChestScreen(ChestContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.title = title;
        this.passEvents = false;
        int i = 222;
        int j = 114;
        this.inventoryRows = container.getNumRows();
        this.ySize = 114 + this.inventoryRows * 18;
        this.playerInventoryTitleY = this.ySize - 94;
    }

    Button button;

    @Override
    protected void init() {
        super.init();
        boolean ah = title.getString().contains("Аукцион");
        boolean accept = title.getString().contains("Покупка предмета");

        if (!Main.unhooked) {
            if (ah || accept) {
                if (AutoBuy.enabled) {
                    this.addButton(button = new Button(width / 2 - 50, height / 2 - 150, 100, 20, new StringTextComponent(TextFormatting.GREEN + "AutoBuy: ON"), (button) -> {
                        AutoBuy.enabled = false;
                    }));
                } else {
                this.addButton(button = new Button(width / 2 - 50, height / 2 - 150, 100, 20, new StringTextComponent(TextFormatting.RED + "AutoBuy: OFF"), (button) -> {
                        AutoBuy.enabled = true;
                }));
                }
            }
        }
    }

    @Override
    public void closeScreen() {
                AutoBuy.enabled = false;

        super.closeScreen();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (button != null) {
            if (AutoBuy.enabled) {
                button.setMessage(new StringTextComponent(TextFormatting.GREEN + "AutoBuy: ON"));
            }
            if (!AutoBuy.enabled) {
                button.setMessage(new StringTextComponent(TextFormatting.RED + "AutoBuy: OFF"));
            }
        }
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.blit(matrixStack, i, j + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}
