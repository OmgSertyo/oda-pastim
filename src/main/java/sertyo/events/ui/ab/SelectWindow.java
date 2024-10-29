package sertyo.events.ui.ab;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import sertyo.events.Main;
import sertyo.events.ui.ab.font.main.IFont;
import sertyo.events.ui.ab.manager.AutoBuyManager;
import sertyo.events.ui.ab.manager.CustomAutoBuyItem;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.Animation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static sertyo.events.ui.ab.AutoBuyGui.getStringIgnoreLastChar;
import static sertyo.events.ui.ab.AutoBuyGui.isHover;

public class SelectWindow {
    public double x, y, w, h;
    public List<SelectWindowButton> selectWindowButtonList = new ArrayList<>();
    public String search = "";
    public boolean write;
    public double scroll = 0, targetScroll;
    public int dot = 0;

    public SelectWindow(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        double y2 = y + 26;

        for (CustomAutoBuyItem customAutoBuyItem : AutoBuyManager.getCustomAutoBuyItemList()) {
            selectWindowButtonList.add(new SelectWindowButton(x, y2, w, 18, customAutoBuyItem.item, customAutoBuyItem.name, customAutoBuyItem));
            y += 20;
        }

        for (Item item : Registry.ITEM) {
            if (item == Items.AIR) continue;
            selectWindowButtonList.add(new SelectWindowButton(x, y2, w, 18, item, item.getName().getString(), null));
            y += 20;
        }
    }

    public void open() {
        selectWindowButtonList.clear();
        double y2 = y + 26;

        for (CustomAutoBuyItem customAutoBuyItem : AutoBuyManager.getCustomAutoBuyItemList()) {
            selectWindowButtonList.add(new SelectWindowButton(x, y2, w, 18, customAutoBuyItem.item, customAutoBuyItem.name, customAutoBuyItem));
            y2 += 20;
        }

        for (Item item : Registry.ITEM) {
            if (item == Items.AIR) continue;
            selectWindowButtonList.add(new SelectWindowButton(x, y2, w, 18, item, item.getName().getString(), null));
            y2 += 20;
        }
    }

    public double calcH() {
        return selectWindowButtonList.stream().filter(f -> f.name.toLowerCase().contains(search.toLowerCase())).toList().size() * 20 + 4;
    }

    public void render(int mx, int my) {
        targetScroll = MathHelper.clamp(targetScroll, -(calcH() - (h - 24)), 0);
        if (calcH() - (h - 24) < 0) {
            targetScroll = 0;
        }
        scroll = Animation.fast(scroll, targetScroll);
        Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
        Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
        RenderUtil.Render2D.drawShadow((float) x, (float) y + 4, (float) (w), (float) h, 4,
                new Color(ColorUtil.setAlpha(color2.getRGB(), 175)).getRGB(),
                new Color(ColorUtil.setAlpha(color.getRGB(), 175)).getRGB(),
                new Color(ColorUtil.setAlpha(color.getRGB(), 175)).getRGB(),
                new Color(ColorUtil.setAlpha(color2.getRGB(), 175)).getRGB());

        RenderUtil.Render2D.drawRoundedRect((float) x, (float) y + 4, (float) (w), (float) h, 4, new Color(28, 28, 28, 200).getRGB());
        if (dot > 20) dot = 0;
        if (write) dot++;

        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, 300);
        RenderUtil.Render2D.drawShadow((float) x + 4, (float) y - 20, (float) (w - 8), (float) 20, 4,
                new Color(ColorUtil.setAlpha(color2.getRGB(), 175)).getRGB(),
                new Color(ColorUtil.setAlpha(color.getRGB(), 175)).getRGB(),
                new Color(ColorUtil.setAlpha(color.getRGB(), 175)).getRGB(),
                new Color(ColorUtil.setAlpha(color2.getRGB(), 175)).getRGB());

