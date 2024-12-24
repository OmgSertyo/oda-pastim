package sertyo.events.utility.render;



import java.awt.Color;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import org.lwjgl.opengl.GL11;
import sertyo.events.Main;
import sertyo.events.module.impl.render.Arraylist;
import sertyo.events.utility.Utility;

public class ColorUtility implements Utility {
   public static float[] getRGBAf(int color) {
      return new float[]{(float)(color >> 16 & 255) / 255.0F, (float)(color >> 8 & 255) / 255.0F, (float)(color & 255) / 255.0F, (float)(color >> 24 & 255) / 255.0F};
   }
   public static int rgb(int r, int g, int b) {
      return 255 << 24 | r << 16 | g << 8 | b;
   }

   public static int rgba(int r, int g, int b, int a) {
      return a << 24 | r << 16 | g << 8 | b;
   }
   public static float[] rgba(final int color) {
      return new float[] {
              (color >> 16 & 0xFF) / 255f,
              (color >> 8 & 0xFF) / 255f,
              (color & 0xFF) / 255f,
              (color >> 24 & 0xFF) / 255f
      };
   }

   public static void setColor(Color color) {
      if (color == null) {
         color = Color.WHITE;
      }

      setColor((double)((float)color.getRed() / 255.0F), (double)((float)color.getGreen() / 255.0F), (double)((float)color.getBlue() / 255.0F), (double)((float)color.getAlpha() / 255.0F));
   }

