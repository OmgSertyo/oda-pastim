package sertyo.events.ui.csgui.component.impl;


import java.awt.Color;

import net.minecraft.util.math.MathHelper;
import sertyo.events.Main;
import sertyo.events.manager.theme.Themes;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.math.MathUtility;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.AnimationMath;
import sertyo.events.utility.font.Fonts;

public class SliderComponent extends Component {
   public ModuleComponent moduleComponent;
   public NumberSetting setting;
   public float animation = 0.0F;
   public boolean isDragging;

   public SliderComponent(ModuleComponent moduleComponent, NumberSetting setting) {
      super(0.0F, 0.0F, 0.0F, 19.0F);
      this.moduleComponent = moduleComponent;
      this.setting = setting;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      int color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0].getRGB();
      int color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1].getRGB();
      boolean isDark = Main.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      Fonts.msBold[14].drawString(this.setting.getName(), this.x + 5.0F, this.y + 3.0F, isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
      RenderUtil.Render2D.drawRect(this.x + 5.0F, this.y + 12.0F, this.width - 10.0F, 3.5F, isDark ? (new Color(25, 25, 25)).getRGB() : (new Color(160, 160, 160)).getRGB());
      float sliderWidth = (float)(((double)this.setting.get() - this.setting.getMinValue()) / (this.setting.getMaxValue() - this.setting.getMinValue()) * (double)(this.width - 10.0F));
      this.animation = AnimationMath.fast(this.animation, sliderWidth, 15.0F);
      RenderUtil.Render2D.drawGradientRound(this.x + 5.0F, this.y + 12.0F, this.animation, 3.5F, color, color2, color2, color, 0);
      RenderUtil.Render2D.drawShadow(this.x + this.animation + 3.5F, this.y + 10.5F, 2.0F, 7.0F, 3, Color.BLACK.getRGB());
      RenderUtil.Render2D.drawRect(this.x + this.animation + 3.0F, this.y + 10.0F, 2.5F, 7.5F, -1);
      if (this.isDragging) {
         float sliderValue = (float)MathHelper.clamp(MathUtility.round((double)((float)((double)(((float)mouseX - this.x - 5.0F) / (this.width - 10.0F)) * (this.setting.getMaxValue() - this.setting.getMinValue()) + this.setting.getMinValue())), this.setting.getIncrement()), this.setting.getMinValue(), this.setting.getMaxValue());
         this.setting.set(sliderValue);
      }

      Fonts.msBold[14].drawString(String.valueOf(this.setting.get()), this.x + this.width - 7.0F - (float)Fonts.msBold[12].getWidth(String.valueOf(this.setting.get())), this.y + 3.0F, isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      boolean isHovered = RenderUtil.isHovered(mouseX, mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height);
      if (isHovered && mouseButton == 0) {
         this.isDragging = true;  // Начать перетаскивание, если мышь наведена и нажата левая кнопка
      }  else {
         this.isDragging = false; // Остановить перетаскивание, если левая кнопка отпущена
      }


   }

   public boolean isVisible() {
      return (Boolean)this.setting.getVisible().get();
   }
}
