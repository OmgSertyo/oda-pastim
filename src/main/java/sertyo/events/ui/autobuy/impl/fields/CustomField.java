package sertyo.events.ui.autobuy.impl.fields;

import com.mojang.blaze3d.matrix.MatrixStack;
import sertyo.events.module.setting.impl.TextSetting;
import sertyo.events.ui.csgui.component.impl.InputObject;
import sertyo.events.utility.font.Fonts;


import java.awt.*;

public class CustomField {
    public final String name;
    public final String description;
    public InputObject field;

    public CustomField(String name, String description) {
        this.name = name;
        this.description = description;
        this.field = new InputObject(new TextSetting(name, ""));
    }

    public CustomField(String name, String description, String initialValue) {
        this.name = name;
        this.description = description;
        this.field = new InputObject(new TextSetting(name, initialValue));
    }

    public void setValue(String text) {
        try {
            long value = Long.parseLong(text);
            if (value >= 1000000000) {
                field.set.text = text;
            } else {
                field.set.text = "1000000000";
            }
        } catch (NumberFormatException e) {
        }
    }

    public void render(float x, float y) {
        Fonts.msMedium[18].drawString(new MatrixStack(), name, x, y, -1);
        Fonts.msMedium[18].drawString(new MatrixStack(), description, x, y + 10, new Color(0xB9B9B9).hashCode());

        field.x = x;
        field.y = y + 20;
        field.width = 175;

        field.render(1, 1);
    }

    public void mousePressed(double mouseX, double mouseY, int button) {
        field.mouseClicked((int) mouseX, (int) mouseY, button);
    }

    public void keyTyped(int keyCode, int scanCode, int modifiers) {
        field.keyTypedd(keyCode, scanCode, modifiers);
    }

    public void charTyped(char codePoint, int modifiers) {
        field.charTyped(codePoint, modifiers);
    }
}