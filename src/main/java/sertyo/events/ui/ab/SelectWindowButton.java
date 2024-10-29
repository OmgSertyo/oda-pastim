package sertyo.events.ui.ab;

import net.minecraft.item.Item;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import sertyo.events.Main;
import sertyo.events.ui.ab.font.main.IFont;
import sertyo.events.ui.ab.manager.AutoBuyItem;
import sertyo.events.ui.ab.manager.AutoBuyManager;
import sertyo.events.ui.ab.manager.CustomAutoBuyItem;
import sertyo.events.ui.ab.manager.DefaultAutoBuyItem;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;

import java.awt.*;

import static sertyo.events.utility.Utility.mc;

public class SelectWindowButton {
    public double x, y, w, h;
    public Item item;
    public String name;
    public CustomAutoBuyItem customAutoBuyItem = null;

    public SelectWindowButton(double x, double y, double w, double h, Item item, String name, CustomAutoBuyItem customAutoBuyItem) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.item = item;
        this.name = name;
        this.customAutoBuyItem = customAutoBuyItem;
    }

    public void render(double mx, double my) {
        Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
        Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
        //  RenderUtil.Render2D.drawRoundedRect((float) (x + 4), (float) y, (float) (w - 8), (float) h, 2, new Color(16, 15, 15, 255).getRGB());
        RenderUtil.Render2D.drawShadow((float) x + 4, (float) y -14 , (float) (w - 8), (float) h, 4,
                new Color(ColorUtil.setAlpha(color.getRGB(), 200)).getRGB(),
                new Color(ColorUtil.setAlpha(color.getRGB(), 200)).getRGB(),
                new Color(ColorUtil.setAlpha(color.getRGB(), 200)).getRGB(),
                new Color(ColorUtil.setAlpha(color.getRGB(), 200)).getRGB());
        RenderUtil.Render2D.drawRoundedRect((float) x + 4, (float) y - 14, (float) (w - 8), (float) h, 4, new Color(28, 28, 28, 200).getRGB());
        IFont.drawCenteredY(IFont.MONTSERRAT_MEDIUM, name, (float) (x + 28), (float) (y -14  + h / 2), Color.WHITE, 8);
        IFont.drawCenteredY(IFont.MONTSERRAT_MEDIUM, " + ", (float) (x + w - 8 - IFont.getWidth(IFont.MONTSERRAT_MEDIUM, " + ", 8)), (float) (y -14  + h / 2), Color.GREEN, 8);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 8, y -14  + h / 2 - 8, 0);
        mc.getItemRenderer().renderItemIntoGUI(item.getDefaultInstance(), 0, 0);
        GL11.glPopMatrix();
    }

    public void click(double mx, double my, int b) {
        if (AutoBuyGui.isHover(x + w - 32, y -14 , 32, h, mx, my) && b == GLFW.GLFW_MOUSE_BUTTON_1) {
            AutoBuyItem newItem;
            if (customAutoBuyItem != null) {
                newItem = new CustomAutoBuyItem(item, 1000);
                ((CustomAutoBuyItem) newItem).name = customAutoBuyItem.name;
                ((CustomAutoBuyItem) newItem).line = customAutoBuyItem.line;
                ((CustomAutoBuyItem) newItem).enchantments = customAutoBuyItem.enchantments;
                ((CustomAutoBuyItem) newItem).attributes = customAutoBuyItem.attributes;
                ((CustomAutoBuyItem) newItem).effect = customAutoBuyItem.effect;
                ((CustomAutoBuyItem) newItem).strictCheck = customAutoBuyItem.strictCheck;
            } else {
                newItem = new DefaultAutoBuyItem(item, 1000);
            }
            AutoBuyManager.addItem(newItem);
            Main.abGui.addItem(newItem);
        }
    }
}