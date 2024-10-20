package sertyo.events.utility.ab;


import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import sertyo.events.utility.ab.font.main.IFont;
import sertyo.events.utility.ab.manager.HistoryItem;
import sertyo.events.utility.render.RenderUtil;

import java.awt.*;
import java.util.Formatter;

import static sertyo.events.utility.Utility.mc;


public class HistoryItemButton {
    public double x, y, w, h;
    public HistoryItem historyItem;

    public HistoryItemButton(double x, double y, double w, double h, HistoryItem historyItem) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.historyItem = historyItem;
    }

    public void render(int mx, int my) {
        RenderUtil.Render2D.drawRoundedRect(x + 4, y, w - 8, h, 2, new Color(16, 15, 15, 255));
        IFont.drawCenteredY(IFont.MONTSERRAT_MEDIUM, clampName() + " | " + AutoBuyButton.replace(historyItem.price + "") + "$ | " + (historyItem.purchased ? "§aкуплено" : "§cне куплено"), (float) (x + 28), (float) (y + h / 2), Color.WHITE, 8);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 8, y + h / 2 - 8, 0);
        mc.getItemRenderer().renderItemIntoGUI(historyItem.stack, 0, 0);
        mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, historyItem.stack, 0, 0);
        GL11.glPopMatrix();
    }

    public String clampName() {
        StringBuilder s = new StringBuilder();
        double tw = 0;

        for (char c : historyItem.stack.getDisplayName().getString().toCharArray()) {
            s.append(c);
            tw += IFont.getWidth(IFont.MONTSERRAT_MEDIUM, c + "", 8);
            if (tw > 50 && c != historyItem.stack.getDisplayName().getString().toCharArray()[historyItem.stack.getDisplayName().getString().toCharArray().length - 1]) {
                s.append("...");
                break;
            }
        }

        return s.toString();
    }
}
