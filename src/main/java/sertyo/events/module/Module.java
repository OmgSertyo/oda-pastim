//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.module;

import com.darkmagician6.eventapi.EventManager;
import com.google.gson.JsonObject;

import net.minecraft.util.text.TextFormatting;
import sertyo.events.manager.notification.NotificationManager;
import sertyo.events.manager.notification.NotificationType;
import sertyo.events.module.impl.render.ClickGuiModule;
import sertyo.events.module.setting.Setting;
import sertyo.events.module.setting.impl.*;
import sertyo.events.utility.Utility;
import sertyo.events.utility.render.animation.Animation;
import sertyo.events.utility.render.animation.Direction;
import sertyo.events.utility.render.animation.impl.DecelerateAnimation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class Module implements Utility {
   protected ModuleAnnotation info = (ModuleAnnotation)this.getClass().getAnnotation(ModuleAnnotation.class);
   public String name;
   public Category category;
   public boolean enabled;
   public int bind;
   private final Animation animation;

   public Module() {
      this.animation = new DecelerateAnimation(250, 1.0F, Direction.BACKWARDS);
      this.name = this.info.name();
      this.category = this.info.category();
      this.enabled = false;
      this.bind = 0;
   }

   public boolean isSearched() {
    //  return CsGui.search.getText().isEmpty() || this.name.toLowerCase().toLowerCase().contains(CsGui.search.getText());
      return true;
   }

   public void setToggled(boolean state) {
      if (state) {
         this.onEnable();
      } else {
         this.onDisable();
      }

      this.enabled = state;
   }

   public void toggle() {
      this.enabled = !this.enabled;
      if (this.enabled) {
         this.onEnable();
      } else {
         this.onDisable();
      }

   }

   public int getMouseBind() {
      return this.bind + 100;
   }

   public void onEnable() {
      EventManager.register(this);
      if (!(this instanceof ClickGuiModule)) {
         //NotificationManager.notify(NotificationType.SUCCESS, "Module was " + TextFormatting.GREEN + "enabled", TextFormatting.GRAY + this.name, 1000.0F);
      }

   }

   public void onDisable() {
      EventManager.unregister(this);
      if (!(this instanceof ClickGuiModule)) {
     //    NotificationManager.notify(NotificationType.ERROR, "Module was " + TextFormatting.RED + "disabled", TextFormatting.GRAY + this.name, 1000.0F);
      }

   }

   public List<Setting> getSettings() {
      return (List)Arrays.stream(this.getClass().getDeclaredFields()).map((field) -> {
         try {
            field.setAccessible(true);
            return field.get(this);
         } catch (IllegalAccessException var3) {
            var3.printStackTrace();
            return null;
         }
      }).filter((field) -> {
         return field instanceof Setting;
      }).map((field) -> {
         return (Setting)field;
      }).collect(Collectors.toList());
   }

   public JsonObject save() {
      JsonObject object = new JsonObject();
      object.addProperty("enabled", this.enabled);
      object.addProperty("bind", this.bind);
      JsonObject propertiesObject = new JsonObject();
      Iterator var3 = this.getSettings().iterator();

      while(true) {
         while(var3.hasNext()) {
            Setting setting = (Setting)var3.next();
            if (setting instanceof BooleanSetting) {
               propertiesObject.addProperty(setting.getName(), ((BooleanSetting)setting).get());
            } else if (setting instanceof ModeSetting) {
               propertiesObject.addProperty(setting.getName(), ((ModeSetting)setting).get());
            } else if (setting instanceof NumberSetting) {
               propertiesObject.addProperty(setting.getName(), ((NumberSetting)setting).get());
            } else if (setting instanceof ColorSetting) {
               propertiesObject.addProperty(setting.getName(), ((ColorSetting)setting).get());
            } else if (setting instanceof MultiBooleanSetting) {
               StringBuilder builder = new StringBuilder();
               int i = 0;

               for(Iterator var7 = ((MultiBooleanSetting)setting).values.iterator(); var7.hasNext(); ++i) {
                  String s = (String)var7.next();
                  if ((Boolean)((MultiBooleanSetting)setting).selectedValues.get(i)) {
                     builder.append(s).append("\n");
                  }
               }

               propertiesObject.addProperty(setting.getName(), builder.toString());
            }
         }

         object.add("Settings", propertiesObject);
         return object;
      }
   }

   public void load(JsonObject object) {
      if (object != null) {
         if (object.has("enabled")) {
            this.setToggled(object.get("enabled").getAsBoolean());
         }

         if (object.has("bind")) {
            this.bind = object.get("bind").getAsInt();
         }

         Iterator var2 = this.getSettings().iterator();

         while(true) {
            while(true) {
               Setting setting;
               JsonObject propertiesObject;
               do {
                  do {
                     do {
                        if (!var2.hasNext()) {
                           return;
                        }

                        setting = (Setting)var2.next();
                        propertiesObject = object.getAsJsonObject("Settings");
                     } while(setting == null);
                  } while(propertiesObject == null);
               } while(!propertiesObject.has(setting.getName()));

               if (setting instanceof BooleanSetting) {
                  ((BooleanSetting)setting).state = propertiesObject.get(setting.getName()).getAsBoolean();
               } else if (setting instanceof ModeSetting) {
                  ((ModeSetting)setting).set(propertiesObject.get(setting.getName()).getAsString());
               } else if (setting instanceof NumberSetting) {
                  ((NumberSetting)setting).current = propertiesObject.get(setting.getName()).getAsFloat();
               } else if (setting instanceof ColorSetting) {
                  ((ColorSetting)setting).setColor(propertiesObject.get(setting.getName()).getAsInt());
               } else if (setting instanceof MultiBooleanSetting) {
                  int i;
                  for(i = 0; i < ((MultiBooleanSetting)setting).selectedValues.size(); ++i) {
                     ((MultiBooleanSetting)setting).selectedValues.set(i, false);
                  }

                  i = 0;
                  String[] strs = propertiesObject.get(setting.getName()).getAsString().split("\n");

                  for(Iterator var7 = ((MultiBooleanSetting)setting).values.iterator(); var7.hasNext(); ++i) {
                     String s = (String)var7.next();
                     String[] var9 = strs;
                     int var10 = strs.length;

                     for(int var11 = 0; var11 < var10; ++var11) {
                        String str = var9[var11];
                        if (str.equalsIgnoreCase(s)) {
                           ((MultiBooleanSetting)setting).selectedValues.set(i, true);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public String getName() {
      return this.name;
   }

   public Category getCategory() {
      return this.category;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public int getBind() {
      return this.bind;
   }

   public Animation getAnimation() {
      return this.animation;
   }
}
