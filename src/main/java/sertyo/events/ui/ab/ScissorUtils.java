package sertyo.events.ui.ab;

import com.mojang.blaze3d.systems.RenderSystem;

import static sertyo.events.utility.Utility.mc;

public class ScissorUtils {
    public static void enableScissor(double x, double y, double endX, double endY) {
        double width = endX - x;
        double height = endY - y;
        width = Math.max(0, width);
        height = Math.max(0, height);
        float mulScale = (float) mc.getMainWindow().getGuiScaleFactor();
        int invertedY = (int) ((mc.getMainWindow().getScaledHeight() - (y + height)) * mulScale);
        RenderSystem.enableScissor((int) (x * mulScale), invertedY, (int) (width * mulScale), (int) (height * mulScale));
    }

    public static void disableScissor() {
        RenderSystem.disableScissor();
    }
}
