package sertyo.events.manager.theme;

import sertyo.events.Main;

import java.awt.*;

import static sertyo.events.utility.render.ColorUtil.gradient;

public class ThemeManager {
   private Theme currentGuiTheme;
   private Theme currentStyleTheme;

   public ThemeManager() {
      this.currentGuiTheme = Themes.DARK.getTheme();
      this.currentStyleTheme = Themes.LOLLIPOP.getTheme();
   }
   public static Color getColor(int index) {
      Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
      Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      return gradient((int)(10.0F - 6), index, color, color2);
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
