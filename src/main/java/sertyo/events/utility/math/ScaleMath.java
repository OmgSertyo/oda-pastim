package sertyo.events.utility.math;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;

import static sertyo.events.utility.Utility.mc;


public class ScaleMath {
    private int scale;

    public void pushScale() {
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            Minecraft.getInstance().gameRenderer.setupOverlayRendering(2); //Мне похуй на это я очень пиздатый пац
        } else {
            Minecraft.getInstance().gameRenderer.setupOverlayRendering(2);

        }
    }

    public void popScale() {
        Minecraft.getInstance().gameRenderer.setupOverlayRendering();
    }

    public int calc(int value) {
        MainWindow rs = mc.getMainWindow();
        return (int) (value * rs.getGuiScaleFactor() / this.scale);
    }

    public float calc(float value) {
        MainWindow rs = mc.getMainWindow();
        return value * (float)rs.getGuiScaleFactor() / (float)this.scale;
    }

    public Vec2i getMouse(int mouseX, int mouseY, MainWindow rs) {
        return new Vec2i((int) (mouseX * rs.getGuiScaleFactor() / this.scale), (int) (mouseY * rs.getGuiScaleFactor() / this.scale));
    }

    public ScaleMath(int scale) {
        this.scale = scale;
    }

    public int getScale() {
        return this.scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }
}
