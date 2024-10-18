package sertyo.events.module.setting.impl;

import sertyo.events.module.setting.Setting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MultiBooleanSetting extends Setting {
   public List<Boolean> selectedValues;
   public List<String> values;

   public MultiBooleanSetting(String name, List<String> values) {
      this(name, values, (Supplier)null);
      this.setVisible(() -> {
         return true;
      });
   }

   public MultiBooleanSetting(String name, List<String> values, Supplier<Boolean> visible) {
      super(name, visible);
      this.selectedValues = new ArrayList();
      this.values = values;

      for(int i = 0; i < values.size(); ++i) {
         this.selectedValues.add(false);
      }

      this.setVisible(visible);
   }

   public MultiBooleanSetting(String name, List<String> values, boolean allEnabled, Supplier<Boolean> visible) {
      super(name, visible);
      this.selectedValues = new ArrayList();
      this.values = values;

      for(int i = 0; i < values.size(); ++i) {
         this.selectedValues.add(allEnabled);
      }

      this.setVisible(visible);
   }

   public boolean get(int id) {
      return (Boolean)this.selectedValues.get(id);
   }

   public void set(int id, Boolean value) {
      this.selectedValues.set(id, value);
   }
}
