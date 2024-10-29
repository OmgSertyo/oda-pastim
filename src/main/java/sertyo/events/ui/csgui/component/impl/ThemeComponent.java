//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.csgui.component.impl;

import net.minecraft.util.math.vector.Vector4f;
import sertyo.events.Main;
import sertyo.events.manager.theme.Theme;
import sertyo.events.manager.theme.Themes;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.font.Fonts;

import java.awt.*;

public class ThemeComponent extends Component {
   private final Theme theme;

   public ThemeComponent(Theme theme, float width, float height) {
      super(0.0F, 0.0F, width, height);
      this.theme = theme;
   }
   float[] radius = new float[]{4.0F, 4.0F, 0.0F, 0.0F}; // радиус для верхних углов 4, для нижних 0

   public void render(int mouseX, int mouseY) {
      boolean isDark = Main.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      if (Main.getInstance().getThemeManager().getCurrentStyleTheme().equals(this.theme) || Main.getInstance().getThemeManager().getCurrentGuiTheme().equals(this.theme)) {
         RenderUtil.Render2D.drawRoundedRect(this.x - 1.0F, this.y - 1.0F, this.width + 2.0F, this.height + 2.0F, 4, isDark ? Color.WHITE.getRGB() : (new Color(85, 85, 85)).getRGB());
      }

      RenderUtil.Render2D.drawRoundedRect(this.x, this.y, this.width, this.height, 3.5f, isDark ? (new Color(34, 34, 34)).getRGB() : (new Color(210, 210, 210)).getRGB());
      if (this.theme.getType().equals(Theme.ThemeType.GUI)) {
         RenderUtil.Render2D.drawRoundedGradientRect(this.x, this.y, this.width, this.height - 12.0F, 7.0F, 0.0F, 7.0F, 0.0F, 1.0F, this.theme.getColors()[0].getRGB(), this.theme.getColors()[0].getRGB(), this.theme.getColors()[0].getRGB(), this.theme.getColors()[0].getRGB());
      } else {
         RenderUtil.Render2D.drawRoundedGradientRect(this.x, this.y, this.width, this.height - 12.0F, 7.0F, 0.0F, 7.0F, 0.0F, 1.0F, this.theme.getColors()[0].getRGB(), this.theme.getColors()[0].getRGB(), this.theme.getColors()[1].getRGB(), this.theme.getColors()[1].getRGB());
      }

      Fonts.msBold[14].drawCenteredString(this.theme.getName(), this.x + this.width / 2.0F, this.y + 22.0F, isDark ? Color.WHITE.getRGB() : (new Color(65, 65, 65)).getRGB());
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      if (RenderUtil.isHovered(mouseX, mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height)) {
         if (this.theme.getType().equals(Theme.ThemeType.GUI)) {
            Main.getInstance().getThemeManager().setCurrentGuiTheme(this.theme);
         } else {
            Main.getInstance().getThemeManager().setCurrentStyleTheme(this.theme);
         }
      }

   }

   public Theme getTheme() {
      return this.theme;
   }
}
