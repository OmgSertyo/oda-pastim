//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.csgui.component.impl;

import com.google.common.collect.Lists;
import sertyo.events.module.setting.impl.MultiBooleanSetting;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.render.RenderUtility;
import sertyo.events.utility.render.fonts.Fonts;

import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;

public class MultiBoolComponent extends Component {
   public ModuleComponent moduleComponent;
   public MultiBooleanSetting setting;
   private boolean extended;

   public MultiBoolComponent(ModuleComponent moduleComponent, MultiBooleanSetting setting) {
      super(0.0F, 0.0F, 0.0F, 15.0F);
      this.moduleComponent = moduleComponent;
      this.setting = setting;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      int color = Color.decode("#1E1F30").getRGB();
      sertyo.events.utility.font.Fonts.msBold[14].drawString(this.setting.getName(), this.x + 5.0F, this.y + 5.5F, (new Color(149, 149, 161)).getRGB());
      float normalHeight = 0.0F;
      if (this.extended) {
         normalHeight = 11.5F;

         for(Iterator var5 = this.setting.values.stream().sorted(Comparator.comparingDouble(this::getEnabledWidth)).toList().iterator(); var5.hasNext(); normalHeight += 10.0F) {
            String mode = (String)var5.next();
         }
      } else {
         normalHeight = 11.0F;
      }

      this.setHeight(normalHeight + 4.0F);
      int maxLength = this.setting.values.stream().mapToInt(this::getEnabledWidth).max().orElse(0);
      StringBuilder str = new StringBuilder();
      Iterator var7 = this.setting.values.iterator();

      while(var7.hasNext()) {
         String mode = (String)var7.next();
         int index = this.setting.values.indexOf(mode);
         if (this.setting.get(index)) {
            str.append(mode).append(", ");
         }
      }

      String result = str.toString();
      if (result.endsWith(", ")) {
         result = result.substring(0, result.length() - 2);
      }

      if (result.isEmpty()) {
         result = "Empty";
      }

      RenderUtility.drawRoundedRect(this.x + this.width - 24.0F - (float)maxLength, this.y + 1.5F, (float)maxLength + 16.5F, normalHeight, 4.0F, Color.decode("#2B2C44").getRGB());
      Fonts.mntssb14.drawSubstring(result, this.x + this.width - 21.0F - (float)maxLength, this.y + 5.5F, (new Color(149, 149, 161)).getRGB(), (float)(maxLength - 5));
      sertyo.events.utility.font.Fonts.icons[16].drawString("t", this.x + this.width - 17.0F, this.y + 6.5F, (new Color(149, 149, 161)).getRGB());
      if (this.extended) {
         int offset = 0;

         for(Iterator var16 = Lists.reverse(this.setting.values.stream().sorted(Comparator.comparingDouble(this::getEnabledWidth)).toList()).iterator(); var16.hasNext(); offset += 10) {
            String mode = (String)var16.next();
            int index = this.setting.values.indexOf(mode);
            if (this.setting.get(index)) {
               RenderUtility.drawRoundedRect(this.x + this.width - 22.0F - (float)maxLength, this.y + 12.0F + (float)offset, (float)(Fonts.mntssb14.getStringWidth(mode) + 4), 9.0F, 4.0F, color);
            }

            Fonts.mntssb14.drawString(mode, this.x + this.width - 20.0F - (float)maxLength, this.y + 15.0F + (float)offset, this.setting.get(index) ? -1 : (new Color(149, 149, 161)).getRGB());
         }
      }

   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      super.mouseClicked(mouseX, mouseY, mouseButton);
      int maxLength = this.setting.values.stream().mapToInt(this::getEnabledWidth).max().orElse(0);
      if (mouseButton == 1 && RenderUtility.isHovered(mouseX, mouseY, (double)(this.x + this.width - 23.0F - (float)maxLength), (double)(this.y + 1.5F), (double)((float)maxLength + 15.5F), 11.0)) {
         this.extended = !this.extended;
      }

      if (this.extended) {
         int offset = 0;

         for(Iterator var8 = Lists.reverse(this.setting.values.stream().sorted(Comparator.comparingDouble(this::getEnabledWidth)).toList()).iterator(); var8.hasNext(); offset += 10) {
            String mode = (String)var8.next();
            if (RenderUtility.isHovered(mouseX, mouseY, (double)(this.x + this.width - 22.0F - (float)maxLength), (double)(this.y + 12.0F + (float)offset), (double)(Fonts.mntssb14.getStringWidth(mode) + 4), 9.0)) {
               int index = this.setting.values.indexOf(mode);
               this.setting.selectedValues.set(index, !(Boolean)this.setting.selectedValues.get(index));
               return;
            }
         }
      }

   }

   private int getEnabledWidth(String mode) {
      return Fonts.mntssb14.getStringWidth(mode) + 4;
   }

   public boolean isVisible() {
      return (Boolean)this.setting.getVisible().get();
   }
}
