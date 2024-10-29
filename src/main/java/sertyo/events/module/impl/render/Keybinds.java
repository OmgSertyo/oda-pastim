package sertyo.events.module.impl.render;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import sertyo.events.Main;
import sertyo.events.manager.dragging.Draggable;
import sertyo.events.module.impl.render.Hud;
import sertyo.events.utility.render.ColorUtility;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.RenderUtility;
import sertyo.events.utility.render.animation.AnimationMath;
import sertyo.events.utility.render.fonts.Fonts;

import java.awt.Color;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import sertyo.events.module.Module;

public class Keybinds {
   public static float realOffset = 0.0F;

   public Keybinds() {
   }

   public static void render() {
      Main.getInstance().getScaleMath().pushScale();
      Hud hud = (Hud) Main.getInstance().getModuleManager().getModule(Hud.class);
      Draggable keybindsDraggable = hud.keybindsDraggable;
      List<Module> sortedBinds = (List) Main.getInstance().getModuleManager().getModules().stream().filter((modulex) -> {
         return modulex.getBind() != 0 && modulex.isEnabled();
      }).sorted(Comparator.comparing((modulex) -> {
         return Fonts.mntssb15.getStringWidth(modulex.getName());
      }, Comparator.reverseOrder())).collect(Collectors.toList());
      int offset = -1;
      int width = 105;
      if (!sortedBinds.isEmpty()) {
         width = (Integer)sortedBinds.stream().max(Comparator.comparingInt((modulex) -> {
            return Fonts.mntssb15.getStringWidth(getText(modulex));
         })).map((modulex) -> {
            return Fonts.mntssb15.getStringWidth(getText(modulex));
         }).get() + 11;
         if (width < 105) {
            width = 105;
         }

         offset = 0;

         for(Iterator var5 = sortedBinds.iterator(); var5.hasNext(); offset += 11) {
            Module module = (Module)var5.next();
         }
      }

      realOffset = AnimationMath.fast(realOffset, (float)offset, 15.0F);
      keybindsDraggable.setWidth((float)width);
      keybindsDraggable.setHeight((float)(19 + offset));
      int bgColor = Color.decode("#1B1C2C").getRGB();
      int elementsColor = Color.decode("#1E1F30").getRGB();
      int strokeColor = Color.decode("#26273B").getRGB();
      RenderUtility.drawGlow((float)(keybindsDraggable.getX() - 1), (float)(keybindsDraggable.getY() - 1), (float)(width + 2), 21.0F + realOffset, 10, new Color(bgColor).getRGB());
      RenderUtility.drawRoundedRect((float)(keybindsDraggable.getX() - 1), (float)(keybindsDraggable.getY() - 1), (float)(width + 2), 21.0F + realOffset, 11.0F, strokeColor);
      RenderUtility.drawRoundedRect((float)keybindsDraggable.getX(), (float)keybindsDraggable.getY(), (float)width, 19.0F + realOffset, 10.0F, elementsColor);
      RenderUtility.drawGradientGlow((float)(keybindsDraggable.getX() + width - 3 - Fonts.icons21.getStringWidth("o")), (float)keybindsDraggable.getY() + 6.5F, (float)(Fonts.icons21.getStringWidth("o") - 3), (float)(Fonts.icons21.getFontHeight() - 1), 8, ColorUtility.applyOpacity2(Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0], 0.4F), ColorUtility.applyOpacity2(Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0], 0.4F), ColorUtility.applyOpacity2(Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1], 0.4F), ColorUtility.applyOpacity2(Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1], 0.4F));
      int finalWidth = width;
      RenderUtility.applyGradientMask((float)(keybindsDraggable.getX() + width - 5 - Fonts.icons21.getStringWidth("o")), (float)keybindsDraggable.getY() + 6.5F, (float)Fonts.icons21.getStringWidth("o"), (float)Fonts.icons21.getFontHeight(), 0.5F, Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0], Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1], Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0], Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1], () -> {
         Fonts.icons21.drawString("o", (float)(keybindsDraggable.getX() + finalWidth - 5 - Fonts.icons21.getStringWidth("o")), (float)keybindsDraggable.getY() + 6.5F, -1);
      });
      Fonts.mntssb16.drawString("Keybinds", (float)(keybindsDraggable.getX() + 5), (float)keybindsDraggable.getY() + 6.5F, -1);
      if (!sortedBinds.isEmpty()) {
         RenderUtility.Render2D.drawRect((float)keybindsDraggable.getX(), (float)(keybindsDraggable.getY() + 16), (float)width, 1.0F, strokeColor);
      }

      RenderUtil.SmartScissor.push();
      RenderUtil.SmartScissor.setFromComponentCoordinates((double)keybindsDraggable.getX(), (double)keybindsDraggable.getY(), (double)width, (double)(19.0F + realOffset));
      offset = 0;

      for(Iterator var9 = sortedBinds.iterator(); var9.hasNext(); offset += 11) {
         Module module = (Module)var9.next();
         Fonts.mntssb15.drawString(module.getName(), (float)(keybindsDraggable.getX() + 5), (float)(keybindsDraggable.getY() + 22 + offset), -1);
         String var10000 = module.bind < 0 ? "MOUSE " + module.getMouseBind() : GLFW.glfwGetKeyName(module.getBind(), -1);
         String bindText = "[" + var10000 + "]";
         Fonts.mntssb15.drawString(bindText, (float)(keybindsDraggable.getX() + width - Fonts.mntssb15.getStringWidth(bindText) - 5), (float)(keybindsDraggable.getY() + 22 + offset), -1);
      }

      RenderUtil.SmartScissor.unset();
      RenderUtil.SmartScissor.pop();
      Main.getInstance().getScaleMath().popScale();
   }

   private static String getText(Module module) {
      String var10000 = module.getName();
      return var10000 + " [" + (module.bind < 0 ? "MOUSE " + module.getMouseBind() : GLFW.glfwGetKeyName(module.getBind(), -1)) + "]";
   }
}
