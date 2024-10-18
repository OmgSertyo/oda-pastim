package sertyo.events.manager.config;

import com.darkmagician6.eventapi.EventManager;
import com.google.gson.JsonObject;
import sertyo.events.Main;
import sertyo.events.command.CommandManager;
import sertyo.events.manager.theme.Themes;
import sertyo.events.module.Module;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Config implements ConfigUpdater {
   private final String name;
   private final File file;

   public Config(String name) {
      this.name = name;
      this.file = new File(ConfigManager.configDirectory, name + ".neiron");
      if (!this.file.exists()) {
         try {
            this.file.createNewFile();
         } catch (IOException var3) {
            var3.printStackTrace();
         }
      }

   }

   public JsonObject save() {
      JsonObject jsonObject = new JsonObject();
      JsonObject modulesObject = new JsonObject();
      Iterator var3 = Main.getInstance().getModuleManager().getModules().iterator();

      while(var3.hasNext()) {
         Module module = (Module)var3.next();
         modulesObject.add(module.name, module.save());
      }

      jsonObject.addProperty("Prefix", CommandManager.getPrefix());
      jsonObject.addProperty("GuiTheme", Main.getInstance().getThemeManager().getCurrentGuiTheme().getName());
      jsonObject.addProperty("StyleTheme", Main.getInstance().getThemeManager().getCurrentStyleTheme().getName());
      jsonObject.add("Modules", modulesObject);
      return jsonObject;
   }

   public void load(JsonObject object) {
      if (object.has("Prefix")) {
         CommandManager.setPrefix(object.get("Prefix").getAsString());
      }

      if (object.has("GuiTheme")) {
         Main.getInstance().getThemeManager().setCurrentGuiTheme(Themes.findByName(object.get("GuiTheme").getAsString()));
      }

      if (object.has("StyleTheme")) {
         Main.getInstance().getThemeManager().setCurrentStyleTheme(Themes.findByName(object.get("StyleTheme").getAsString()));
      }

      if (object.has("Prefix")) {
         CommandManager.setPrefix(object.get("Prefix").getAsString());
      }

      if (object.has("Modules")) {
         JsonObject modulesObject = object.getAsJsonObject("Modules");
         Iterator var3 = Main.getInstance().getModuleManager().getModules().iterator();

         while(var3.hasNext()) {
            Module module = (Module)var3.next();
            EventManager.unregister(module);
            module.load(modulesObject.getAsJsonObject(module.name));
         }
      }

   }

   public String getName() {
      return this.name;
   }

   public File getFile() {
      return this.file;
   }
}
