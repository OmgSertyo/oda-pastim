
package sertyo.events.ui.csgui.component.impl;

import org.lwjgl.glfw.GLFW;
import sertyo.events.Main;
import sertyo.events.module.setting.impl.KeySetting;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.render.ColorUtility;
import sertyo.events.utility.render.RenderUtility;

import java.awt.*;

public class KeyComponent extends Component {
    public static ModuleComponent moduleComponent;
    public static KeySetting set;
    private boolean binding = false;
    private static final KeyComponent instance = new KeyComponent(moduleComponent, set);

    public KeyComponent(ModuleComponent moduleComponent, KeySetting setting) {
        super(0.0F, 0.0F, 0.0F, 14.0F);
        this.moduleComponent = moduleComponent;
        this.set = setting;
    }
    public static KeyComponent getInstance() {
        return instance;
    }

    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        setHeight(18);
        float max = Math.max(11, Fonts.msBold[14].getWidth(Main.getKey(set.get()) == null ? "" : Main.getKey(set.get()).toUpperCase()) + 6);
        RenderUtility.Render2D.drawRoundedRect(x + width - max - 5, y + 5, max, 8, 3, Color.decode("#2B2C44").getRGB());
        if (binding) {
            Fonts.msBold[14].drawCenteredString(System.currentTimeMillis() % 1000 > 500 ? "_" : "", x + width - max - 5 + max / 2f, y + 7, (new Color(149, 149, 161)).getRGB());
        } else {
            Fonts.msBold[14].drawCenteredString(Main.getKey(set.get()) == null ? "" : Main.getKey(set.get()).toUpperCase(), x + width - max - 5 + max / 2f, y + 7, (new Color(149, 149, 161)).getRGB());
        }
        Fonts.msBold[15].drawString(set.getName(), x + 5, y + 6.5f, (new Color(149, 149, 161)).getRGB());
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isHovered((int) mouseX, (int) mouseY, x, y, width, height) && mouseButton != 2) binding = !binding;
    }
    public void keyTypedd(int keyCode, int scanCode, int modifiers) {
        if (binding) {
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                set.setKey(0);
                binding = false;
                return;
            }
            set.setKey(keyCode);
            binding = false;
        }
    }
    public boolean isVisible() {
        return (Boolean)this.set.getVisible().get();
    }
}
