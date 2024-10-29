package sertyo.events.manager.theme;

import sertyo.events.Main;
import sertyo.events.utility.render.ColorUtil;

import java.awt.*;

public class Theme {
   private final String name;
   private final ThemeType type;
   private final Color[] colors;

   public Theme(String name, ThemeType type, Color... colors) {
      this.name = name;
      this.type = type;
      this.colors = colors;
   }


   public String getName() {
      return this.name;
   }

   public ThemeType getType() {
      return this.type;
   }

   public Color[] getColors() {
      return this.colors;
   }

   public static enum ThemeType {
      GUI,
      STYLE;
   }
}
