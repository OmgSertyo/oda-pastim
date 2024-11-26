package sertyo.events.module.impl.render;



import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.ColorSetting;
import sertyo.events.module.setting.impl.ModeSetting;

import java.awt.Color;

@ModuleAnnotation(
   name = "Glint",
   category = Category.RENDER
)
public class Glint extends Module {
   public static ModeSetting mode = new ModeSetting("Color Mode", "Theme", new String[]{"Theme", "Custom"});
   public static ColorSetting color = new ColorSetting("Color", (new Color(68, 205, 205)).getRGB(), () -> {
      return mode.get().equals("Custom");
   });

   public static Color getColor() {
      Color customColor = Color.WHITE;
      String var1 = mode.get();
      byte var2 = -1;
      switch(var1.hashCode()) {
      case 80774569:
         if (var1.equals("Theme")) {
            var2 = 0;
         }
         break;
      case 2029746065:
         if (var1.equals("Custom")) {
            var2 = 1;
         }
      }

      switch(var2) {
      case 0:
         customColor = Arraylist.getArrayColor(1);
         break;
      case 1:
         customColor = color.getColor();
      }

      return customColor;
   }
}
