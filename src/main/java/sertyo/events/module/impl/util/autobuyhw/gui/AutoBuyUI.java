package sertyo.events.module.impl.util.autobuyhw.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.MainWindow;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import org.apache.commons.lang3.tuple.Triple;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.font.Fonts;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AutoBuyUI extends Screen {
    private final MainWindow window = Minecraft.getInstance().getMainWindow();
    private float centerX = 0.0F;
    private float centerY = 0.0F;
    private int buttonsPerRow = 30;
    private int buttonWidth = 20;
    private int buttonHeight = 20;
    private int buttonSpacing = 5;
    private final List<CustomButton> buttons = new ArrayList<>();

    public AutoBuyUI() {
        super(new StringTextComponent("AutoBuy UI"));
    }

    @Override
    protected void init() {
        super.init();
        this.buttonSpacing = 2;
        this.buttonsPerRow = 10;
        this.centerX = this.window.getScaledWidth() / 2.0F;
        this.centerY = this.window.getScaledHeight() / 2.0F;
        this.buttons.clear();
        int startX = (int) (this.centerX - ((this.buttonWidth + this.buttonSpacing) * this.buttonsPerRow / 2));
        int startY = (int) (this.centerY - ((this.buttonHeight + this.buttonSpacing) * this.buttons.size() / this.buttonsPerRow / 2)) - 45;

        for (Triple<String, String, String> buttonData : getButtonLabels()) {
            String buttonLabel = buttonData.getLeft();
            String skin = buttonData.getMiddle();
            String additionalName = buttonData.getRight();

            Item item = Registry.ITEM.getOrDefault(new ResourceLocation(skin));
            int x = startX + this.buttons.size() % this.buttonsPerRow * (this.buttonWidth + this.buttonSpacing);
            int y = startY + this.buttons.size() / this.buttonsPerRow * (this.buttonHeight + this.buttonSpacing);
            ItemPrice itemPrice = new ItemPrice();
            CustomButton button = new CustomButton(x, y, this.buttonWidth, this.buttonHeight, buttonLabel, item, additionalName, b -> onButtonPressed(itemPrice, buttonLabel));
            this.buttons.add(button);
            this.addButton(button);
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        int width = 230;
        int height = 115;
        float renderX = this.centerX - (width / 2);
        float renderY = this.centerY - (height / 2);

        // Draw UI background
        RenderUtil.Render2D.drawRoundedRect(renderX, (renderY + height + 7.0F), (float) width, (float) height, 5.0F, new Color(30, 30, 30).getRGB());
        for (CustomButton button : this.buttons) {
            button.render(matrixStack, mouseX, mouseY, partialTicks);
        }

        for (CustomButton button : this.buttons) {
            if (button.isHovered()) {
                String additionalName = button.getAdditionalName();
                RenderUtil.Render2D.drawRoundedRect(renderX, (renderY - 9.0F), (Fonts.msBold[15].getWidth(additionalName) + 5.0F), (Fonts.msBold[15].getFontHeight() + 5.0F), 1.0F, new Color(30, 30, 30).getRGB());
                Fonts.msBold[15].drawString(additionalName, (renderX + 2.0F), (renderY - 10.0F), Color.WHITE.getRGB());
                break;
            }
        }

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void onButtonPressed(ItemPrice item, String name) {
        Minecraft.getInstance().displayGuiScreen(new PriceSettingMenu(item, name));
    }

    /*     */   private List<Triple<String, String, String>> getButtonLabels() {
        /* 106 */     List<Triple<String, String, String>> labels = new ArrayList<>();
        /* 107 */     labels.add(Triple.of("shlemetenity", "minecraft:netherite_helmet", "Шлем Этернити"));
        /* 108 */     labels.add(Triple.of("booteternity", "minecraft:netherite_boots", "Ботинки Этернити"));
        /* 109 */     labels.add(Triple.of("yniversalka", "minecraft:tripwire_hook", "Универсальный ключ"));
        /* 110 */     labels.add(Triple.of("armorelytra", "minecraft:elytra", "Броневая элитра"));
        /* 111 */     labels.add(Triple.of("topsfera", "minecraft:glowstone", "Сфера Урон 2, Скорость 2, Броня 2"));
        /* 112 */     labels.add(Triple.of("spawner", "minecraft:spawner", "Спавнер"));
        /* 113 */     labels.add(Triple.of("opit50", "minecraft:experience_bottle", "Бутылка опыта 50 лвл"));
        /* 114 */     labels.add(Triple.of("opit100", "minecraft:experience_bottle", "Бутылка опыта 100 лвл"));
        /* 115 */     labels.add(Triple.of("talicSatiri", "minecraft:totem_of_undying", "Талисман Сатиры"));
        /* 116 */     labels.add(Triple.of("mifkasfera", "minecraft:torch", "Мифическая Сфера Броня III, Урон II"));
        /* 117 */     labels.add(Triple.of("otmichkaso", "minecraft:tripwire_hook", "Отмычка Секретная"));
        /* 118 */     labels.add(Triple.of("otmichkobo", "minecraft:tripwire_hook", "Отмычка Уникальная"));
        /* 119 */     labels.add(Triple.of("freokatd", "minecraft:tripwire_hook", "Отмычка Редкая"));
        /* 120 */     labels.add(Triple.of("obicgjsa", "minecraft:tripwire_hook", "Отмычка Обычная"));
        /* 121 */     labels.add(Triple.of("negaelt", "minecraft:elytra", "Неразрушимые Элитры"));
        /* 122 */     labels.add(Triple.of("nekiaods", "minecraft:netherite_pickaxe", "Мечта Шахтёра"));
        /* 123 */     labels.add(Triple.of("molikds", "minecraft:totem_of_undying", "Талик бессмертие II"));
        /* 124 */     labels.add(Triple.of("mekenik", "minecraft:potion", "Справедливость"));
        /* 125 */     labels.add(Triple.of("sunhelmet", "minecraft:golden_helmet", "Шлем Солнца"));
        /*     */
        /* 127 */     labels.add(Triple.of("stan", "minecraft:nether_star", "Стан"));
        /* 128 */     labels.add(Triple.of("battle", "minecraft:prismarine_crystals", "Боевые кристаллы"));
        /* 129 */     labels.add(Triple.of("clay", "minecraft:clay", "Взрывчатое вещество"));
        /* 130 */     labels.add(Triple.of("chesteternity", "minecraft:netherite_chestplate", "Нагрудник Этернити"));
        /* 131 */     labels.add(Triple.of("legginseternirty", "minecraft:netherite_leggings", "Поножи Этернити"));
        /* 132 */     labels.add(Triple.of("swordeternityt", "minecraft:netherite_sword", "Меч Этернити"));
        /* 133 */     labels.add(Triple.of("kirkaeternity", "minecraft:netherite_pickaxe", "Кирка Этернити"));
        /*     */
        /* 135 */     labels.add(Triple.of("z5shlem", "minecraft:netherite_helmet", "Защита 5 шлем"));
        /* 136 */     labels.add(Triple.of("z5grudak", "minecraft:netherite_chestplate", "Защита 5 нагрудник"));
        /* 137 */     labels.add(Triple.of("z5leggins", "minecraft:netherite_leggings", "Защита 5 поножи"));
        /* 138 */     labels.add(Triple.of("z5boots", "minecraft:netherite_boots", "Защита 5 Ботинки"));
        /*     */
        /* 140 */     labels.add(Triple.of("shleminf", "minecraft:netherite_helmet", "Шлем инфинити"));
        /* 141 */     labels.add(Triple.of("grudakinf", "minecraft:netherite_chestplate", "Нагрудник инфинити"));
        /* 142 */     labels.add(Triple.of("legginsinf", "minecraft:netherite_leggings", "Поножи инфинити"));
        /* 143 */     labels.add(Triple.of("bootinf", "minecraft:netherite_boots", "Ботинки инфинити"));
        /*     */
        /* 145 */     labels.add(Triple.of("talicetern", "minecraft:totem_of_undying", "Талисман Этернити"));
        /* 146 */     labels.add(Triple.of("talicstring", "minecraft:totem_of_undying", "Талисман Стрингера"));
        /*     */
        /* 148 */     labels.add(Triple.of("sferahui", "minecraft:glowstone", "Сфера урон 3"));
        /* 149 */     labels.add(Triple.of("sferaspeed", "minecraft:glowstone", "Сфера скорость 3"));
        /* 150 */     labels.add(Triple.of("sferka", "minecraft:torch", "Сфера урон 3, броня 2"));
        /* 151 */     labels.add(Triple.of("talick", "minecraft:totem_of_undying", "Талисман Инфинити"));
        /* 152 */     labels.add(Triple.of("sferadam", "minecraft:glowstone", "Сфера урон 2 броня 2"));
        /* 153 */     labels.add(Triple.of("sped", "minecraft:glowstone", "Сфера скорость 2 урон 2"));
        /* 154 */     labels.add(Triple.of("spedi", "minecraft:glowstone", "Сфера скорость 1 урон 2 броня 2"));
        /*     */
        /* 156 */     labels.add(Triple.of("sferkaflesh", "minecraft:red_bed", "Сфера Флеша"));
        /* 157 */     labels.add(Triple.of("sferkaarmor", "minecraft:yellow_bed", "Сфера арморталити"));
        /* 158 */     labels.add(Triple.of("sferkacerbe", "minecraft:green_bed", "Сфера Цербера"));
        /* 159 */     labels.add(Triple.of("farmert", "minecraft:netherite_sword", "Фармер 2"));
        /* 160 */     labels.add(Triple.of("farmerg", "minecraft:netherite_sword", "Фармер 3-10"));
        /*     */
        /* 162 */     return labels;
        /*     */   }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CustomButton b : this.buttons) {
            if (b.isHovered()) {
                return b.mouseClicked(mouseX, mouseY, button);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC key
            this.minecraft.player.closeScreen();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public static boolean hovered(double mouseX, double mouseY, double x, double y, double width, double height) {
        return (mouseX > x && mouseY > y && mouseX < x + width && mouseY < y + height);
    }
}