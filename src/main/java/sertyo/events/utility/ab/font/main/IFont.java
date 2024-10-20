package sertyo.events.utility.ab.font.main;

import com.mojang.blaze3d.matrix.MatrixStack;

import java.awt.*;
import java.util.HashMap;

public class IFont {
    public static final int MONTSERRAT_BOLD = 1;
    public static final int MONTSERRAT_MEDIUM = 2;

    private static final HashMap<Integer, FontContainer> MAP = new HashMap<>();
    private static final MatrixStack EMPTY_STACK = new MatrixStack();
    private static int ACTIVE_FONT = MONTSERRAT_BOLD;
    private static boolean init;

    public static void init() {
        if (init) return;
        MAP.put(MONTSERRAT_BOLD, new FontContainer("mbold.ttf"));
        MAP.put(MONTSERRAT_MEDIUM, new FontContainer("mmedium.ttf"));
        init = true;
    }

    public static void draw(int font, String text, float x, float y, Color color, int size, MatrixStack stack) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x, y, color, false);
    }

    public static void draw(int font, String text, float x, float y, Color color, int size) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x, y, color, false);
    }

    public static void drawWithShadow(int font, String text, float x, float y, Color color, int size, MatrixStack stack) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x + 1, y + 1, color, true);
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x, y, color, false);
    }

    public static void drawWithShadow(int font, String text, float x, float y, Color color, int size) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x + 1, y + 1, color, true);
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x, y, color, false);
    }

    public static void drawCenteredX(int font, String text, float x, float y, Color color, int size, MatrixStack stack) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x - getWidth(text, size) / 2, y, color, false);
    }

    public static void drawCenteredX(int font, String text, float x, float y, Color color, int size) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x - getWidth(text, size) / 2, y, color, false);
    }

    public static void drawWithShadowCenteredX(int font, String text, float x, float y, Color color, int size, MatrixStack stack) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x - getWidth(text, size) / 2 + 1, y + 1, color, true);
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x - getWidth(text, size) / 2, y, color, false);
    }

    public static void drawWithShadowCenteredX(int font, String text, float x, float y, Color color, int size) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x - getWidth(text, size) / 2 + 1, y + 1, color, true);
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x - getWidth(text, size) / 2, y, color, false);
    }

    public static void drawCenteredY(int font, String text, float x, float y, Color color, int size, MatrixStack stack) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x, y - getHeight(text, size) / 2, color, false);
    }

    public static void drawCenteredY(int font, String text, float x, float y, Color color, int size) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x, y - getHeight(text, size) / 2, color, false);
    }

    public static void drawWithShadowCenteredY(int font, String text, float x, float y, Color color, int size, MatrixStack stack) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x + 1, y - getHeight(text, size) / 2 + 1, color, true);
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x, y - getHeight(text, size) / 2, color, false);
    }

    public static void drawWithShadowCenteredY(int font, String text, float x, float y, Color color, int size) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x + 1, y - getHeight(text, size) / 2 + 1, color, true);
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x, y - getHeight(text, size) / 2, color, false);
    }

    public static void drawCenteredXY(int font, String text, float x, float y, Color color, int size, MatrixStack stack) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x - getWidth(text, size) / 2, y - getHeight(text, size) / 2, color, false);
    }

    public static void drawCenteredXY(int font, String text, float x, float y, Color color, int size) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x - getWidth(text, size) / 2, y - getHeight(text, size) / 2, color, false);
    }

    public static void drawWithShadowCenteredXY(int font, String text, float x, float y, Color color, int size, MatrixStack stack) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x - getWidth(text, size) / 2 + 1, y - getHeight(text, size) / 2 + 1, color, true);
        MAP.get(ACTIVE_FONT).get(size).drawString(stack, text, x - getWidth(text, size) / 2, y - getHeight(text, size) / 2, color, false);
    }

    public static void drawWithShadowCenteredXY(int font, String text, float x, float y, Color color, int size) {
        ACTIVE_FONT = font;
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x - getWidth(text, size) / 2 + 1, y - getHeight(text, size) / 2 + 1, color, true);
        MAP.get(ACTIVE_FONT).get(size).drawString(EMPTY_STACK, text, x - getWidth(text, size) / 2, y - getHeight(text, size) / 2, color, false);
    }

    public static float getWidth(int font, String text, int size) {
        ACTIVE_FONT = font;
        return MAP.get(ACTIVE_FONT).get(size).getStringWidth(text);
    }

    public static float getHeight(int font, String text, int size) {
        ACTIVE_FONT = font;
        return MAP.get(ACTIVE_FONT).get(size).getStringHeight(text);
    }

    private static float getWidth(String text, int size) {
        return MAP.get(ACTIVE_FONT).get(size).getStringWidth(text);
    }

    private static float getHeight(String text, int size) {
        return MAP.get(ACTIVE_FONT).get(size).getStringHeight(text);
    }

    public static Color getShadowColor(Color color) {
        return new Color((color.getRGB() & 16579836) >> 2 | color.getRGB() & -16777216);
    }
}