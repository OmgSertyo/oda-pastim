package sertyo.events.ui.roulete;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import sertyo.events.module.impl.render.Arraylist;
import sertyo.events.ui.menu.widgets.CustomButton;
import sertyo.events.utility.Utility;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.RenderUtility;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouletteScreen extends Screen implements Utility {
    private List<Item> items;
    private String won;
    private int selectedItemIndex = -1;
    private long startTime = -1;
    private boolean spinning = false;
    private float offset = 0;
    private long lastUpdateTime = 0;

    private float spinSpeed = 0.2f; // Скорость прокрутки
    public RouletteScreen() {
        super(new StringTextComponent("Roulette"));
        items = new ArrayList<>();
        items.add(new Item("Sub 1 day", 30,new Color(-1)));  // 30% шанс
        items.add(new Item("Sub month", 15, new Color(-1)));  // 50% шанс
        items.add(new Item("Sub forever", 5, new Color(0xAF18AF)));  // 20% шанс
        items.add(new Item("Nothing", 60, new Color(0xAF18AF)));  // 20% шанс
    }

    @Override
    protected void init() {

        this.addButton(new CustomButton(this.width / 2 - 50, this.height -40, 100, 20, new StringTextComponent("Spin"), p_onPress_1_ -> {
            if (!spinning) {
                spinning = true;
                startTime = System.currentTimeMillis();
            }
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderUtil.Render2D.drawRect(0.0F, 0.0F, (float)sr.getScaledWidth(), (float)sr.getScaledHeight(), (new Color(20, 20, 20)).getRGB());

        super.render(matrixStack, mouseX, mouseY, partialTicks);
        RenderUtil.Render2D.drawRoundedRect(this.width / 2 - 140, 200, 300, 150, 5.0F, (new Color(30, 30, 30)).getRGB());
        if (won != null)
            Fonts.msBold[15].drawString(matrixStack, ColorUtil.gradient(String.format("You won %s", won), Arraylist.getColor(0).getRGB(), Arraylist.getColor(90).getRGB()), this.width / 2 - 50, 370, -1);

        if (!spinning) {
            int x = 30;
            for (Item item : items) {
              //  RenderUtility.drawRoundedRect(this.width / 2 - Fonts.msBold[20].getWidth(item.getName()) / 2 + x - 2, 94, Fonts.msBold[19].getWidth(item.getName()) + 5, 30,5, item.getColor());

                Fonts.msBold[15].drawString(matrixStack, item.getName(), this.width / 2 - 130 - Fonts.msBold[15].getWidth(item.getName()) + x + 50, 265, -1);
                x += 60;
            }

        } else {
            // Анимация прокрутки
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastUpdateTime > 50) { // Обновляем каждую 50 миллисекунд
                offset += spinSpeed;
                if (offset > items.size()) {
                    offset = 0; // Сбросить прокрутку, если вышли за пределы списка
                }
                lastUpdateTime = currentTime; // Обновляем время
            }

            // Рисуем анимацию прокрутки
            int x = 30;
            for (int i = 0; i < items.size(); i++) {
                int index = (int) ((i + offset) % items.size());
                String itemName = items.get(index).getName();
                Fonts.msBold[15].drawString(matrixStack, itemName, this.width / 2 - 130 - Fonts.msBold[15].getWidth(itemName) + x + 50, 265, -1);

                x += 60;
            }

            // Проверка, если прошло 10 секунд
            if (System.currentTimeMillis() - startTime > 10000) {
                spinning = false;
                selectRandomItem(matrixStack);
            }
        }
    }

    private void selectRandomItem(MatrixStack ms) {
        int totalChance = items.stream().mapToInt(Item::getChance).sum();
        int randomValue = new Random().nextInt(totalChance) + 1;

        int cumulativeChance = 0;
        for (Item item : items) {
            cumulativeChance += item.getChance();
            if (randomValue <= cumulativeChance) {
                selectedItemIndex = items.indexOf(item);
                break;
            }
        }

        // Покажем выбранный товар
        showSelectedItem(ms);
    }

    private void showSelectedItem(MatrixStack ms) {
        if (selectedItemIndex != -1) {
            String selectedItem = items.get(selectedItemIndex).getName();
            System.out.println(selectedItem);
            won = selectedItem;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false; // Отключаем паузу при открытии экрана
    }

    public static class Item {
        @Getter
        @Setter
        private String name;
        @Getter
        @Setter
        private int chance;
        @Getter
        @Setter
        private Color color;

        public Item(String name, int chance, Color color) {
            this.name = name;
            this.chance = chance;
            this.color = color;
        }


    }
}