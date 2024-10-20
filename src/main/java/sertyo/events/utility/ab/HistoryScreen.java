package sertyo.events.utility.ab;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import sertyo.events.utility.ab.manager.HistoryItem;
import sertyo.events.utility.ab.manager.HistoryManager;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.Animation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static sertyo.events.utility.ab.AutoBuyGui.isHover;


public class HistoryScreen {
    public double x, y, w, h;
    public List<HistoryItemButton> historyItemButtons = new ArrayList<>();
    public double scroll = 0, targetScroll;
    public Screen screen;

    public HistoryScreen(double x, double y, double w, double h, Screen father) {
        this.screen = father;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        double y2 = y + 4;
        for (HistoryItem historyItem : HistoryManager.getHistoryItem()) {
            historyItemButtons.add(new HistoryItemButton(x, y2, w, 18, historyItem));
            y2 += 20;
        }
        if (calcH() - h > 0) {
            targetScroll = -(calcH() - h);
            scroll = -(calcH() - h);
        }
    }

    public void open() {
        historyItemButtons.clear();
        double y2 = y + 4;
        for (HistoryItem historyItem : HistoryManager.getHistoryItem()) {
            historyItemButtons.add(new HistoryItemButton(x, y2, w, 18, historyItem));
            y2 += 20;
        }
        if (calcH() - h > 0) {
            targetScroll = -(calcH() - h);
            scroll = -(calcH() - h);
        }
    }

    public double calcH() {
        return historyItemButtons.size() * 20 + 6;
    }

    public void render(int mx, int my) {
        targetScroll = MathHelper.clamp(targetScroll, -(calcH() - (h - 24)), 0);
        ItemStack stack = null;
        if (calcH() - h < 0) {
            targetScroll = 0;
        }
        scroll = Animation.fast(scroll, targetScroll);

        RenderUtil.Render2D.drawRoundedRect(x, y, w, h, 4, new Color(39, 37, 37, 255));

        double y2 = y + 4 + scroll;
        ScissorUtils.enableScissor(x, y + 4, x + w, y + h);
        for (HistoryItemButton autoBuyButton : historyItemButtons) {
            autoBuyButton.y = y2;
            y2 += 20;
            if (y2 > y + h + 100 || y2 < y) continue;
            if (isHover(autoBuyButton.x + 4, autoBuyButton.y, 16, 16, mx, my)) {
                stack = autoBuyButton.historyItem.stack;
            }
            autoBuyButton.render(mx, my);
        }
        ScissorUtils.disableScissor();
        if (stack != null) {
            MatrixStack stack1 = new MatrixStack();
            stack1.scale(1f, 1f, 1f);
            screen.renderTooltip(stack1, stack, mx, my);
        }
    }

    public void scroll(double mx, double my, double delta) {
        if (isHover(x, y + 4, w, h - 4, mx, my) && (calcH() - h > 0)) {
            if (delta < 0) {
                if (targetScroll > -(calcH() - h)) {
                    targetScroll -= 15;
                }
                targetScroll = MathHelper.clamp(targetScroll, -(calcH() - h), 0);
            }
            if (delta > 0) {
                if (targetScroll < 0) {
                    targetScroll += 15;
                }
                targetScroll = MathHelper.clamp(targetScroll, -(calcH() - h), 0);
            }
        }
    }
}