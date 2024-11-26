//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.csgui.component.impl;


import java.awt.Color;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.math.MathUtility;
import sertyo.events.utility.render.RenderUtility;
import sertyo.events.utility.render.animation.AnimationMath;
import sertyo.events.utility.render.fonts.Fonts;

public class SliderComponent extends Component {
   public ModuleComponent moduleComponent;
   public NumberSetting setting;
   public float animation = 0.0F;
   public boolean isDragging;

   public SliderComponent(ModuleComponent moduleComponent, NumberSetting setting) {
      super(0.0F, 0.0F, 0.0F, 14.0F);
      this.moduleComponent = moduleComponent;
      this.setting = setting;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      sertyo.events.utility.font.Fonts.msBold[14].drawString(this.setting.getName(), this.x + 5.0F, this.y + 5.5F, (new Color(149, 149, 161)).getRGB());
      int textWidth = Fonts.mntssb14.getStringWidth(this.setting.getName());
      RenderUtility.drawRoundedRect(this.x + 64.0F, this.y + 6.0F, 60.0F, 2.5F, 1.0F, Color.decode("#2B2C44").getRGB());
      float sliderWidth = (float)(((double)this.setting.get() - this.setting.getMinValue()) / (this.setting.getMaxValue() - this.setting.getMinValue()) * 60.0);
      this.animation = AnimationMath.fast(this.animation, sliderWidth, 15.0F);
      RenderUtility.drawRoundedRect(this.x + 64.0F + this.animation - 2.0F, this.y + 5.5F, 4.0F, 4.0F, 3.0F, -1);
      if (this.isDragging) {
         if (!(GLFW.glfwGetMouseButton(GLFW.glfwGetCurrentContext(), GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS)) {
            this.isDragging = false;
         }

         float sliderValue = (float)MathHelper.clamp(MathUtility.round((double)((float)((double)(((float)mouseX - this.x - 64.0F) / 60.0F) * (this.setting.getMaxValue() - this.setting.getMinValue()) + this.setting.getMinValue())), this.setting.getIncrement()), this.setting.getMinValue(), this.setting.getMaxValue());
         this.setting.set(sliderValue);
      }

      RenderUtility.drawRoundedRect(this.x + this.width - 9.0F - (float)Fonts.mntssb14.getStringWidth(String.valueOf(this.setting.get())), this.y + 3.0F, (float)(Fonts.mntssb14.getStringWidth(String.valueOf(this.setting.get())) + 4), (float)(Fonts.mntssb14.getFontHeight() + 4), 3.0F, Color.decode("#2B2C44").getRGB());
      sertyo.events.utility.font.Fonts.msBold[14].drawString(String.valueOf(this.setting.get()), this.x + this.width - 7.0F - (float)Fonts.mntssb14.getStringWidth(String.valueOf(this.setting.get())), this.y + 5.5F, -1);
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      boolean isHovered = RenderUtility.isHovered(mouseX, mouseY, (double)this.x, (double)this.y, (double)this.width, (double)this.height);
      if (isHovered && mouseButton == 0) {
         this.isDragging = true;
      }

   }

   public boolean isVisible() {
      return (Boolean)this.setting.getVisible().get();
   }
}
