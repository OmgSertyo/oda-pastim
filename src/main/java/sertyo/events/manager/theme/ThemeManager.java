package sertyo.events.manager.theme;

import sertyo.events.Main;
import sertyo.events.utility.render.ColorUtil;

import java.awt.*;

import static sertyo.events.utility.render.ColorUtil.gradient;

public class ThemeManager {
   private Theme currentGuiTheme;
   private Theme currentStyleTheme;

   public ThemeManager() {
      this.currentGuiTheme = Themes.DARK.getTheme();
      this.currentStyleTheme = Themes.LOLLIPOP.getTheme();
   }


   public Theme getCurrentGuiTheme() {
      return this.currentGuiTheme;
   }

   public void setCurrentGuiTheme(Theme currentGuiTheme) {
      this.currentGuiTheme = currentGuiTheme;
   }

   public Theme getCurrentStyleTheme() {
      return this.currentStyleTheme;
   }

   public void setCurrentStyleTheme(Theme currentStyleTheme) {
      this.currentStyleTheme = currentStyleTheme;
   }
}
