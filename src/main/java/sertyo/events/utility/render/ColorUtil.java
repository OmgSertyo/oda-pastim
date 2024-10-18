package sertyo.events.utility.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import org.lwjgl.opengl.GL11;
import sertyo.events.Main;
import sertyo.events.utility.Utility;
import sertyo.events.utility.math.MathUtility;

import java.awt.*;

import static sertyo.events.module.impl.render.Arraylist.colorSpeed;
import static sertyo.events.utility.render.RenderUtil.reAlphaInt;


/**
 * @author dedinside
 * @since 11.06.2023
 */

public class ColorUtil implements Utility {
    public static float[] getRGBAf(int color) {
        return new float[]{(float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, (float)(color >> 24 & 255) / 255.0F};
    }
    public static Color applyOpacity(Color color, float opacity) {
        opacity = Math.min(1.0F, Math.max(0.0F, opacity));
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * opacity));
    }
    public static final int green = ColorUtil.rgba(36, 218, 118, 255);
    public static final int yellow = ColorUtil.rgba(255, 196, 67, 255);
    public static final int orange = ColorUtil.rgba(255, 134, 0, 255);
    public static final int red = ColorUtil.rgba(239, 72, 54, 255);
    public static Color injectAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    public static int setAlpha(int color, int alpha) {
        Color c = new Color(color);
        return (new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha)).getRGB();
    }
    public static Color interpolateTwoColors(int speed, int index, Color start, Color end, boolean trueColor) {
        int angle = 0;
        if (speed == 0) {
            angle = index % 360;
        } else {
            angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
        }
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        boolean tur = trueColor;
        return tur ? interpolateColorHue(start, end, angle / 360f) : interpolateColorC(start, end, angle / 360f);
    }
    public static Color interpolateColorC(int color1, int color2, float amount) {
        amount = Math.min(1.0F, Math.max(0.0F, amount));
        return new Color(interpolateInt(getRed(color1), getRed(color2), (double)amount), interpolateInt(getGreen(color1), getGreen(color2), (double)amount), interpolateInt(getBlue(color1), getBlue(color2), (double)amount), interpolateInt(getAlpha(color1), getAlpha(color2), (double)amount));
    }
    public static Color interpolateColorC(Color color1, Color color2, float amount) {
        //amount = Math.min(1, Math.max(0, amount));
        float zalupa = amount;
        return new Color(MathUtility.lerp(color1.getRed(), color2.getRed(), zalupa),
                MathUtility.lerp(color1.getGreen(), color2.getGreen(), zalupa),
                MathUtility.lerp(color1.getBlue(), color2.getBlue(), zalupa),
                MathUtility.lerp(color1.getAlpha(), color2.getAlpha(), zalupa));
    }

    public static Color interpolateColorHue(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));

        float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
        float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

        Color resultColor = Color.getHSBColor(MathUtility.lerp(color1HSB[0], color2HSB[0], amount),
                MathUtility.lerp(color1HSB[1], color2HSB[1], amount), MathUtility.lerp(color1HSB[2], color2HSB[2], amount));

        return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(),
                (int) MathUtility.lerp(color1.getAlpha(), color2.getAlpha(), amount));
    }
    public static Color interpolateTwoColors(int speed, int index, Color start, Color end) {
        return interpolateTwoColors(speed, index, start, end, false);
    }
    public static int setAlpha(int color, float alpha) {
        Color c = new Color(color);
        return (new Color((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, alpha)).getRGB();
    }
    public static Color injectAlpha(Color color, float alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255.0F));
    }
    public static void setAlphaColor(final int color, final float alpha) {
        final float red = (float) (color >> 16 & 255) / 255.0F;
        final float green = (float) (color >> 8 & 255) / 255.0F;
        final float blue = (float) (color & 255) / 255.0F;
        RenderSystem.color4f(red, green, blue, alpha);
    }
    public static void setColor(double red, double green, double blue, double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }
    public static void setColor(Color color) {
        if (color == null) {
            color = Color.WHITE;
        }

        setColor((double)((float)color.getRed() / 255.0F), (double)((float)color.getGreen() / 255.0F), (double)((float)color.getBlue() / 255.0F), (double)((float)color.getAlpha() / 255.0F));
    }

    public static void setColor(int color) {
        setColor(color, (float)(color >> 24 & 255) / 255.0F);
    }

    public static void setColor(int color, float alpha) {
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        GlStateManager.color((int) r, (int) g, (int) b, (int) alpha);
    }

    public static int astolfo(int speed, int offset, float saturation, float brightness, float alpha) {
        float hue = (float) calculateHueDegrees(speed, offset);
        hue = (float) ((double) hue % 360.0);
        float hueNormalized;
        return reAlphaInt(
                Color.HSBtoRGB((double) ((hueNormalized = hue % 360.0F) / 360.0F) < 0.5 ? -(hueNormalized / 360.0F) : hueNormalized / 360.0F, saturation, brightness),
                Math.max(0, Math.min(255, (int) (alpha * 255.0F)))
        );
    }

    private static int calculateHueDegrees(int divisor, int offset) {
        long currentTime = System.currentTimeMillis();
        long calculatedValue = (currentTime / divisor + offset) % 360L;
        return (int) calculatedValue;
    }



    public static int rgba(int r, int g, int b, int a) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int rgba(double r, double g, double b, double a) {
        return rgba((int) r, (int) g, (int) b, (int) a);
    }

    public static int getRed(final int hex) {
        return hex >> 16 & 255;
    }

    public static int getGreen(final int hex) {
        return hex >> 8 & 255;
    }

    public static int getBlue(final int hex) {
        return hex & 255;
    }

    public static int getAlpha(final int hex) {
        return hex >> 24 & 255;
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }

    public static int getColor(int index) {
            Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
            Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
            return ColorUtil.gradient((int)(16.0F - colorSpeed.get()), index * 2, new Color[]{color, color2}).getRGB();

    }
    public static int getColor(int red, int green, int blue) {
        return new Color(red,green,blue).getRGB();
    }
    public static StringTextComponent gradient(String message, int first, int end) {

        StringTextComponent text = new StringTextComponent("");
        for (int i = 0; i < message.length(); i++) {
            text.append(new StringTextComponent(String.valueOf(message.charAt(i))).setStyle(Style.EMPTY.setColor(new net.minecraft.util.text.Color(ColorUtil.interpolateColor(first, end, (float) i / message.length())))));
        }

        return text;

    }
    public static Color gradient(int speed, int index, Color... colors) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        int colorIndex = (int)((float)angle / 360.0F * (float)colors.length);
        if (colorIndex == colors.length) {
            --colorIndex;
        }

        Color color1 = colors[colorIndex];
        Color color2 = colors[colorIndex == colors.length - 1 ? 0 : colorIndex + 1];
        return interpolateColorC(color1.getRGB(), color2.getRGB(), (float)angle / 360.0F * (float)colors.length - (float)colorIndex);
    }
    //    gradient with more than two colors
    public static int gradient(int speed, int index, int... colors) {
        int angle = (int) ((System.currentTimeMillis() / speed + index) % 360);
        angle = (angle > 180 ? 360 - angle : angle) + 180;
        int colorIndex = (int) (angle / 360f * colors.length);
        if (colorIndex == colors.length) {
            colorIndex--;
        }
        int color1 = colors[colorIndex];
        int color2 = colors[colorIndex == colors.length - 1 ? 0 : colorIndex + 1];
        return interpolateColor(color1, color2, angle / 360f * colors.length - colorIndex);
    }


    public static int interpolateColor(int color1, int color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));

        int red1 = getRed(color1);
        int green1 = getGreen(color1);
        int blue1 = getBlue(color1);
        int alpha1 = getAlpha(color1);

        int red2 = getRed(color2);
        int green2 = getGreen(color2);
        int blue2 = getBlue(color2);
        int alpha2 = getAlpha(color2);

        int interpolatedRed = interpolateInt(red1, red2, amount);
        int interpolatedGreen = interpolateInt(green1, green2, amount);
        int interpolatedBlue = interpolateInt(blue1, blue2, amount);
        int interpolatedAlpha = interpolateInt(alpha1, alpha2, amount);

        return (interpolatedAlpha << 24) | (interpolatedRed << 16) | (interpolatedGreen << 8) | interpolatedBlue;
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }
    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r, g, b;

        if (saturation == 0) {
            int value = (int) (brightness * 255.0f + 0.5f);
            return 0xff000000 | (value << 16) | (value << 8) | value;
        }

        float h = (hue - (float) Math.floor(hue)) * 6.0f;
        float f = h - (float) Math.floor(h);
        float p = brightness * (1.0f - saturation);
        float q = brightness * (1.0f - saturation * f);
        float t = brightness * (1.0f - (saturation * (1.0f - f)));

        switch ((int) h) {
            case 0:
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (t * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 1:
                r = (int) (q * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (p * 255.0f + 0.5f);
                break;
            case 2:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (brightness * 255.0f + 0.5f);
                b = (int) (t * 255.0f + 0.5f);
                break;
            case 3:
                r = (int) (p * 255.0f + 0.5f);
                g = (int) (q * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
                break;
            case 4:
                r = (int) (t * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (brightness * 255.0f + 0.5f);
                break;
            case 5:
                r = (int) (brightness * 255.0f + 0.5f);
                g = (int) (p * 255.0f + 0.5f);
                b = (int) (q * 255.0f + 0.5f);
                break;
            default:
                throw new IllegalArgumentException("Invalid hue value");
        }

        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    public static int HUEtoRGB(int value) {
        float hue = (float)value / 360.0F;
        return Color.HSBtoRGB(hue, 1.0F, 1.0F);
    }
}