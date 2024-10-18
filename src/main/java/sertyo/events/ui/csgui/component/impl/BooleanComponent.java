package sertyo.events.ui.csgui.component.impl;

import sertyo.events.Main;
import sertyo.events.manager.theme.Themes;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.AnimationMath;
import sertyo.events.utility.font.Fonts;

import java.awt.Color;

public class BooleanComponent extends Component {
   public ModuleComponent moduleComponent;
   public BooleanSetting setting;
   public float animation = 0.0F;

   public BooleanComponent(ModuleComponent moduleComponent, BooleanSetting setting) {
      super(0.0F, 0.0F, 0.0F, 14.0F);
      this.moduleComponent = moduleComponent;
      this.setting = setting;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      boolean isDark = Main.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      this.animation = AnimationMath.fast(this.animation, this.setting.state ? -1.0F : 0.0F, 15.0F);
      Fonts.msBold[14].drawString(this.setting.getName(), this.x + 5.0F, this.y + 5.0F, isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
      RenderUtil.Render2D.drawRoundedRect(this.x + this.width - 25.0F, this.y + 2.0F, 20.0F, 10.0F, 6.0F, isDark ? (new Color(25, 25, 25)).getRGB() : (new Color(160, 160, 160)).getRGB());
      Color c = ColorUtil.interpolateColorC(isDark ? (new Color(34, 34, 34)).getRGB() : Color.WHITE.getRGB(), isDark ? Color.WHITE.getRGB() : (new Color(100, 100, 100)).getRGB(), Math.abs(this.animation));
      RenderUtil.Render2D.drawRoundedRect(this.x + this.width - 23.5F - this.animation * 10.0F, this.y + 3.5F, 7.0F, 7.0F, 6.0F, c.getRGB());
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      boolean isHovered = RenderUtil.isHovered(mouseX, mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height);
      if (isHovered && mouseButton == 0) {
         this.setting.state = !this.setting.get();
      }

   }

   public boolean isVisible() {
      return (Boolean)this.setting.getVisible().get();
   }
}
