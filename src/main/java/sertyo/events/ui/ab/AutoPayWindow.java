package sertyo.events.ui.ab;

import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.glfw.GLFW;
import sertyo.events.ui.ab.font.main.IFont;
import sertyo.events.utility.render.RenderUtil;

import java.awt.*;

import static sertyo.events.utility.Utility.mc;
import static sertyo.events.ui.ab.AutoBuyGui.getStringIgnoreLastChar;
import static sertyo.events.ui.ab.AutoBuyGui.isHover;

public class AutoPayWindow {
    public double x, y, w, h;

    public String search = "";
    public boolean write;
    public int dot = 0;

    public AutoPayWindow(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        search = AutoBuy.autoPayCom;
    }

    public void render() {
        w = IFont.getWidth(IFont.MONTSERRAT_MEDIUM, "  AutoPay: " + (search.isEmpty() ? "введите никнейм и мин.сумму" : search) + (write ? dot > 15 ? "_" : "" : "") + "  ", 8);
        x = (double) mc.getMainWindow().getScaledWidth() / 2 - w / 2;
        if (dot > 30) dot = 0;
        if (write) dot++;

        RenderUtil.Render2D.drawRoundedRect((float) (x - 2), (float) y, (float) (w + 4), (float) h, 6, new Color(16, 15, 15, 255).getRGB());
        IFont.drawCenteredXY(IFont.MONTSERRAT_MEDIUM, "  AutoPay: " + (search.isEmpty() ? "введите никнейм и мин.сумму" : search) + (write ? dot > 15 ? "_" : "" : "") + "  ", (float) (x + w / 2), (float) (y + h / 2), search.isEmpty() ? Color.GRAY : Color.WHITE, 8);
    }

    public void click(double mouseX, double mouseY, int button) {
        if (isHover(x, y + 4, w, 20, mouseX, mouseY) && button == GLFW.GLFW_MOUSE_BUTTON_1) {
            PrintStack.setCallback(this.getClass(), () -> write = false);
            write = true;
        }
    }

    public void key(int keyCode) {
        if (write) {
            if (keyCode == GLFW.GLFW_KEY_V && Screen.hasControlDown()) {
                search += mc.keyboardListener.getClipboardString();
            }
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                write = false;
                AutoBuy.autoPayCom = search;
            }
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                search = getStringIgnoreLastChar(search);
            }
        }
    }

    public void charTyped(char chr) {
        if (write) {
            search += chr;
        }
    }
}
