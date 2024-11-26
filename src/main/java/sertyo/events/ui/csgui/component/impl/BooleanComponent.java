package sertyo.events.ui.csgui.component.impl;

import sertyo.events.Main;
import sertyo.events.manager.theme.Themes;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.ColorUtility;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.RenderUtility;
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
      this.animation = AnimationMath.fast(this.animation, this.setting.state ? -1.0F : 0.0F, 15.0F);
      Fonts.msBold[14].drawString(this.setting.getName(), this.x + 5.0F, this.y + 5.5F, (new Color(149, 149, 161)).getRGB());
      Color glowColor = ColorUtility.applyOpacity(Color.BLACK, 0.2F);
      RenderUtility.drawShadow(this.x + this.width - 25.0F, this.y + 2.0F, 17.5F, 10.0F, 5, glowColor, glowColor);
      RenderUtility.drawRoundedRect(this.x + this.width - 25.0F, this.y + 2.0F, 17.5F, 10.0F, 8.0F, Color.decode("#2B2C44").getRGB());
      Color c = ColorUtility.interpolateColorC((new Color(78, 79, 98)).getRGB(), (new Color(202, 202, 208)).getRGB(), Math.abs(this.animation));
      RenderUtility.drawRoundedRect(this.x + this.width - 23.0F - this.animation * 7.0F, this.y + 4.0F, 6.0F, 6.0F, 5.0F, c.getRGB());
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
