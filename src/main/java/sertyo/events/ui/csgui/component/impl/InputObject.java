package sertyo.events.ui.csgui.component.impl;

import com.mojang.blaze3d.matrix.MatrixStack;


import org.lwjgl.glfw.GLFW;

import sertyo.events.module.setting.impl.KeySetting;
import sertyo.events.module.setting.impl.TextSetting;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;

public class InputObject extends Component {

    public TextSetting set;
    public ModuleComponent object;



    public InputObject(ModuleComponent moduleComponent, TextSetting setting) {
        super(0.0F, 0.0F, 0.0F, 14.0F);
        this.object = moduleComponent;
        this.set = setting;
    }
    public InputObject(TextSetting setting) {
        super(0.0F, 0.0F, 0.0F, 14.0F);
        this.set = setting;
    }


    public boolean focused;

    public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        sertyo.events.utility.font.Fonts.msLight[13].drawString(new MatrixStack(), set.getName(), x + 5, y + height / 2f - Fonts.msLight[13].getFontHeight() / 2f + 2, ColorUtil.rgba(161, 166, 179, 255));

        RenderUtil.Render2D.drawRoundedRect(x + 5, y + 12, width - 10, 10, 4, sertyo.events.utility.render.ColorUtil.rgba(20, 21, 24, 255));

        Fonts.msLight[13].drawString(new MatrixStack(), set.text, x + 7 + (focused ? 1 : 0), y + 10 + height / 2f - Fonts.msLight[13].getFontHeight() / 2f + 2, ColorUtil.rgba(255, 255, 255, 255));
        this.height = 27;
    }


    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        {
            if (RenderUtil.isInRegion(mouseX, mouseY, x + 5, y + 12, width - 10, 10)) {
                focused = true;
            } else {
                focused = false;
            }
        }
    }



    public void keyTypedd(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER) {
            focused = false;
        }
        if (keyCode == GLFW.GLFW_KEY_DELETE || keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (set.text.isEmpty()) return;
            StringBuilder b = new StringBuilder();
            int i = 0;
            for (char c : set.text.toCharArray()) {
                if (i < set.text.length() - 1)
                    b.append(c);
                i++;
            }
            set.text = b.toString();
        }
    }

    public void charTyped(char codePoint, int modifiers) {
        if (Fonts.msLight[13].getWidth(set.text != null ? set.text : "") > width - 20) return;
        StringBuilder b = new StringBuilder();
        int i = 0;
        for (char c : set.text.toCharArray()) {
            if (i < set.text.length() - 1)
                b.append(c);
            else
                b.append(c + "" + codePoint);
            i++;
        }
        set.text = set.text.isEmpty() ? String.valueOf(codePoint) : b.toString();
    }
}
