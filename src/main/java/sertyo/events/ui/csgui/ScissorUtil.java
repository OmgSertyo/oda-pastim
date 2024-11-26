package sertyo.events.ui.csgui;

import net.minecraft.client.MainWindow;
import org.lwjgl.opengl.GL11;

public class ScissorUtil {
    public static void enable() {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void disable() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void scissor(MainWindow window, double x, double y, double width, double height) {
        if (x + width == x || y + height == y || x < 0 || y + height < 0) return;
        final double scaleFactor = window.getGuiScaleFactor();
        GL11.glScissor((int) Math.round(x * scaleFactor), (int) Math.round((window.getScaledHeight() - (y + height)) * scaleFactor), (int) Math.round(width * scaleFactor), (int) Math.round(height * scaleFactor));
    }
}