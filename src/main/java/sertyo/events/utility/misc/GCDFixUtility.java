package sertyo.events.utility.misc;


import static sertyo.events.utility.Utility.mc;

public class GCDFixUtility {
   public static float getFixedRotation(float rot) {
      return getDeltaMouse(rot) * getGCDValue();
   }

   public static float getGCDValue() {
      return (float)((double)getGCD() * 0.15D);
   }

   public static float getGCD() {
      float f1;
      return (f1 = (float)((double)mc.gameSettings.mouseSensitivity * 0.6D + 0.2D)) * f1 * f1 * 8.0F;
   }

   public static float getDeltaMouse(float delta) {
      return (float)Math.round(delta / getGCDValue());
   }
}
