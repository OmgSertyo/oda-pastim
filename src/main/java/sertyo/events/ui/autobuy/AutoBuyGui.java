package sertyo.events.ui.autobuy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector4f;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;
import sertyo.events.module.setting.impl.TextSetting;
import sertyo.events.ui.autobuy.impl.ItemManager;
import sertyo.events.ui.autobuy.impl.TestItem;
import sertyo.events.ui.autobuy.impl.renderShit.ScaleUtil;
import sertyo.events.ui.csgui.component.impl.InputObject;
import sertyo.events.ui.menu.main.CVec2;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.render.NColor;
import sertyo.events.utility.render.RenderUtility;
import sertyo.events.utility.render.animation.AnimationMath;

import java.awt.*;
import java.util.stream.Collectors;

import static sertyo.events.utility.Utility.mc;


public class AutoBuyGui extends Screen {
    public AutoBuyGui() {
        super(NarratorChatListener.EMPTY);
        size.update(new CVec2(902, 523).divide(FACTOR));

        if (selected == null) {
            int i = (int) (Math.random() * ItemManager.itemList.size() - 1);
            int z = 0;
            for (TestItem testItem : ItemManager.itemList) {
                if (z == i) {
                    selected = testItem;
                }
                z++;
            }
        }
    }

    private final static float FACTOR = 2F;

    private final static CVec2 position = new CVec2();
    private final static CVec2 size = new CVec2(902, 603).divide(FACTOR);

    public TestItem selected;

    public CVec2 itemListScroll = new CVec2();
    public CVec2 selectedItemListScroll = new CVec2();


    public CVec2 saveAnim = new CVec2();

    public CVec2 loadAnim = new CVec2();

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        MainWindow mw = Minecraft.getInstance().getMainWindow();
        position.update(new CVec2(mw.getScaledWidth() / 2f - size.x() / 2f, mw.getScaledHeight() / 2f - size.y() / 2f));
        RenderUtility.Render2D.drawRoundedRect(position.x, position.y, size.x, size.y, 5, 0xFF14151A);