        RenderUtil.Render2D.drawRoundedRect((float) x + 4, (float) y - 20, (float) (w - 8), (float) 20, 4, new Color(28, 28, 28, 200).getRGB());
        IFont.drawCenteredY(IFont.MONTSERRAT_MEDIUM, search.isEmpty() && !write ? "Поиск..." : search + (write ? dot > 10 ? "_" : "" : ""), (float) (x + 8), (float) (y + 14 - 24), search.isEmpty() ? Color.GRAY : Color.WHITE, 8);
        String reset = "очистить";
//        RenderUtil.Render2D.drawRoundedRect((float) (x + w - 4 - IFont.getWidth(IFont.MONTSERRAT_MEDIUM, reset, 8) - 6), (float) (y + 4 + 10 - IFont.getHeight(IFont.MONTSERRAT_MEDIUM - 6, reset, 8) / 2 - 1), IFont.getWidth(IFont.MONTSERRAT_MEDIUM, reset, 8) + 4, IFont.getHeight(IFont.MONTSERRAT_MEDIUM, reset, 8) + 2, 2, new Color(43, 44, 44, 255).getRGB());
        IFont.drawCenteredXY(IFont.MONTSERRAT_MEDIUM, reset, (float) (x + w - 4 - IFont.getWidth(IFont.MONTSERRAT_MEDIUM, reset, 8) / 2 - 4), (float) (y + 14 - 24), Color.WHITE, 8);
        GL11.glPopMatrix();

        double y2 = y + 26 + scroll;
        ScissorUtils.enableScissor(x, y + 24 - 14, x + w, y + h);
        for (SelectWindowButton autoBuyButton : selectWindowButtonList) {
            if (!autoBuyButton.name.toLowerCase().contains(search.toLowerCase())) continue;
            autoBuyButton.y = y2;
            y2 += 20;
            if (y2 > y + h + 100 || y2 < y) continue;
            autoBuyButton.render(mx, my);
        }
        ScissorUtils.disableScissor();
    }

    public void click(double mx, double my, int b) {
        if (isHover(x, y + 24 - 20, w, h - 24, mx, my)) {
            for (SelectWindowButton autoBuyButton : selectWindowButtonList) {
                if (!autoBuyButton.name.toLowerCase().contains(search.toLowerCase())) continue;
                autoBuyButton.click((int) mx, (int) my, b);
            }
        }
        if (isHover(x, y - 6 - 20, w, 20, mx, my)) {
            PrintStack.setCallback(this.getClass(), () -> write = false);
            write = true;
        }
        if (isHover(x + w - 8 - IFont.getWidth(IFont.MONTSERRAT_MEDIUM, "очистить", 8) - 6, y - 20, IFont.getWidth(IFont.MONTSERRAT_MEDIUM, "очистить", 8) + 8, 20, mx, my) && b == GLFW.GLFW_MOUSE_BUTTON_1) {
            write = false;
            search = "";
        }
    }

    public void key(int key) {
        if (write) {
            if (key == GLFW.GLFW_KEY_ENTER) {
                write = false;
            }
            if (key == GLFW.GLFW_KEY_BACKSPACE) {
                search = getStringIgnoreLastChar(search);
            }
        }
    }

    public void charTyped(char chr) {
        if (write) {
            search += chr;
        }
    }

    public void scroll(double mx, double my, double delta) {
        if (isHover(x, y + 24, w, h - 24, mx, my) && (calcH() - (h - 24) > 0)) {
            if (delta < 0) {
                if (targetScroll > -(calcH() - h)) {
                    targetScroll -= 15;
                }
                targetScroll = MathHelper.clamp(targetScroll, -(calcH() - (h - 24)), 0);
            }
            if (delta > 0) {
                if (targetScroll < 0) {
                    targetScroll += 15;
                }
                targetScroll = MathHelper.clamp(targetScroll, -(calcH() - (h - 24)), 0);
            }
        }
    }
}
