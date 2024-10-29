package sertyo.events.module.setting.impl;

import sertyo.events.module.setting.Setting;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModeSetting extends Setting {
   public final List<String> modes;
   public String currentMode;
   public int index;

   public ModeSetting(String name, String currentMode, String... options) {
      super(name, currentMode);
      this.modes = Arrays.asList(options);
      this.index = this.modes.indexOf(currentMode);
      this.currentMode = (String)this.modes.get(this.index);
      this.setVisible(() -> {
         return true;
      });
   }

   public ModeSetting(String name, String currentMode, Supplier<Boolean> visible, String... options) {
      super(name, currentMode);
      this.modes = Arrays.asList(options);
      this.index = this.modes.indexOf(currentMode);
      this.currentMode = (String)this.modes.get(this.index);
      this.setVisible(visible);
   }

   public boolean is(String mode) {
      return this.currentMode.equals(mode);
   }

   public String get() {
      return this.currentMode;
   }
   public int getIndexs() {
      return this.index;
   }

   public void set(String selected) {
      this.currentMode = selected;
      this.index = this.modes.indexOf(selected);
   }

   public List<String> getModes() {
      return this.modes;
   }

   public String getOptions() {
      return (String)this.modes.get(this.index);
   }
}