        { // Render left bar
            RenderUtility.Render2D.drawRoundedCorner(position.x, position.y, 506 / FACTOR, size.y, new Vector4f(5,5,0,0), 0xFF0E1116);

            { // render item list

                { // searcher
                    CVec2 searchSize = new CVec2(453, 43).divide(FACTOR);
                    CVec2 searchPos = new CVec2(26, 162).divide(FACTOR).add(position);

                    RenderUtility.Render2D.drawRoundedRect(searchPos.x, searchPos.y, searchSize.x, searchSize.y, 5, 0xFF14151A);
                    Fonts.msMedium[17].drawString(new MatrixStack(), searcher.focused ? searcher.set.text : "Поиск.", searchPos.x + 5, searchPos.y + 8, new NColor(-1).withCustomAnimatedAlpha(0.5f).hashCode());
                }

                CVec2 listSize = new CVec2(453, 202).divide(FACTOR);
                CVec2 listPos = new CVec2(26, 232).divide(FACTOR).add(position);

                RenderUtility.Render2D.drawRoundedCorner(listPos.x, listPos.y, listSize.x, listSize.y, 5, 0xFF13151A);

                CVec2 offset = new CVec2(0, itemListScroll.y);

                RenderUtility.SmartScissor.push();
                RenderUtility.SmartScissor.setFromComponentCoordinates((int) listPos.x, (int) listPos.y + 1, (int) listSize.x, (int) listSize.y - 1);
                for (TestItem i : searcher.focused ? ItemManager.itemList.stream().filter(i -> i.name.toLowerCase().contains(searcher.set.text.toLowerCase())).collect(Collectors.toList()) : ItemManager.itemList) {
                    if (offset.y > listSize.y + 10) continue;
                    ItemStack item = i.item.getDefaultInstance();

                    if (i.donItem)
                        item.addEnchantment(new ProtectionEnchantment(Enchantment.Rarity.RARE, ProtectionEnchantment.Type.ALL, EquipmentSlotType.CHEST), 1);

                    RenderUtility.Render2D.drawRoundedRect(listPos.x() + 4 + offset.x, listPos.y() + 4 + offset.y, 17, 17, 2,
                            (selected == null || selected.name.equals(i.name)) ? selected == null ? 0xFF1C202B : 0xFFBC158F : 0xFF1C202B);

                    drawItemStack(item, listPos.x() + 5 + offset.x, listPos.y() + 5 + offset.y, 1f);

                    if (offset.x() > listSize.x() - 35) {
                        offset.x = 0;
                        offset.y += 20;
                    } else {
                        offset.x += 20;
                    }
                }
            }
            RenderUtility.SmartScissor.pop();

            if (selected != null) { // selected item info
                CVec2 selectedSize = new CVec2(106, 101).divide(FACTOR);
                CVec2 selectedPos = new CVec2(26, 33).divide(FACTOR).add(position);

                { // item preview
                    ItemStack item = selected.item.getDefaultInstance();

                    if (selected.donItem)
                        item.addEnchantment(new ProtectionEnchantment(Enchantment.Rarity.RARE, ProtectionEnchantment.Type.ALL, EquipmentSlotType.CHEST), 1);

                    RenderUtility.Render2D.drawRoundedRect(selectedPos.x, selectedPos.y, selectedSize.x, selectedSize.y, 5, 0xFF14151A);
                    drawItemStack(item, selectedPos.x + getMiddleOfBox(16, selectedSize.x), selectedPos.y() + getMiddleOfBox(16, selectedSize.y), 2f);
                }

                { // item name | Fields box
                    CVec2 boxSize = new CVec2(106, 101).divide(FACTOR);
                    CVec2 boxPos = new CVec2(149, 36).divide(FACTOR).add(position);

                    { // item name
                        Fonts.msBold[23].drawString(new MatrixStack(), selected.name, boxPos.x, boxPos.y, -1);
                    }

                    { // fields
                        selected.maxPrice.render(boxPos.x, boxPos.y + 15);
                    }
                }
            }

            { // buttons
                updateAnimations();
                { // add button
                    CVec2 addSize = new CVec2(220, 43).divide(FACTOR);
                    CVec2 addPos = new CVec2(26, 453).divide(FACTOR).add(position);
                    RenderUtility.Render2D.drawRoundedRect(addPos.x, addPos.y, addSize.x, addSize.y, 5, 0xFF14151A);
                    Color b = selected != null ?
                            new Color(0xFFFFFF) :
                            new Color(0xA1A1A1);
                    Fonts.msMedium[18].drawCenteredString(new MatrixStack(), "Добавить", (int) (addPos.x + addSize.x / 2f), (int) (addPos.y + getMiddleOfBox(Fonts.msMedium[18].getFontHeight(), addSize.y)) + 2, b.hashCode());
                }
                { // save button
                    CVec2 addSize = new CVec2(220, 43).divide(FACTOR).divide(2, 1).add(-5, 0);
                    CVec2 addPos = new CVec2(26, 453).divide(FACTOR).add(position).add(addSize.x * 2 + 20, 0);
                    RenderUtility.Render2D.drawRoundedRect(addPos.x, addPos.y, addSize.x, addSize.y, 5, 0xFF14151A);
                    Color b = selected != null ?
                            ItemManager.selectedItemsList.contains(selected) ? new Color(0xA1A1A1) :
                                    new Color(0xFFFFFF) :
                            new Color(0xA1A1A1);
                    Fonts.msMedium[18].drawCenteredString(new MatrixStack(), "Сохр.", (int) (addPos.x + addSize.x / 2f), (int) (addPos.y + getMiddleOfBox(Fonts.msMedium[18].getFontHeight(), addSize.y)) + 2, b.hashCode());

                    ScaleUtil.scaleStart((int) (addPos.x + addSize.x / 2f), (int) (addPos.y + getMiddleOfBox(Fonts.msMedium[18].getFontHeight(), addSize.y)) + 3, Math.max(0.02f, saveAnim.y));
                    Fonts.msBold[22].drawCenteredString(new MatrixStack(), "*", (int) (addPos.x + addSize.x / 2f), (int) (addPos.y + getMiddleOfBox(Fonts.msMedium[18].getFontHeight(), addSize.y)) + 2,
                            NColor.fromColor(b).withCustomAnimatedAlpha(1f - Math.abs(1f - saveAnim.y)).hashCode());
                    ScaleUtil.scaleEnd();

                }

                { // load button
                    CVec2 addSize = new CVec2(220, 43).divide(FACTOR).divide(2, 1).add(-5, 0);
                    CVec2 addPos = new CVec2(26, 453).divide(FACTOR).add(position).add(addSize.x * 2f + 20, 0).add(addSize.x + 7, 0);
                    RenderUtility.Render2D.drawRoundedRect(addPos.x, addPos.y, addSize.x, addSize.y, 5, 0xFF14151A);
                    Color b = selected != null ?
                            ItemManager.selectedItemsList.contains(selected) ? new Color(0xA1A1A1) :
                                    new Color(0xFFFFFF) :
                            new Color(0xA1A1A1);
                    Fonts.msMedium[18].drawCenteredString(new MatrixStack(), "Загр.", (int) (addPos.x + addSize.x / 2f), (int) (addPos.y + getMiddleOfBox(Fonts.msMedium[18].getFontHeight(), addSize.y)) + 2, b.hashCode());

                    ScaleUtil.scaleStart((int) (addPos.x + addSize.x / 2f), (int) (addPos.y + getMiddleOfBox(Fonts.msMedium[18].getFontHeight(), addSize.y)) + 3, Math.max(0.02f, loadAnim.y));
                    Fonts.msMedium[22].drawCenteredString(new MatrixStack(), "*", (int) (addPos.x + addSize.x / 2f), (int) (addPos.y + getMiddleOfBox(Fonts.msMedium[18].getFontHeight(), addSize.y)) + 2,
                            NColor.fromColor(b).withCustomAnimatedAlpha(1f - Math.abs(1f - loadAnim.y)).hashCode());
                    ScaleUtil.scaleEnd();
                }
            }
        }

