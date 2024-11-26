//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.csgui.component.impl;

import sertyo.events.Main;
import sertyo.events.manager.theme.Theme;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.render.RenderUtility;

public class ThemeComponent extends Component {
   private final Theme theme;

   public ThemeComponent(Theme theme, float width, float height) {
      super(0.0F, 0.0F, width, height);
      this.theme = theme;
   }

   public void render(int mouseX, int mouseY) {
      if (Main.getInstance().getThemeManager().getCurrentStyleTheme().equals(this.theme)) {
         RenderUtility.drawRoundedRect(this.x, this.y, this.width, this.height, 8.0F, -1);
      }

      RenderUtility.drawRoundedGradientRect(this.x + 1.0F, this.y + 1.0F, this.width - 2.0F, this.height - 2.0F, 7.0F, 1.0F, this.theme.getColors()[0].getRGB(), this.theme.getColors()[0].getRGB(), this.theme.getColors()[1].getRGB(), this.theme.getColors()[1].getRGB());
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      if (RenderUtility.isHovered(mouseX, mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height)) {
         Main.getInstance().getThemeManager().setCurrentStyleTheme(this.theme);
      }

   }

   public Theme getTheme() {
      return this.theme;
   }
}
