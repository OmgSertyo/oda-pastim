//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import sertyo.events.Main;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.module.Category;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.Animation;
import sertyo.events.module.Module;
import sertyo.events.utility.render.animation.Direction;
import sertyo.events.utility.font.Fonts;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@ModuleAnnotation(
        name = "Arraylist",
        category = Category.RENDER
)
public class Arraylist extends sertyo.events.module.Module {
   public BooleanSetting hideRender = new BooleanSetting("Hide Render", false);
   public BooleanSetting shadow = new BooleanSetting("Shadow", true);
   public static NumberSetting colorSpeed = new NumberSetting("Color Speed", 6.0F, 1.0F, 9.0F, 1.0F);
   public BooleanSetting lowerCase = new BooleanSetting("Lower Case", false);
   public BooleanSetting colored = new BooleanSetting("Colored", true);

   public Arraylist() {
   }

   @EventTarget
   public void onRender2D(EventRender2D event) {
      if (!mc.gameSettings.showDebugInfo) {
         int scaledWidth = Main.getInstance().getScaleMath().calc(event.getResolution().getScaledWidth());
         float x = (float)(scaledWidth - 10);
         float y = 60.0F;
         int offset = 0;
         int count = 0;
         Color bgColor = Color.decode("#1B1C2C");
         Color strokeColor = Color.decode("#26273B");
         Main.getInstance().getScaleMath().pushScale();
         List<Module> sortedModules = this.getSortedModules();
         String first = "";
         String last = "";
         Iterator var12 = sortedModules.iterator();

         while(true) {
            sertyo.events.module.Module module;
            do {
               if (!var12.hasNext()) {
                  var12 = sortedModules.iterator();

                  while(true) {
                     Animation moduleAnimation;
                     float stringWidth;
                     float alphaAnimation;
                     do {
                        String moduleName;
                        do {
                           if (!var12.hasNext()) {
                              offset = 0;
                              count = 0;
                              var12 = sortedModules.iterator();

                              while(true) {
                                 do {
                                    do {
                                       if (!var12.hasNext()) {
                                          Main.getInstance().getScaleMath().popScale();
                                          return;
                                       }

                                       module = (Module)var12.next();
                                    } while(this.hideRender.get() && module.getCategory() == Category.RENDER);

                                    moduleName = this.lowerCase.get() ? module.getName().toLowerCase() : module.getName();
                                    moduleAnimation = module.getAnimation();
                                    stringWidth = (float)(Fonts.msBold[15].getWidth(moduleName) + 6);
                                    moduleAnimation.setDirection(module.isEnabled() ? Direction.FORWARDS : Direction.BACKWARDS);
                                 } while(!module.isEnabled() && moduleAnimation.finished(Direction.BACKWARDS));

                                 if (!moduleAnimation.isDone()) {
                                    RenderUtil.Render2D.scaleStart(x - stringWidth + stringWidth / 2.0F, y + (float)offset + 7.5F, moduleAnimation.getOutput());
                                 }

                                 alphaAnimation = moduleAnimation.getOutput();
                                 RenderUtil.Render2D.drawCRoundedRect(x - stringWidth, y + (float)offset, stringWidth, 11.0F, !module.getName().equals(first) ? 0.0F : 10.0F, 10.0F, module.getName().equals(first) ? 10.0F : 0.0F, module.getName().equals(last) ? 10.0F : 0.0F, ColorUtil.applyOpacity(bgColor, alphaAnimation).getRGB());
                                 Fonts.msBold[15].drawString(moduleName, x - stringWidth + 3.0F, y + (float)offset + (14.0F - (float)Fonts.msBold[15].getFontHeight()) / 2.0F + 0.5F, this.colored.get() ? getArrayColor(count).getRGB() : -1);
                                 if (!moduleAnimation.isDone()) {
                                    RenderUtil.scaleEnd();
                                 }

                                 offset = (int)((float)offset + moduleAnimation.getOutput() * 10.0F);
                                 ++count;
                              }
                           }

                           module = (Module)var12.next();
                        } while(this.hideRender.get() && module.getCategory() == Category.RENDER);

                        moduleName = this.lowerCase.get() ? module.getName().toLowerCase() : module.getName();
                        moduleAnimation = module.getAnimation();
                        stringWidth = (float)(Fonts.msBold[15].getWidth(moduleName) + 6);
                     } while(!module.isEnabled() && moduleAnimation.finished(Direction.BACKWARDS));

                     if (!moduleAnimation.isDone()) {
                        RenderUtil.Render2D.scaleStart(x - stringWidth + stringWidth / 2.0F, y + (float)offset + 7.5F, moduleAnimation.getOutput());
                     }

                     alphaAnimation = moduleAnimation.getOutput();
                     RenderUtil.Render2D.drawCRoundedRect(x - stringWidth - 1.0F, y + (float)offset - 1.0F, stringWidth + 2.0F, 13.0F, !module.getName().equals(first) ? 0.0F : 10.0F, 10.0F, module.getName().equals(first) ? 10.0F : 0.0F, module.getName().equals(last) ? 10.0F : 0.0F, ColorUtil.applyOpacity(strokeColor, alphaAnimation).getRGB());
                     if (!moduleAnimation.isDone()) {
                        RenderUtil.scaleEnd();
                     }

                     offset = (int)((float)offset + moduleAnimation.getOutput() * 10.0F);
                     ++count;
                  }
               }

               module = (Module)var12.next();
            } while(this.hideRender.get() && module.getCategory() == Category.RENDER);

            if (module.isEnabled()) {
               if (first.isEmpty()) {
                  first = module.getName();
               }

               last = module.getName();
            }
         }
      }
   }

   public List<Module> getSortedModules() {
      return (List) Main.getInstance().getModuleManager().getModules().stream().sorted((module1, module2) -> {
         float width1 = (float)Fonts.msBold[15].getWidth(this.lowerCase.get() ? module1.getName().toLowerCase() : module1.getName());
         float width2 = (float)Fonts.msBold[15].getWidth(this.lowerCase.get() ? module2.getName().toLowerCase() : module2.getName());
         return Float.compare(width2, width1);
      }).collect(Collectors.toList());
   }

   public static Color getColor(int index) {
      Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
      Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      return ColorUtil.gradient((int)(10.0F - colorSpeed.get()), index * 2, new Color[]{color, color2});
   }

   public static Color getArrayColor(int index) {
      Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
      Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      return ColorUtil.gradient((int)(10.0F - colorSpeed.get()), index * 20, new Color[]{color, color2});
   }
}
