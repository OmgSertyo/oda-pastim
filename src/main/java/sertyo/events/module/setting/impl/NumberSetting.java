package sertyo.events.module.setting.impl;

import sertyo.events.module.setting.Setting;

import java.util.function.Supplier;

public class NumberSetting extends Setting {
   public float current;
   public float min;
   public float max;
   public float increment;

   public NumberSetting(String name, float value, float min, float max, float increment) {
      super(name, value);
      this.min = min;
      this.max = max;
      this.current = value;
      this.increment = increment;
      this.setVisible(() -> {
         return true;
      });
   }

   public NumberSetting(String name, float value, float min, float max, float increment, Supplier<Boolean> visible) {
      super(name, value);
      this.min = min;
      this.max = max;
      this.current = value;
      this.increment = increment;
      this.setVisible(visible);
   }

   public double getMinValue() {
      return (double)this.min;
   }

   public void setMinValue(float minimum) {
      this.min = minimum;
   }

   public double getMaxValue() {
      return (double)this.max;
   }

   public void setMaxValue(float maximum) {
      this.max = maximum;
   }

   public float get() {
      return this.current;
   }

   public int getInt() {
      return (int)this.current;
   }

   public void set(float current) {
      this.current = current;
   }

   public double getIncrement() {
      return (double)this.increment;
   }

   public void setIncrement(float increment) {
      this.increment = increment;
   }
}
