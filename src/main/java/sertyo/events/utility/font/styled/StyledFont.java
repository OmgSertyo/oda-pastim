package sertyo.events.utility.font.styled;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.text.ITextComponent;
import sertyo.events.utility.math.MathUtility;
import sertyo.events.utility.font.common.AbstractFont;
import sertyo.events.utility.font.common.Lang;

import java.awt.*;
import java.util.Locale;

public final class StyledFont {

    private final GlyphPage regular;

    public StyledFont(String fileName, int size, float stretching, float spacing, float lifting, boolean antialiasing, Lang lang) {
        int[] codes = lang.getCharCodes();
        char[] chars = new char[(codes[1] - codes[0] + codes[3] - codes[2])];

        int c = 0;
        for (int d = 0; d <= 2; d += 2) {
            for (int i = codes[d]; i <= codes[d + 1] - 1; i++) {
                chars[c] = (char) i;
                c++;
            }
        }

        this.regular = new GlyphPage(AbstractFont.getFont(fileName, Font.PLAIN, size), chars, stretching, spacing, lifting, antialiasing);
    }



    public float renderGlyph(Matrix4f matrix, char c, float x, float y, boolean bold, boolean italic, float red, float green, float blue, float alpha) {
        return getGlyphPage().renderGlyph(matrix, c, x, y, red, green, blue, alpha);
    }

    public void drawStringWithShadow(MatrixStack matrixStack, ITextComponent text, double x, double y, int color) {
        StyledFontRenderer.drawShadowedString(matrixStack, this, text, x, y, color);
    }

    public void drawString(MatrixStack matrixStack, String text, double x, double y, int color) {
        StyledFontRenderer.drawString(matrixStack, this, text, x, y, color);
    }
    public void drawString(MatrixStack matrixStack, String text, double x, double y, Color color, Color color2) {
        StyledFontRenderer.drawString(matrixStack, this, text, x, y, color, color2);
    }
    public void drawString(String text, double x, double y, int color) {
        StyledFontRenderer.drawString(new MatrixStack(), this, text, x, y, color);
    }


    public void drawString(MatrixStack matrixStack, ITextComponent text, double x, double y, int color) {
        StyledFontRenderer.drawString(matrixStack, this, text, x, y, color);
    }

    public void drawStringWithShadow(MatrixStack matrixStack, String text, double x, double y, int color) {
        StyledFontRenderer.drawShadowedString(matrixStack, this, text, x, y, color);
    }

    public void drawCenteredString(MatrixStack matrixStack, String text, double x, double y, int color) {
        StyledFontRenderer.drawCenteredXString(matrixStack, this, text, x, y, color);
    }
    public void drawCenteredString(String text, double x, double y, int color) {
        StyledFontRenderer.drawCenteredXString(new MatrixStack(), this, text, x, y, color);
    }

    public void drawCenteredString(MatrixStack matrixStack, ITextComponent text, double x, double y, int color) {
        StyledFontRenderer.drawCenteredString(matrixStack, this, text, x, y, color);
    }

    public void drawStringWithOutline(MatrixStack stack, String text, double x, double y, int color) {
        Color c = new Color(0, 0, 0, 128);
        x = MathUtility.round(x, 0.5F);
        y = MathUtility.round(y, 0.5F);
        StyledFontRenderer.drawString(stack, this, text, x - 0.5, y, c.getRGB());
        StyledFontRenderer.drawString(stack, this, text, x + 0.5, y, c.getRGB());
        StyledFontRenderer.drawString(stack, this, text, x, y - 0.5f, c.getRGB());
        StyledFontRenderer.drawString(stack, this, text, x, y + 0.5f, c.getRGB());

        drawString(stack, text, x, y, color);
    }

    public void drawCenteredStringWithOutline(MatrixStack stack, String text, double x, double y, int color) {
        drawStringWithOutline(stack, text, x - getWidth(text) / 2F, y, color);
    }

    public float getWidth(String text) {
        float width = 0.0f;

        for (int i = 0; i < text.length(); i++) {
            char c0 = text.charAt(i);
            if (c0 == 167 && i + 1 < text.length() &&
                    StyledFontRenderer.STYLE_CODES.indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1)) != -1) {
                i++;
            } else {
                width += getGlyphPage().getWidth(c0) + regular.getSpacing();
            }
        }

        return (width - regular.getSpacing()) / 2.0f;
    }
    public float getStringWidth(String text) {
        float width = 0.0f;

        for (int i = 0; i < text.length(); i++) {
            char c0 = text.charAt(i);
            if (c0 == 167 && i + 1 < text.length() &&
                    StyledFontRenderer.STYLE_CODES.indexOf(text.toLowerCase(Locale.ENGLISH).charAt(i + 1)) != -1) {
                i++;
            } else {
                width += getGlyphPage().getWidth(c0) + regular.getSpacing();
            }
        }

        return (width - regular.getSpacing()) / 2.0f;
    }

    private GlyphPage getGlyphPage() {
        return regular;
    }


    public int getFontHeight() {
        return (int) ((this.regular.getFontHeight() - 2));
    }
    public float getLifting() {
        return regular.getLifting();
    }



}
