package sertyo.events.utility.render.fonts;

import java.awt.Font;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Fonts {
   public static FontRenderer icons21 = new FontRenderer(getFontFromTTF("icons", 21.0F, 0), true, true);
   public static FontRenderer icons41 = new FontRenderer(getFontFromTTF("icons", 41.0F, 0), true, true);
   public static FontRenderer nunitoBold14 = new FontRenderer(getFontFromTTF("nunito-bold", 14.0F, 0), true, true);
   public static FontRenderer nunitoBold15 = new FontRenderer(getFontFromTTF("nunito-bold", 15.0F, 0), true, true);
   public static FontRenderer nunitoBold16 = new FontRenderer(getFontFromTTF("nunito-bold", 16.0F, 0), true, true);
   public static FontRenderer nunitoBold18 = new FontRenderer(getFontFromTTF("nunito-bold", 18.0F, 0), true, true);
   public static FontRenderer mntsb16 = new FontRenderer(getFontFromTTF("mntsb", 16.0F, 0), true, true);
   public static FontRenderer mntssb16 = new FontRenderer(getFontFromTTF("mntssb", 16.0F, 0), true, true);
   public static FontRenderer mntssb15 = new FontRenderer(getFontFromTTF("mntssb", 15.0F, 0), true, true);
   public static FontRenderer mntssb14 = new FontRenderer(getFontFromTTF("mntssb", 14.0F, 0), true, true);
   public static FontRenderer mntsb13 = new FontRenderer(getFontFromTTF("mntsb", 13.0F, 0), true, true);
   public static FontRenderer tenacityBold28 = new FontRenderer(getFontFromTTF("tenacity-bold", 28.0F, 0), true, true);
   public static FontRenderer tenacityBold35 = new FontRenderer(getFontFromTTF("tenacity-bold", 35.0F, 0), true, true);
   public static FontRenderer tenacityBold18 = new FontRenderer(getFontFromTTF("tenacity-bold", 18.0F, 0), true, true);
   public static FontRenderer tenacityBold12 = new FontRenderer(getFontFromTTF("tenacity-bold", 12.0F, 0), true, true);
   public static FontRenderer ace16 = new FontRenderer(getFontFromTTF("ace", 16.0F, 0), true, true);
   public static FontRenderer hack16 = new FontRenderer(getFontFromTTF("hack", 16.0F, 0), true, true);
   public static FontRenderer minecraft13 = new FontRenderer(getFontFromTTF("minecraft", 14.0F, 0), true, true);
   public static FontRenderer mntsb17 = new FontRenderer(getFontFromTTF("mntsb", 17.0F, 0), true, true);
   public static FontRenderer nunito14 = new FontRenderer(getFontFromTTF("nunito", 14.0F, 0), true, true);
   public static FontRenderer nunito12 = new FontRenderer(getFontFromTTF("nunito", 12.0F, 0), true, true);
   public static FontRenderer icons16 = new FontRenderer(getFontFromTTF("icons", 16.0F, 0), true, true);

   public static Font getFontFromTTF(String name, float fontSize, int fontType) {
      Font output = null;

      try {
         output = Font.createFont(fontType, Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation("font/" + name + ".ttf")).getInputStream());
         output = output.deriveFont(fontSize);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

      return output;
   }
}
