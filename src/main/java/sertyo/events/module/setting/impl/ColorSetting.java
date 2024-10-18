package sertyo.events.module.setting.impl;

import sertyo.events.module.setting.Setting;

import java.awt.Color;
import java.util.function.Supplier;

public class ColorSetting extends Setting {
   private int color;

   public ColorSetting(String name, int color, Supplier<Boolean> visible) {
      super(name, color);
      this.color = color;
      this.setVisible(visible);
   }

   public ColorSetting(String name, int color) {
      super(name, color);
      this.color = color;
      this.setVisible(() -> {
         return true;
      });
   }

   public int get() {
      return this.color;
   }

   public Color getColor() {
      return new Color(this.color);
   }

   public void setColor(int color) {
      this.color = color;
   }
}
