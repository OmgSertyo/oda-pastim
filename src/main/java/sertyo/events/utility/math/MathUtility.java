package sertyo.events.utility.math;

import sertyo.events.utility.Utility;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MathUtility implements Utility {
   public static float clamp(float val, float min, float max) {
      if (val <= min) {
         val = min;
      }

      if (val >= max) {
         val = max;
      }

      return val;
   }
   public static boolean isInRegion(final int mouseX,
                                    final int mouseY,
                                    final int x,
                                    final int y,
                                    final int width,
                                    final int height) {
      return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
   }

   public static boolean isInRegion(final double mouseX,
                                    final double mouseY,
                                    final float x,
                                    final float y,
                                    final float width,
                                    final float height) {
      return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
   }

   public static boolean isInRegion(final double mouseX,
                                    final double mouseY,
                                    final int x,
                                    final int y,
                                    final int width,
                                    final int height) {
      return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
   }
   public static float randomFloat() {
      Random random = new Random();
      return random.nextFloat();
   }
   public static double round(double num, double increment) {
      double v = (double)Math.round(num / increment) * increment;
      BigDecimal bd = new BigDecimal(v);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }

   public static float randomizeFloat(float min, float max) {
      return (float)(Math.random() * (double)(max - min)) + min;
   }

   public static double getDistance(double x1, double y1, double x2, double y2) {
      return Math.sqrt(Math.pow(x2 - x1, 2.0D) + Math.pow(y2 - y1, 2.0D));
   }

   public static float lerp(float a, float b, float f) {
      return a + f * (b - a);
   }

   public static int getMiddle(int old, int newValue) {
      return (old + newValue) / 2;
   }

   public static int getRandomInRange(int int1, int int2) {
      return (int)(Math.random() * (double)(int2 - int1) + (double)int1);
   }

   public static double getDifferenceOf(float num1, float num2) {
      return Math.abs(num2 - num1) > Math.abs(num1 - num2) ? (double)Math.abs(num1 - num2) : (double)Math.abs(num2 - num1);
   }

   public static double getDifferenceOf(double num1, double num2) {
      return Math.abs(num2 - num1) > Math.abs(num1 - num2) ? Math.abs(num1 - num2) : Math.abs(num2 - num1);
   }

   public static double getDifferenceOf(int num1, int num2) {
      return Math.abs(num2 - num1) > Math.abs(num1 - num2) ? (double)Math.abs(num1 - num2) : (double)Math.abs(num2 - num1);
   }

   public static float harp(float val, float current, float speed) {
      return (float)harpD((double)val, (double)current, (double)speed);
   }

   public static double harpD(double val, double current, double speed) {
      double emi = (current - val) * (speed / 2.0) > 0.0 ? Math.max(speed, Math.min(current - val, (current - val) * (speed / 2.0))) : Math.max(current - val, Math.min(-(speed / 2.0), (current - val) * (speed / 2.0)));
      return val + emi;
   }

   public static double interpolate(double current, double old, double scale) {
      return old + (current - old) * scale;
   }
}
