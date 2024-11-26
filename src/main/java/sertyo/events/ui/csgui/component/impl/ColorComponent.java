//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.csgui.component.impl;

import sertyo.events.module.setting.impl.ColorSetting;
import sertyo.events.ui.csgui.CsGui;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.ui.csgui.component.impl.ModuleComponent;
import sertyo.events.ui.csgui.window.ColorPickerWindow;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.render.RenderUtility;

import java.awt.Color;
public class ColorComponent extends Component {
   public ModuleComponent moduleComponent;
   public ColorSetting setting;

   public ColorComponent(ModuleComponent moduleComponent, ColorSetting setting) {
      super(0.0F, 0.0F, 0.0F, 14.0F);
      this.moduleComponent = moduleComponent;
      this.setting = setting;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      int elementsColor = Color.decode("#1E1F30").getRGB();
      Fonts.msBold[14].drawString(this.setting.getName(), this.x + 5.0F, this.y + 5.5F, (new Color(149, 149, 161)).getRGB());
      RenderUtility.drawRoundedRect(this.x + this.width - 16.0F, this.y + 1.5F, 11.0F, 11.0F, 10.0F, Color.decode("#2B2C44").brighter().getRGB());
      RenderUtility.drawRoundedRect(this.x + this.width - 15.0F, this.y + 2.5F, 9.0F, 9.0F, 8.0F, elementsColor);
      RenderUtility.drawRoundedRect(this.x + this.width - 14.0F, this.y + 3.5F, 7.0F, 7.0F, 6.0F, this.setting.get());
   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      if (RenderUtility.isHovered(mouseX, mouseY, (double)(this.x + this.width - 16.0F), (double)(this.y + 1.5F), 11.0, 11.0) && (CsGui.colorPicker == null || !CsGui.colorPicker.getColorSetting().equals(this.setting))) {
         CsGui.colorPicker = new ColorPickerWindow((float)(mouseX + 5.0), (float)(mouseY + 5.0), 80.0F, 80.0F, this.setting);
      }

   }

   public boolean isVisible() {
      return (Boolean)this.setting.getVisible().get();
   }
}
