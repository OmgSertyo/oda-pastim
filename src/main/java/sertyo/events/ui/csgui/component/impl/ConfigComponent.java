//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.csgui.component.impl;

import sertyo.events.Main;
import sertyo.events.manager.theme.Themes;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.font.Fonts;

import java.awt.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
public class ConfigComponent extends Component {
   private final String name;
   private final List<String> buttons = Arrays.asList("Load", "Save", "Delete");

   public ConfigComponent(String name, float width, float height) {
      super(0.0F, 0.0F, width, height);
      this.name = name;
   }

   public void render(int mouseX, int mouseY) {
      Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
      Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      boolean isDark = Main.getInstance().getThemeManager().getCurrentGuiTheme().equals(Themes.DARK.getTheme());
      Color moduleColor = isDark ? new Color(34, 34, 34) : new Color(210, 210, 210);
      RenderUtil.Render2D.drawShadow(this.x + 1.0F, this.y + this.height - 1.0F, (float)((int)this.width - 2), 2.0F, 4, color, color2);
      RenderUtil.Render2D.drawRoundedGradientRect(this.x + 0.5F, this.y + this.height - 8.0F, this.width - 1.0F, 9.0F, 5.0F, 1.0f, color.getRGB(), color.getRGB(), color2.getRGB(), color2.getRGB());
      RenderUtil.Render2D.drawRoundedRect(this.x, this.y, this.width, this.height, 5.0F, moduleColor.getRGB());
      Fonts.msBold[16].drawString(this.name, this.x + 5.0F, this.y + 5.0F, isDark ? Color.WHITE.getRGB() : (new Color(40, 40, 40)).getRGB());
      float xOffset = 2.0F;
      float spacing = 3.0F;

      float enabledWidth;
      for(Iterator var9 = this.buttons.iterator(); var9.hasNext(); xOffset += enabledWidth + spacing) {
         String mode = (String)var9.next();
         enabledWidth = this.getEnabledWidth(mode);
         float enabledHeight = (float)(Fonts.msBold[14].getFontHeight() + 4);
         RenderUtil.Render2D.drawRoundedRect(this.x + 3.0F + xOffset, this.y + 8.0F + (float)Fonts.msBold[14].getFontHeight(), enabledWidth, enabledHeight, 3.0F, isDark ? (new Color(52, 52, 52)).getRGB() : (new Color(160, 160, 160)).getRGB());
         Fonts.msBold[14].drawString(mode, this.x + 5.0F + xOffset, this.y + 13.0F + (float)Fonts.msBold[14].getFontHeight(), isDark ? Color.WHITE.getRGB() : (new Color(55, 55, 55)).getRGB());
      }

   }

   float xOffset = 2.0F;
   float spacing = 3.0F;

   float enabledWidth;
   public boolean mouseBoolClicked(double mouseX, double mouseY, int mouseButton) {
      for(Iterator var8 = this.buttons.iterator(); var8.hasNext(); xOffset += enabledWidth + spacing) {
      String mode = (String)var8.next();
      enabledWidth = this.getEnabledWidth(mode);
      float enabledHeight = (float)(Fonts.msBold[14].getFontHeight() + 4);
      if (RenderUtil.isHovered(mouseX, mouseY, (double)(this.x + 3.0F + xOffset), (double)(this.y + 8.0F + (float)Fonts.msBold[14].getFontHeight()), (double)enabledWidth, (double)enabledHeight)) {
         byte var13 = -1;
         switch(mode.hashCode()) {
            case 2373894:
               if (mode.equals("Load")) {
                  var13 = 0;
               }
               break;
            case 2569629:
               if (mode.equals("Save")) {
                  var13 = 1;
               }
               break;
            case 2043376075:
               if (mode.equals("Delete")) {
                  var13 = 2;
               }
         }

         switch(var13) {
            case 0:
               Main.getInstance().getConfigManager().loadConfig(this.name);
               break;
            case 1:
               Main.getInstance().getConfigManager().saveConfig(this.name);
               break;
            case 2:
               Main.getInstance().getConfigManager().deleteConfig(this.name);
               return true;
         }
      }
   }

      return false;
}

   private float getEnabledWidth(String mode) {
      return (float)(Fonts.msBold[14].getWidth(mode) + 4);
   }

   public String getName() {
      return this.name;
   }
}
