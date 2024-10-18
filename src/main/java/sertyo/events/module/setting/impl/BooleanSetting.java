package sertyo.events.module.setting.impl;

import sertyo.events.module.setting.Setting;

import java.util.function.Supplier;

public class BooleanSetting extends Setting {
   public boolean state;

   public BooleanSetting(String name, boolean state) {
      super(name, state);
      this.state = state;
      this.setVisible(() -> {
         return true;
      });
   }

   public BooleanSetting(String name, boolean state, Supplier<Boolean> visible) {
      super(name, state);
      this.state = state;
      this.setVisible(visible);
   }

   public boolean get() {
      return this.state;
   }

   public void set(boolean state) {
      this.state = state;
   }
}
