package sertyo.events.module.setting;

import java.util.function.Supplier;

public class Setting {
   private String name;
   public Object value;
   protected Supplier<Boolean> visible;

   public Setting(String name, Object value) {
      this.name = name;
      this.value = value;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Object getValue() {
      return this.value;
   }

   public void setValue(Object value) {
      this.value = value;
   }

   public Supplier<Boolean> getVisible() {
      return this.visible;
   }

   public void setVisible(Supplier<Boolean> visible) {
      this.visible = visible;
   }
}
