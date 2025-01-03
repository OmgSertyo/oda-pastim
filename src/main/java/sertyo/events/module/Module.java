package sertyo.events.module;

import com.darkmagician6.eventapi.EventManager;
import com.google.gson.JsonObject;
import lombok.Getter;
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

@Getter
public class Module implements Utility {
   protected ModuleAnnotation info = this.getClass().getAnnotation(ModuleAnnotation.class);
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
   public Module(String name, Category category) {
      this.animation = new DecelerateAnimation(250, 1.0F, Direction.BACKWARDS);
      this.name = name;
      this.category = category;
      this.enabled = false;
      this.bind = 0;
   }
   public boolean isSearched() {
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
       //NotificationManager.notify(NotificationType.SUCCESS, "Module was " + TextFormatting.GREEN + "enabled", TextFormatting.GRAY + this.name, 1000.0F);

   }

   public void onDisable() {
      EventManager.unregister(this);
     //    NotificationManager.notify(NotificationType.ERROR, "Module was " + TextFormatting.RED + "disabled", TextFormatting.GRAY + this.name, 1000.0F);


   }

   public List getSettings() {
      return Arrays.stream(this.getClass().getDeclaredFields()).map((field) -> {
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
      Iterator iterator = this.getSettings().iterator();

      while(true) {
         while(iterator.hasNext()) {
            Setting setting = (Setting)iterator.next();
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

               for(Iterator<String> iterator2 = ((MultiBooleanSetting)setting).values.iterator(); iterator.hasNext(); ++i) {
                  String s = iterator2.next();
                  if (((MultiBooleanSetting)setting).selectedValues.get(i)) {
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

         Iterator iterator = this.getSettings().iterator();

         while(true) {
            while(true) {
               Setting setting;
               JsonObject propertiesObject;
               do {
                  do {
                     do {
                        if (!iterator.hasNext()) {
                           return;
                        }

                        setting = (Setting)iterator.next();
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

                  for(Iterator<String> iterator2 = ((MultiBooleanSetting)setting).values.iterator(); iterator2.hasNext(); ++i) {
                     String s = iterator2.next();

                      for (String str : strs) {
                          if (str.equalsIgnoreCase(s)) {
                              ((MultiBooleanSetting) setting).selectedValues.set(i, true);
                          }
                      }
                  }
               }
            }
         }
      }
   }

}
