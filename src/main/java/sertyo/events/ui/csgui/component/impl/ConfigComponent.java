//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.csgui.component.impl;

import sertyo.events.Main;
import sertyo.events.ui.csgui.component.Component;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.render.ColorUtility;
import sertyo.events.utility.render.RenderUtility;

import java.awt.Color;
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
      int elementsColor = Color.decode("#1E1F30").getRGB();
      Color glowColor = ColorUtility.applyOpacity(Color.BLACK, 0.2F);
      RenderUtility.drawShadow(this.x, this.y, this.width, this.height, 5, glowColor, glowColor);
      RenderUtility.drawRoundedRect(this.x, this.y, this.width, this.height, 10.0F, elementsColor);
      Fonts.msBold[17].drawString(this.name, this.x + 5.0F, this.y + 9.0F, -1);
      float xOffset = 2.0F;
      float spacing = 3.0F;

      float enabledWidth;
      for(Iterator var7 = this.buttons.iterator(); var7.hasNext(); xOffset += enabledWidth + spacing) {
         String mode = (String)var7.next();
         enabledWidth = this.getEnabledWidth(mode);
         float enabledHeight = 11.0F;
         RenderUtility.drawShadow(this.x + 70.0F + xOffset, this.y + 6.0F, enabledWidth, enabledHeight, 5, glowColor, glowColor);
         RenderUtility.drawRoundedRect(this.x + 70.0F + xOffset, this.y + 6.0F, enabledWidth, enabledHeight, 3.0F, Color.decode("#2B2C44").getRGB());
         Fonts.msBold[14].drawString(mode, this.x + 72.0F + xOffset, this.y + 10.0F, (new Color(149, 149, 161)).getRGB());
      }

   }

   public boolean mouseBoolClicked(double mouseX, double mouseY, int mouseButton) {
      float xOffset = 2.0F;
      float spacing = 3.0F;

      float enabledWidth;
      for(Iterator var8 = this.buttons.iterator(); var8.hasNext(); xOffset += enabledWidth + spacing) {
         String mode = (String)var8.next();
         enabledWidth = this.getEnabledWidth(mode);
         float enabledHeight = 11.0F;
         if (RenderUtility.isHovered(mouseX, mouseY, (double)(this.x + 70.0F + xOffset), (double)(this.y + 6.0F), (double)enabledWidth, (double)enabledHeight)) {
            switch (mode) {
               case "Load":
                  Main.getInstance().getConfigManager().loadConfig(this.name);
                  break;
               case "Save":
                  Main.getInstance().getConfigManager().saveConfig(this.name);
                  break;
               case "Delete":
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
