package sertyo.events.module.impl.util.autobuyhw.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.client.MainWindow;
import sertyo.events.utility.font.Fonts;

public class PriceSettingMenu extends Screen {
    private final MainWindow mainWindow = Minecraft.getInstance().getMainWindow();
    private final ItemPrice itemPrice;
    private final TextFieldWidget priceField;
    private final String name;

    public PriceSettingMenu(ItemPrice itemPrice, String name) {
        super(new StringTextComponent("Price Setting Menu"));
        this.itemPrice = itemPrice;
        this.name = name;

        this.priceField = new TextFieldWidget(Minecraft.getInstance().fontRenderer, 10, 10, 100, 20, new StringTextComponent(""));
        this.priceField.setText(Integer.toString((int)ItemPrice.getPrice(name)));
        this.priceField.setMaxStringLength(9);
    }

    @Override
    protected void init() {
        super.init();

        int screenWidth = this.mainWindow.getScaledWidth();
        int screenHeight = this.mainWindow.getScaledHeight();

        int buttonWidth = 100;
        int buttonHeight = 20;

        int fieldX = (screenWidth - this.priceField.getWidth()) / 2;
        int fieldY = (screenHeight - this.priceField.getHeight()) / 2 - 20;
        int buttonX = (screenWidth - buttonWidth) / 2;
        int buttonY = fieldY + this.priceField.getHeight() + 10;

        this.priceField.x = fieldX;
        this.priceField.y = fieldY;

        this.addButton(this.priceField);

        this.addButton(new Button(buttonX, buttonY, buttonWidth, buttonHeight, new StringTextComponent("Done"), button -> {
            try {
                ItemPrice.loadPrices();
                int newPrice = Integer.parseInt(this.priceField.getText());
                ItemPrice.setPrice(this.name, newPrice);
                ItemPrice.savePrices();
            } catch (NumberFormatException ignored) {}
            this.onClose();
            Minecraft.getInstance().displayGuiScreen(new AutoBuyUI());
        }));
    }

    @Override
    public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(mStack);
        super.render(mStack, mouseX, mouseY, partialTicks);

        int screenWidth = this.mainWindow.getScaledWidth();
        int screenHeight = this.mainWindow.getScaledHeight();
        String text = "Введите цену";

        int textWidth = Minecraft.getInstance().fontRenderer.getStringWidth(text);
        int textX = (screenWidth - textWidth) / 2;
        int textY = (screenHeight - 9) / 2 - 30;

        Fonts.msBold[15].drawString(text, textX, textY, Color.WHITE.getRGB());
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
