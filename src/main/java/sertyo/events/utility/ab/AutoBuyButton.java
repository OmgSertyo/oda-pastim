package sertyo.events.utility.ab;


import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import sertyo.events.Main;
import sertyo.events.utility.ab.font.main.IFont;
import sertyo.events.utility.ab.manager.AutoBuyItem;
import sertyo.events.utility.ab.manager.AutoBuyManager;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.Animation;

import java.awt.*;
import java.util.HashMap;

import static sertyo.events.utility.Utility.mc;

public class AutoBuyButton {
    public double x, y, w, h;
    public sertyo.events.utility.ab.manager.AutoBuyItem autoBuyItem;
    public String name;

    public double offset = 0;
    public int alpha;
    public boolean write;
    public int dot;
    public String override;

    public AutoBuyButton(double x, double y, double w, double h, sertyo.events.utility.ab.manager.AutoBuyItem autoBuyItem, String name) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.autoBuyItem = autoBuyItem;
        this.name = name;
    }

    public static final HashMap<Integer, String> replaceMap = new HashMap<>(){{
        put(4, "*.***");
        put(5, "**.***");
        put(6, "***.***");
        put(7, "*.***.***");
        put(8, "**.***.***");
        put(9, "***.***.***");
        put(10, "*.***.***.***");
        put(11, "**.***.***.***");
        put(12, "***.***.***.***");
    }};

    public static String replace(String in) {
        if (in.length() <= 3) return in;
        try {

            StringBuilder s = new StringBuilder();
            String replaceManual = replaceMap.get(in.length());

            char[] chars = replaceManual.toCharArray();
            int index = 0;
            for (int i = 0; i < replaceManual.toCharArray().length; i++) {
                if (chars[i] == '.') {
                    s.append('.');
                } else {
                    s.append(in.toCharArray()[index]);
                    index++;
                }
            }

            return s.toString();
        } catch (Exception exc) {
            return in;
        }
    }

    public void render(int mx, int my) {
        if (AutoBuyGui.isHover(x, y, 32, 16, mx, my)) {
            offset = Animation.fast(offset, 8);
            if (alpha < 255) alpha += 15;
        } else {
            if (alpha > 0) alpha -= 15;
            offset = Animation.fast(offset, 0);
        }
        RenderUtil.Render2D.drawRoundedRect((float) (x + 4), (float) y, (float) (w - 8), (float) h, 2, new Color(16, 15, 15, 255).getRGB());
        if (alpha > 0) {
            IFont.drawCenteredXY(IFont.MONTSERRAT_BOLD, " - ", (float) (x + 12), (float) (y + 8), new Color(255, 0, 0, MathHelper.clamp(alpha, 0, 255)), 8);
        }
        GL11.glPushMatrix();
        GL11.glTranslated(offset, 0, 0);
        IFont.drawCenteredY(IFont.MONTSERRAT_MEDIUM, clampName(), (float) (x + 28), (float) (y + h / 2), Color.WHITE, 8);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 8, y + h / 2 - 8, 0);
        mc.getItemRenderer().renderItemIntoGUI(autoBuyItem.item.getDefaultInstance(), 0, 0);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        String p = replace(autoBuyItem.price + "") + "$";
        if (write) {
            if (dot > 45) dot = 0;
            dot++;
            p = replace(override) + (dot > 30 ? "_" : "");
        }
        IFont.drawCenteredY(IFont.MONTSERRAT_MEDIUM, p, (float) (x + w - 8 - IFont.getWidth(IFont.MONTSERRAT_MEDIUM, p, 8)), (float) (y + h / 2), Color.WHITE, 8);
    }

    public String clampName() {
        StringBuilder s = new StringBuilder();
        double tw = 0;

        for (char c : name.toCharArray()) {
            s.append(c);
            tw += IFont.getWidth(IFont.MONTSERRAT_MEDIUM, c + "", 8);
            if (tw > 100 && c != name.toCharArray()[name.toCharArray().length - 1]) {
                s.append("...");
                break;
            }
        }

        return s.toString();
    }

    public void click(int mx, int my, int b) {
        if (AutoBuyGui.isHover(x, y, 32, 16, mx, my) && b == GLFW.GLFW_MOUSE_BUTTON_1) {
            Main.abGui.autoBuyButtons.remove(this);
            AutoBuyManager.removeItem(autoBuyItem);
        }

        String p = replace(autoBuyItem.price + "") + "$";
        if (AutoBuyGui.isHover(x + w - 8 - IFont.getWidth(IFont.MONTSERRAT_MEDIUM, p, 8), y, IFont.getWidth(IFont.MONTSERRAT_MEDIUM, p, 8), h, mx, my) && b == GLFW.GLFW_MOUSE_BUTTON_1) {
            PrintStack.setCallback(this.getClass(), () -> write = false);
            write = true;
            override = autoBuyItem.price + "";
        }
    }

    public void key(int key) {
        if (write) {
            if (key == GLFW.GLFW_KEY_BACKSPACE) {
                override = AutoBuyGui.getStringIgnoreLastChar(override);
            }
            if (key == GLFW.GLFW_KEY_ENTER) {
                int parse = 1;
                try {
                    parse = Integer.parseInt(override);
                } catch (Exception ignore) {
                }
                parse = MathHelper.clamp(parse, 1, Integer.MAX_VALUE);
                autoBuyItem.price = parse;
                write = false;
            }
        }
    }

    public void charTyped(char chr) {
        if (isNum(chr) && write) {
            override += chr;
        }
    }

    public boolean isNum(int key) {
        try {
            Integer.parseInt(String.valueOf(key));
            return true;
        } catch (Exception exc) {
            return false;
        }
    }
}
