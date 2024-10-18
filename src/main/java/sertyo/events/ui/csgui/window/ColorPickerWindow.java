//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.csgui.window;



import net.java.games.input.Mouse;
import org.lwjgl.system.MathUtil;
import sertyo.events.module.setting.impl.ColorSetting;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.ui.csgui.CsGui;
import sertyo.events.utility.math.MathUtility;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.Animation;
import sertyo.events.utility.render.animation.Direction;
import sertyo.events.utility.render.animation.impl.EaseInOutQuad;

import java.awt.*;
public class ColorPickerWindow extends Window {
   private final ColorSetting colorSetting;
   private Animation animation;
   private final NumberSetting hueSlider;
   private boolean isHueDragging;
   private int hueValue;

   public ColorPickerWindow(float x, float y, float width, float height, ColorSetting colorSetting) {
      super(x, y, width, height);
      this.animation = new EaseInOutQuad(250, 1.0F, Direction.FORWARDS);
      this.hueSlider = new NumberSetting("Hue", 1.0F, 1.0F, 360.0F, 1.0F);
      this.colorSetting = colorSetting;
      float[] color = ColorUtil.getRGBAf(colorSetting.get());
      float[] hueArray = Color.RGBtoHSB((int)(color[0] * 255.0F), (int)(color[1] * 255.0F), (int)(color[2] * 255.0F), color);
      this.hueValue = (int)(hueArray[0] * 360.0F);
   }

   public void init() {
      this.animation = new EaseInOutQuad(250, 1.0F, Direction.FORWARDS);
   }

   public void render(int mouseX, int mouseY) {
      if (this.animation.finished(Direction.BACKWARDS)) {
         CsGui.colorPicker = null;
      } else {
         int bgColor = Color.decode("#151521").getRGB();
         int elementsColor = Color.decode("#1E1F30").getRGB();
         RenderUtil.Render2D.scaleStart(this.x + this.width / 2.0F, this.y + this.height / 2.0F, this.animation.getOutput());
         RenderUtil.Render2D.drawRoundedRect(this.x - 1.0F, this.y - 1.0F, this.width + 2.0F, this.height + 2.0F, 6.0F, Color.decode("#2B2C44").brighter().getRGB());
         RenderUtil.Render2D.drawRoundedRect(this.x, this.y, this.width, this.height, 5.0F, elementsColor);
         int hueColor = ColorUtil.HUEtoRGB(this.hueValue);
         RenderUtil.Render2D.drawGradientRound(this.x + 5.0F, this.y + 5.0F, this.width - 10.0F, this.height - 20.0F, 0.0F, Color.WHITE.getRGB(), Color.BLACK.getRGB(), hueColor, Color.BLACK.getRGB());
         int pixelColor = 0;
         float inc;
         float times;
         if (RenderUtil.isInRegion((float)mouseX, (float)mouseY, this.x + 5.0F, this.y + 5.0F, this.width - 10.0F, this.height - 20.0F) && !this.isHueDragging) {
            inc = ((float)mouseX - (this.x + 5.0F)) / (this.width - 10.0F);
            times = 1.0F - ((float)mouseY - (this.y + 5.0F)) / (this.height - 20.0F);
            pixelColor = Color.HSBtoRGB((float)this.hueValue / 360.0F, inc, times);
         }

         if (pixelColor != 0) {
            this.colorSetting.setColor(pixelColor);
         }

         if (this.isHueDragging) {
            this.hueValue = this.getSliderValue(this.hueSlider, this.x + 5.0F, mouseX);
         }

         inc = 0.2F;
         times = 1.0F / inc;
         float xHuePos = this.x + 5.0F;
         float size = (this.width - 10.0F) / times;

         for(int i = 0; (float)i < times; ++i) {
            boolean last = (float)i == times - 1.0F;
            if (last) {
               --size;
            }

            RenderUtil.Render2D.drawGradientRound(xHuePos, this.y + this.height - 10.0F, size, 5.0F, Color.HSBtoRGB(inc * (float)i, 1.0F, 1.0F), Color.HSBtoRGB(inc * (float)(i + 1), 1.0F, 1.0F), Color.HSBtoRGB(inc * (float)(i + 1), 1.0F, 1.0F), Color.HSBtoRGB(inc * (float)i, 1.0F, 1.0F), 0);
            if (!last) {
               xHuePos += size;
            }
         }

         RenderUtil.Render2D.drawRoundedRect(this.x + 4.0F + (float)this.getPos(this.hueSlider, this.hueValue), this.y + this.height - 11.0F, 2.0F, 7.0F, 1.0F, -1);
         RenderUtil.scaleEnd();
      }
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      if (!RenderUtil.isInRegion((int) mouseX, (int) mouseY, (int) this.x, (int) this.y, (int) this.width, (int) this.height) && this.animation.isDone()) {
         this.animation.setDirection(Direction.BACKWARDS);
         this.animation.setDuration(225);
      }

      if (RenderUtil.isInRegion((int) mouseX, (int) mouseY, (int) (this.x + 5.0F), (int) (this.y + this.height - 10.0F), (int) (this.width - 10.0F), (int) 5.0) && mouseButton == 0) {
         this.isHueDragging = true;
      }

   }

   public void mouseReleased(double mouseX, double mouseY, int state) {
      this.isHueDragging = false;
   }

   public int getSliderValue(NumberSetting numberSetting, float posX, int mouseX) {
      int delta = (int)(numberSetting.getMaxValue() - numberSetting.getMinValue());
      float clickedX = (float)mouseX - posX;
      float value = clickedX / (this.width - 10.0F);
      float outValue = (float)(numberSetting.getMinValue() + (double)((float)delta * value));
      return (int) MathUtility.clamp((float)((int)MathUtility.round((double)outValue, numberSetting.getIncrement())), (float)numberSetting.getMinValue(), (float)numberSetting.getMaxValue());
   }

   public int getPos(NumberSetting numberSetting, int value) {
      int delta = (int)(numberSetting.getMaxValue() - numberSetting.getMinValue());
      return (int)((double)(this.width - 10.0F) * ((double)value - numberSetting.getMinValue()) / (double)delta);
   }

   public ColorSetting getColorSetting() {
      return this.colorSetting;
   }
}