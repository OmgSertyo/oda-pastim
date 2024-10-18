package sertyo.events.ui.csgui.component.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.glfw.GLFW;
import sertyo.events.Main;
import sertyo.events.manager.theme.Themes;
import sertyo.events.module.setting.Setting;
import sertyo.events.module.Module;
import sertyo.events.module.setting.impl.*;
import sertyo.events.ui.csgui.CsGui;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.AnimationMath;
import sertyo.events.utility.font.Fonts;

public class ModuleComponent extends Component {
   private final Module module;
   public boolean binding;
   public List<Component> elements = new ArrayList();
   public float enableAnimation = 0.0F;

   public ModuleComponent(Module module, float width, float height) {
      super(0.0F, 0.0F, width, height);
      this.module = module;
      Iterator var4 = module.getSettings().iterator();

      while(var4.hasNext()) {
         Setting setting = (Setting)var4.next();
         if (setting instanceof BooleanSetting) {
            this.elements.add(new BooleanComponent(this, (BooleanSetting)setting));
         } else if (setting instanceof NumberSetting) {
            this.elements.add(new SliderComponent(this, (NumberSetting)setting));
         } else if (setting instanceof ModeSetting) {
            this.elements.add(new ModeComponent(this, (ModeSetting)setting));
         } else if (setting instanceof MultiBooleanSetting) {
            this.elements.add(new MultiBoolComponent(this, (MultiBooleanSetting)setting));
         } else if (setting instanceof ColorSetting) {
            this.elements.add(new ColorComponent(this, (ColorSetting)setting));
         }
      }

   }

   public void render(int mouseX, int mouseY) {
      int offset = 0;
      Iterator var4 = this.elements.iterator();

      while(var4.hasNext()) {
         Component element = (Component)var4.next();
         if (element.isVisible()) {
            offset = (int)((float)offset + element.height);
         }
      }

      float normalHeight = this.height + (float)offset + 2;
      Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
      Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      boolean isDark = Main.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      Color moduleColor = isDark ? new Color(34, 34, 34) : new Color(210, 210, 210);
      RenderUtil.Render2D.drawShadow(this.x + 1.0F, this.y + normalHeight - 1.0F, (float)((int)this.width - 2), 2.0F, 4, color, color2);
      RenderUtil.Render2D.drawRoundedGradientRect(this.x + 0.5F, this.y + normalHeight - 8.0F, this.width - 1.0F, 9.0F, 5.0F, 1, color.getRGB(), color.getRGB(), color2.getRGB(), color2.getRGB());
      RenderUtil.Render2D.drawRoundedRect(this.x, this.y, this.width, normalHeight, 5.0F, moduleColor.getRGB());
      Fonts.msBold[14].drawString(this.binding ? "Press a key... " : this.module.getName(), this.x + 5.0F, this.y + 6.0F, isDark ? Color.WHITE.getRGB() : (new Color(40, 40, 40)).getRGB());
      if (this.binding) {
         String bindText = this.module.bind < 0 ? "MOUSE " + this.module.getMouseBind() : GLFW.glfwGetKeyName(this.module.getBind(), -1);
         if (bindText != null) {
            RenderUtil.Render2D.drawRoundedRect(this.x + this.width - 10.0F - (float)Fonts.msBold[14].getWidth(this.module.bind == 0 ? "R" : bindText), this.y + 2.0F, this.module.bind == 0 ? (float)(Fonts.msBold[14].getWidth("R") + 5) : (float)(Fonts.msBold[14].getWidth(bindText) + 5), 10.0F, 4.0F, isDark ? (new Color(27, 27, 27)).getRGB() : (new Color(180, 180, 180)).getRGB());
         if (this.module.bind != 0) {
               Fonts.msBold[14].drawString(bindText, this.x + this.width - 10.0F - (float) Fonts.msBold[14].getWidth(bindText) + 2.4F, this.y + 5.0F, isDark ? Color.WHITE.getRGB() : (new Color(40, 40, 40)).getRGB());
            }
         }
      }

      RenderUtil.Render2D.drawRect(this.x + 5.0F, this.y + 5.0F + (float)Fonts.msBold[14].getFontHeight() + 3.0F, this.width - 10.0F, 1.0F, isDark ? (new Color(54, 54, 54)).getRGB() : (new Color(170, 170, 170)).getRGB());
      Fonts.msBold[14].drawString("Enabled", this.x + 5.0F, this.y + 5.0F + (float)Fonts.msBold[14].getFontHeight() + 9.0F, isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
      this.enableAnimation = AnimationMath.fast(this.enableAnimation, this.module.enabled ? -1.0F : 0.0F, 15.0F);
      RenderUtil.Render2D.drawRoundedRect(this.x + this.width - 25.0F, this.y + 5.0F + (float)Fonts.msBold[14].getFontHeight() + 6.0F, 20.0F, 10.0F, 6.0F, isDark ? (new Color(25, 25, 25)).getRGB() : (new Color(160, 160, 160)).getRGB());
      Color c = ColorUtil.interpolateColorC(isDark ? (new Color(34, 34, 34)).getRGB() : Color.WHITE.getRGB(), isDark ? Color.WHITE.getRGB() : (new Color(100, 100, 100)).getRGB(), Math.abs(this.enableAnimation));
      RenderUtil.Render2D.drawRoundedRect(this.x + this.width - 23.5F - this.enableAnimation * 10.0F, this.y + 6.5F + (float)Fonts.msBold[14].getFontHeight() + 6.0F, 7.0F, 7.0F, 6.0F, c.getRGB());
      offset = 0;
      Iterator var10 = this.elements.iterator();

      while(var10.hasNext()) {
         Component element = (Component)var10.next();
         if (element.isVisible()) {
            element.x = this.x;
            element.y = this.y + 29.0F + (float)offset;
            element.width = this.width;
            element.render(mouseX, mouseY);
            offset = (int)((float)offset + element.height);
         }
      }

   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (this.binding && mouseButton > 2) {
         this.module.bind = mouseButton - 100;
         this.binding = false;
      }

      boolean enableButtonHovered = RenderUtil.isHovered(mouseX, mouseY, (double)(this.x + this.width - 25.0F), (double)(this.y + 5.0F + (float)Fonts.msBold[14].getFontHeight() + 6.0F), 20.0D, 10.0D);
      boolean isTitleHovered = RenderUtil.isHovered(mouseX, mouseY, (double)this.x, (double)this.y, (double)this.width, (double)(Fonts.msBold[14].getFontHeight() + 8));
      if (enableButtonHovered && mouseButton == 0) {
         this.module.toggle();
      } else if (isTitleHovered && mouseButton == 2) {
         this.binding = !this.binding;
      }

      Iterator var8 = this.elements.iterator();

      while(var8.hasNext()) {
         Component element = (Component)var8.next();
         element.mouseClicked(mouseX, mouseY, mouseButton);
      }

   }

   public void keyTyped(int keyCode) {
      super.keyTyped(keyCode);
      if (this.binding) {
         if (keyCode == 1) {
            CsGui.escapeInUse = true;
            this.binding = false;
            return;
         }

         if (keyCode == 211) {
            this.module.bind = 0;
         } else {
            this.module.bind = keyCode;
         }

         this.binding = false;
      }

   }

   public Module getModule() {
      return this.module;
   }
}
