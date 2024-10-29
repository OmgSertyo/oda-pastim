package sertyo.events.ui.ab.font.utils;

import sertyo.events.Main;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;

public class InstallUtils {
    public static Font installFont(String link, int size) {
        Font result;

        BufferedInputStream b = new BufferedInputStream(Main.class.getResourceAsStream("/assets/minecraft/font/" + link));

        try {
            result = Font
                    .createFont(Font.TRUETYPE_FONT, b)
                    .deriveFont(Font.PLAIN, size);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