        { // Render right bar
            { // render item list
                CVec2 listSize = new CVec2(360, 462).divide(FACTOR);
                CVec2 listPos = new CVec2(518, 33).divide(FACTOR).add(position);

                Fonts.msMedium[20].drawString(new MatrixStack(), "Добавленые предметы", listPos.x + listSize.x - Fonts.msMedium[23].getWidth("��������� ��������"), listPos.y - 10, -1);

                RenderUtility.Render2D.drawRoundedRect(listPos.x, listPos.y, listSize.x + 5, listSize.y, 5, new NColor(0xFF13151A).darker().hashCode());

                CVec2 offset = new CVec2(0, selectedItemListScroll.y);

                RenderUtility.SmartScissor.push();
                RenderUtility.SmartScissor.setFromComponentCoordinates((int) listPos.x, (int) listPos.y, (int) listSize.x, (int) listSize.y);
                for (TestItem i : ItemManager.selectedItemsList) {
                    if (offset.y > listSize.y + 10) continue;
                    ItemStack item = i.item.getDefaultInstance();

                    if (i.donItem)
                        item.addEnchantment(new ProtectionEnchantment(Enchantment.Rarity.RARE, ProtectionEnchantment.Type.ALL, EquipmentSlotType.CHEST), 1);

                    RenderUtility.Render2D.drawRoundedRect(listPos.x() + 4 + offset.x, listPos.y() + 4 + offset.y, 17, 17, 2,
                            (selected == null || selected.name.equals(i.name)) ? selected == null ? 0xFF1C202B : (selected.item.equals(i.item) ? 0xFFBC158F : 0xFF1C202B) : 0xFF1C202B);

                    drawItemStack(item, listPos.x() + 5 + offset.x, listPos.y() + 5 + offset.y, 1f);

                    if (offset.x() > listSize.x() - 35) {
                        offset.x = 0;
                        offset.y += 20;
                    } else {
                        offset.x += 20;
                    }
                }
                RenderUtility.SmartScissor.pop();

                offset = new CVec2(0, selectedItemListScroll.y);

                for (TestItem i : ItemManager.selectedItemsList) {
                    if (offset.y > listSize.y + 10) continue;

                    if (isHovered(listPos.x() + 4 + offset.x, listPos.y() + 4 + offset.y, 17, 17,mouseX,mouseY)) {
                        String price = String.valueOf(ItemManager.getPrice(i));
                        RenderUtility.Render2D.drawRoundedRect(mouseX - 1, mouseY - 7 - 1, Fonts.msMedium[15].getWidth(price) + 1, 7 + 1, 1,
                                (selected == null || selected.name.equals(i.name)) ? selected == null ? 0xFF1C202B : (selected.item.equals(i.item) ? 0xFFBC158F : 0xFF1C202B) : 0xFF1C202B);
                        Fonts.msMedium[15].drawString(new MatrixStack(), price, mouseX, mouseY - 6, -1);
                    }
                    if (offset.x() > listSize.x() - 35) {
                        offset.x = 0;
                        offset.y += 20;
                    } else {
                        offset.x += 20;
                    }
                }
            }

        }
    }

    public InputObject searcher = new InputObject(new TextSetting("searcher", ""));

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        { // left board
            CVec2 listSize = new CVec2(453, 202).divide(FACTOR);
            CVec2 listPos = new CVec2(26, 232).divide(FACTOR).add(position);

            if (isHovered(listPos.x, listPos.y, listSize.x, listSize.y, mouseX, mouseY)) {
                itemListScroll.x += (int) (delta * 16);
                if (itemListScroll.x > 0) itemListScroll.x = 0;
            }
        }

        { // right board
            CVec2 listSize = new CVec2(360, 462).divide(FACTOR);
            CVec2 listPos = new CVec2(518, 33).divide(FACTOR).add(position);

            if (isHovered(listPos.x, listPos.y, listSize.x, listSize.y, mouseX, mouseY)) {
                selectedItemListScroll.x += (int) (delta * 16);
                if (selectedItemListScroll.x > 0) selectedItemListScroll.x = 0;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (selected != null) { // ��������� ������� ���� ������ ��� ������
            selected.maxPrice.mousePressed(mouseX, mouseY, button);
        }
        { // Render left bar
            { // render item list

                CVec2 listSize = new CVec2(453, 202).divide(FACTOR);
                CVec2 listPos = new CVec2(26, 232).divide(FACTOR).add(position);

                CVec2 offset = new CVec2(0, itemListScroll.y);

                if (isHovered(listPos.x, listPos.y, listSize.x, listSize.y, mouseX,mouseY)) {
                    for (TestItem i : searcher.focused ? ItemManager.itemList.stream().filter(i -> i.name.toLowerCase().contains(searcher.set.text.toLowerCase())).collect(Collectors.toList()) : ItemManager.itemList) {
                        ItemStack item = i.item.getDefaultInstance();

                        if (i.donItem)
                            item.addEnchantment(new ProtectionEnchantment(Enchantment.Rarity.RARE, ProtectionEnchantment.Type.ALL, EquipmentSlotType.CHEST), 1);

                        if (isHovered(listPos.x() + 4 + offset.x, listPos.y() + 4 + offset.y, 17, 17, mouseX, mouseY)) {
                            selected = i;
                            return true;
                        }
                        if (offset.x() > listSize.x() - 35) {
                            offset.x = 0;
                            offset.y += 20;
                        } else {
                            offset.x += 20;
                        }
                    }
                }
            }

            { // buttons
                { // add button
                    CVec2 addSize = new CVec2(220, 43).divide(FACTOR);
                    CVec2 addPos = new CVec2(26, 453).divide(FACTOR).add(position);
                    if (selected != null) {
                        if (!ItemManager.selectedItemsList.contains(selected) && isHovered(addPos.x, addPos.y, addSize.x, addSize.y, mouseX, mouseY)) {
                            ItemManager.selectedItemsList.add(selected);
                            return true;
                        }
                    }
                }
                { // save button
                    CVec2 addSize = new CVec2(220, 43).divide(FACTOR).divide(2, 1).add(-5, 0);
                    CVec2 addPos = new CVec2(26, 453).divide(FACTOR).add(position).add(addSize.x * 2 + 20, 0);
                    if (isHovered(addPos.x, addPos.y, addSize.x, addSize.y, mouseX, mouseY)) {
                        //Managment.CONFIG_MANAGER.saveConfiguration("autocfg_�autobuy");
                        startAnim(1);
                    }
                }

                { // load button
                    CVec2 addSize = new CVec2(220, 43).divide(FACTOR).divide(2, 1).add(-5, 0);
                    CVec2 addPos = new CVec2(26, 453).divide(FACTOR).add(position).add(addSize.x * 2f + 20, 0).add(addSize.x + 7, 0);
                    if (isHovered(addPos.x, addPos.y, addSize.x, addSize.y, mouseX, mouseY)) {
                        //Managment.CONFIG_MANAGER.loadConfiguration("autocfg_�autobuy", true);
                        startAnim(2);
                    }
                }
            }
        }
        { // Render right bar
            { // render item list
                CVec2 listSize = new CVec2(360, 462).divide(FACTOR);
                CVec2 listPos = new CVec2(518, 33).divide(FACTOR).add(position);

                CVec2 offset = new CVec2(0, selectedItemListScroll.y);

                for (TestItem i : ItemManager.selectedItemsList) {
                    ItemStack item = i.item.getDefaultInstance();

                    if (i.donItem)
                        item.addEnchantment(new ProtectionEnchantment(Enchantment.Rarity.RARE, ProtectionEnchantment.Type.ALL, EquipmentSlotType.CHEST), 1);

                    if (isHovered(listPos.x() + 4 + offset.x, listPos.y() + 4 + offset.y, 17, 17, mouseX, mouseY)) {
                        if (button == 1) {
                            ItemManager.itemList.get(ItemManager.itemList.indexOf(i)).maxPrice.field.set.text = "";
                            ItemManager.selectedItemsList.remove(i);
                        }else {
                            selected = i;
                        }
                        return true;
                    }

                    if (offset.x() > listSize.x() - 35) {
                        offset.x = 0;
                        offset.y += 20;
                    } else {
                        offset.x += 20;
                    }
                }
            }

            { // searcher
                CVec2 searchSize = new CVec2(453, 43).divide(FACTOR);
                CVec2 searchPos = new CVec2(26, 162).divide(FACTOR).add(position);

                if (isHovered(searchPos.x, searchPos.y, searchSize.x, searchSize.y, mouseX,mouseY)) {
                    itemListScroll.x = itemListScroll.y = 0;
                }
                searcher.x = searchPos.x;
                searcher.y = searchPos.y;
                searcher.width = searchSize.w();
                searcher.height = searchSize.h() + 10;

                searcher.mouseClicked((int) mouseX, (int) mouseY, button);
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public void startAnim(int mode) {
        new Thread(() -> {
            switch (mode) {
                case 1:
                    saveAnim.update(new CVec2(
                            2,
                            saveAnim.y
                    ));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    saveAnim.update(new CVec2(
                            0,
                            0
                    ));
                    break;
                case 2:
                    loadAnim.update(new CVec2(
                            2,
                            loadAnim.y
                    ));
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    loadAnim.update(new CVec2(
                            0,
                            0
                    ));
                    break;
            }
        }).start();
    }

    public void updateAnimations() {
        itemListScroll.y = AnimationMath.fast(itemListScroll.x, itemListScroll.y, 44);
        selectedItemListScroll.y = AnimationMath.fast(selectedItemListScroll.x, selectedItemListScroll.y, 44);

        loadAnim.update(
                new CVec2(
                        loadAnim.x,
                        AnimationMath.fast(loadAnim.y, loadAnim.x, 10)
                )
        );
        saveAnim.update(
                new CVec2(
                        saveAnim.x,
                        AnimationMath.fast(saveAnim.y, saveAnim.x, 10)
                )
        );
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (selected != null) { // ��������� ������� ���� ������ ��� ������
            selected.maxPrice.keyTyped(keyCode, scanCode, modifiers);

            int index = ItemManager.selectedItemsList.indexOf(selected);

            if (index != -1) {
                ItemManager.selectedItemsList.remove(index);
                ItemManager.selectedItemsList.add(selected);
            }
        }

        searcher.keyTypedd(keyCode, scanCode, modifiers);
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (selected != null) { // ��������� ������� ��� ������
            selected.maxPrice.charTyped(codePoint, modifiers);
            if (selected.maxPrice.field.focused && searcher.focused) {
                selected.maxPrice.field.focused = searcher.focused = false;
            }
            ;
        }
        searcher.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }

    public boolean isHovered(double x, double y, double width, double height, double mouseX, double mouseY) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public float getMiddleOfBox(double objectSize, double boxSize) {
        return (float) (boxSize / 2f - objectSize / 2f);
    }

    public static void drawItemStack(ItemStack stack, double x, double y, float scale) {
        ScaleUtil.scaleStart((float) (x + 8), (float) (y + 8), scale);
        GL11.glPushMatrix();
        GL11.glScalef(-0.01f * -200, -0.01f * -200, -0.01f * -200);
        GL11.glPushMatrix();
        //GlStateManager.enableLighting();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
        GlStateManager.scaled(0.5, 0.5, 0.5);
        mc.getItemRenderer().renderItemIntoGUI(stack, (int) x, (int) y);

        String count = "";
        if (stack.getCount() > 1) {
            count = stack.getCount() + "";
        }
        mc.getItemRenderer().renderItemOverlayIntoGUI(mc.fontRenderer, stack, (int) x, (int) y, count);

        //RenderHelper.disableStandardItemLighting();
        //GlStateManager.disableLighting();
        GL11.glPopMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        ScaleUtil.scaleEnd();
    }
}