   public static int setAlpha(int color, int alpha) {
      Color c = new Color(color);
      return (new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha)).getRGB();
   }

   public static int setAlpha(int color, float alpha) {
      Color c = new Color(color);
      return (new Color((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, alpha)).getRGB();
   }

   public static void setColor(double red, double green, double blue, double alpha) {
      GL11.glColor4d(red, green, blue, alpha);
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

   public static int getRed(int hex) {
      return hex >> 16 & 255;
   }

   public static int getGreen(int hex) {
      return hex >> 8 & 255;
   }

   public static int getBlue(int hex) {
      return hex & 255;
   }

   public static int getAlpha(int hex) {
      return hex >> 24 & 255;
   }

   public static Double interpolate(double oldValue, double newValue, double interpolationValue) {
      return oldValue + (newValue - oldValue) * interpolationValue;
   }
   public static StringTextComponent gradient(String message, int first, int end) {

      StringTextComponent text = new StringTextComponent("");
      for (int i = 0; i < message.length(); i++) {
         text.append(new StringTextComponent(String.valueOf(message.charAt(i))).setStyle(Style.EMPTY.setColor(new net.minecraft.util.text.Color(interpolateInt(first, end, (float) i / message.length())))));
      }

      return text;

   }
   public static Color applyOpacity(Color color, float opacity) {
      opacity = Math.min(1.0F, Math.max(0.0F, opacity));
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * opacity));
   }
   public static int applyOpacity2(Color color, float opacity) {
      opacity = Math.min(1.0F, Math.max(0.0F, opacity));
      return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)((float)color.getAlpha() * opacity)).getRGB();
   }

   public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
      return interpolate((double)oldValue, (double)newValue, (double)((float)interpolationValue)).intValue();
   }

   public static Color interpolateColorC(int color1, int color2, float amount) {
      amount = Math.min(1.0F, Math.max(0.0F, amount));
      return new Color(interpolateInt(getRed(color1), getRed(color2), (double)amount), interpolateInt(getGreen(color1), getGreen(color2), (double)amount), interpolateInt(getBlue(color1), getBlue(color2), (double)amount), interpolateInt(getAlpha(color1), getAlpha(color2), (double)amount));
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

   public static Color fade(int speed, int index, Color color, float alpha) {
      float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), (float[])null);
      int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
      angle = (angle > 180 ? 360 - angle : angle) + 180;
      Color colorHSB = new Color(Color.HSBtoRGB(hsb[0], hsb[1], (float)angle / 360.0F));
      return new Color(colorHSB.getRed(), colorHSB.getGreen(), colorHSB.getBlue(), Math.max(0, Math.min(255, (int)(alpha * 255.0F))));
   }

   public static int HUEtoRGB(int value) {
      float hue = (float)value / 360.0F;
      return Color.HSBtoRGB(hue, 1.0F, 1.0F);
   }

   public static int getColor(int index) {
      Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
      Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      return gradient((int)(10.0F - Arraylist.colorSpeed.get()), index, color, color2).getRGB();
   }
   public static int getColor3(int index, float aPC) {
      return ColorUtility.swapAlpha(Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[index].getRGB(), (float)ColorUtility.getAlphaFromColor(Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[index].getRGB()) * aPC);
   }
   public static int getColor1() {
      return Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0].getRGB();
   }
   public static float getBrightnessFromColor(int color) {
      float[] athsb = Color.RGBtoHSB(getRedFromColor(color), getGreenFromColor(color), getBlueFromColor(color), (float[])null);
      return athsb[2];
   }
   public static int swapDark(int color, float dark) {
      int f = color >> 16 & 255;
      int f1 = color >> 8 & 255;
      int f2 = color & 255;
      return getColor((int)((float)f * dark), (int)((float)f1 * dark), (int)((float)f2 * dark));
   }
   public static int getColor(int red, int green, int blue) {
      return getColor(red, green, blue, 255);
   }
   public static int getRedFromColor(int color) {
      return color >> 16 & 255;
   }

   public static int getGreenFromColor(int color) {
      return color >> 8 & 255;
   }

   public static int getBlueFromColor(int color) {
      return color & 255;
   }
   public static int getOverallColorFrom(int color1, int color2) {
      int red1 = getRedFromColor(color1);
      int green1 = getGreenFromColor(color1);
      int blue1 = getBlueFromColor(color1);
      int alpha1 = getAlphaFromColor(color1);
      int red2 = getRedFromColor(color2);
      int green2 = getGreenFromColor(color2);
      int blue2 = getBlueFromColor(color2);
      int alpha2 = getAlphaFromColor(color2);
      int finalRed = (red1 + red2) / 2;
      int finalGreen = (green1 + green2) / 2;
      int finalBlue = (blue1 + blue2) / 2;
      int finalAlpha = (alpha1 + alpha2) / 2;
      return getColor(finalRed, finalGreen, finalBlue, finalAlpha);
   }

   public static int getOverallColorFrom(int color1, int color2, float percentTo2) {
      int red1 = getRedFromColor(color1);
      int green1 = getGreenFromColor(color1);
      int blue1 = getBlueFromColor(color1);
      int alpha1 = getAlphaFromColor(color1);
      int red2 = getRedFromColor(color2);
      int green2 = getGreenFromColor(color2);
      int blue2 = getBlueFromColor(color2);
      int alpha2 = getAlphaFromColor(color2);
      int finalRed = (int)((float)red1 * (1.0F - percentTo2) + (float)red2 * percentTo2);
      int finalGreen = (int)((float)green1 * (1.0F - percentTo2) + (float)green2 * percentTo2);
      int finalBlue = (int)((float)blue1 * (1.0F - percentTo2) + (float)blue2 * percentTo2);
      int finalAlpha = (int)((float)alpha1 * (1.0F - percentTo2) + (float)alpha2 * percentTo2);
      return getColor(finalRed, finalGreen, finalBlue, finalAlpha);
   }
   public static int getColor2() {
      return Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1].getRGB();
   }
   public static int getAlphaFromColor(int color) {
      return color >> 24 & 255;
   }

   public static String getHealthStr(LivingEntity entity) {
      String str = "";
      int health = (int)entity.getHealth();
      if ((double)health <= (double)entity.getMaxHealth() * 0.25D) {
         str = "§4";
      } else if ((double)health <= (double)entity.getMaxHealth() * 0.5D) {
         str = "§6";
      } else if ((double)health <= (double)entity.getMaxHealth() * 0.75D) {
         str = "§e";
      } else if ((float)health <= entity.getMaxHealth()) {
         str = "§a";
      }

      return str;
   }
   public static int getColor(int red, int green, int blue, int alpha) {
      int color = 0;
      color |= alpha << 24;
      color |= red << 16;
      color |= green << 8;
      return color | blue;
   }
   public static int swapAlpha(int color, float alpha) {
      int f = color >> 16 & 255;
      int f1 = color >> 8 & 255;
      int f2 = color & 255;
      return getColor(f, f1, f2, (int)alpha);
   }
}
