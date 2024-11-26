package sertyo.events.ui.csgui.component.impl;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import com.google.common.collect.Lists;
import sertyo.events.module.setting.impl.ModeSetting;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.ui.csgui.component.impl.ModuleComponent;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.render.RenderUtility;

import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;

public class ModeComponent extends Component {
   public ModuleComponent moduleComponent;
   public ModeSetting setting;
   private boolean extended;

   public ModeComponent(ModuleComponent moduleComponent, ModeSetting setting) {
      super(0.0F, 0.0F, 0.0F, 15.0F);
      this.moduleComponent = moduleComponent;
      this.setting = setting;
   }

   public void render(int mouseX, int mouseY) {
      super.render(mouseX, mouseY);
      int color = Color.decode("#1E1F30").getRGB();
      Fonts.msBold[14].drawString(this.setting.getName(), this.x + 5.0F, this.y + 5.5F, (new Color(149, 149, 161)).getRGB());
      float normalHeight = 0.0F;
      if (this.extended) {
         normalHeight = 12.5F;

         for(Iterator var5 = this.setting.getModes().stream().sorted(Comparator.comparingDouble(this::getEnabledWidth)).toList().iterator(); var5.hasNext(); normalHeight += 9.0F) {
            String mode = (String)var5.next();
         }
      } else {
         normalHeight = 11.0F;
      }

      this.setHeight(normalHeight + 4.0F);
      int maxLength = this.setting.getModes().stream().mapToInt(this::getEnabledWidth).max().orElse(0);
      RenderUtility.drawRoundedRect(this.x + this.width - 20.0F - (float)(this.extended ? maxLength : Fonts.msBold[14].getWidth(this.setting.get()) + 4), this.y + 1.5F, (float)(this.extended ? maxLength : Fonts.msBold[14].getWidth(this.setting.get()) + 4) + 12.5F, normalHeight, 4.0F, Color.decode("#2B2C44").getRGB());
      Fonts.msBold[14].drawString(this.setting.get(), this.x + this.width - 17.0F - (float)(this.extended ? maxLength : Fonts.msBold[14].getWidth(this.setting.get()) + 4), this.y + 5.5F, (new Color(149, 149, 161)).getRGB());
      Fonts.icons[16].drawString("t", this.x + this.width - 17.0F, this.y + 6.5F, (new Color(149, 149, 161)).getRGB());
      if (this.extended) {
         int offset = 0;

         for(Iterator var7 = Lists.reverse(this.setting.getModes().stream().sorted(Comparator.comparingDouble(this::getEnabledWidth)).toList()).iterator(); var7.hasNext(); offset += 9) {
            String mode = (String)var7.next();
            if (this.setting.is(mode)) {
               RenderUtility.drawRoundedRect(this.x + this.width - 18.0F - (float)(this.extended ? maxLength : Fonts.msBold[14].getWidth(this.setting.get()) + 4), this.y + 12.0F + (float)offset, (float)(Fonts.msBold[14].getWidth(mode) + 4), 9.0F, 4.0F, color);
            }

            Fonts.msBold[14].drawString(mode, this.x + this.width - 16.0F - (float)(this.extended ? maxLength : Fonts.msBold[14].getWidth(this.setting.get()) + 4), this.y + 15.0F + (float)offset, this.setting.is(mode) ? -1 : (new Color(149, 149, 161)).getRGB());
         }
      }

   }

   public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
      int maxLength = this.setting.getModes().stream().mapToInt(this::getEnabledWidth).max().orElse(0);
      if (mouseButton == 1 && RenderUtility.isHovered(mouseX, mouseY, (double)(this.x + this.width - 19.0F - (float)(this.extended ? maxLength : Fonts.msBold[14].getWidth(this.setting.get()) + 4)), (double)(this.y + 1.5F), (double)((float)(this.extended ? maxLength : Fonts.msBold[14].getWidth(this.setting.get()) + 4) + 11.5F), 11.0)) {
         this.extended = !this.extended;
      }

      if (this.extended) {
         int offset = 0;

         for(Iterator var8 = Lists.reverse(this.setting.getModes().stream().sorted(Comparator.comparingDouble(this::getEnabledWidth)).toList()).iterator(); var8.hasNext(); offset += 9) {
            String mode = (String)var8.next();
            if (RenderUtility.isHovered(mouseX, mouseY, (double)(this.x + this.width - 18.0F - (float)(this.extended ? maxLength : Fonts.msBold[14].getWidth(this.setting.get()) + 4)), (double)(this.y + 12.0F + (float)offset), (double)(Fonts.msBold[14].getWidth(mode) + 4), 9.0)) {
               this.setting.set(mode);
               return;
            }
         }
      }

   }

   private int getEnabledWidth(String mode) {
      return (int) (Fonts.msBold[14].getWidth(mode) + 4);
   }

   public boolean isVisible() {
      return (Boolean)this.setting.getVisible().get();
   }
}
