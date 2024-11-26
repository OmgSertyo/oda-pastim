package sertyo.events.manager.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.FilenameUtils;
import sertyo.events.Main;
import sertyo.events.ui.menu.main.NeironMainMenu;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class ConfigManager extends Manager<Config> {
   public static final File configDirectory;
   private static final ArrayList<Config> loadedConfigs;

   public ConfigManager() {
      this.setContents(loadConfigs());
      configDirectory.mkdirs();
   }

   private static ArrayList<Config> loadConfigs() {
      File[] files = configDirectory.listFiles();
      if (files != null) {
         File[] var1 = files;
         int var2 = files.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            File file = var1[var3];
            if (FilenameUtils.getExtension(file.getName()).equals("neiron")) {
               loadedConfigs.add(new Config(FilenameUtils.removeExtension(file.getName())));
            }
         }
      }

      return loadedConfigs;
   }

   public void load() {
      if (!configDirectory.exists()) {
         configDirectory.mkdirs();
      }

      if (configDirectory != null) {
         File[] files = configDirectory.listFiles((fx) -> {
            return !fx.isDirectory() && FilenameUtils.getExtension(fx.getName()).equals("neiron");
         });
         File[] var2 = files;
         int var3 = files.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            File f = var2[var4];
            Config config = new Config(FilenameUtils.removeExtension(f.getName()).replace(" ", ""));
            loadedConfigs.add(config);
         }
      }

   }

   public boolean loadConfig(String configName) {
      if (configName == null) {
         return false;
      } else {
         Config config = this.findConfig(configName);
         if (config == null) {
            return false;
         } else {
            try {
               FileReader reader = new FileReader(config.getFile());
               JsonParser parser = new JsonParser();
               JsonObject object = (JsonObject)parser.parse(reader);
               config.load(object);
               return true;
            } catch (FileNotFoundException var6) {
               return false;
            }
         }
      }
   }

   public boolean saveConfig(String configName) {
      if (configName == null) {
         return false;
      } else {
         Config config;
         if ((config = this.findConfig(configName)) == null) {
            Config newConfig = config = new Config(configName);
            this.getContents().add(newConfig);
         }

         String contentPrettyPrint = (new GsonBuilder()).setPrettyPrinting().create().toJson(config.save());

         try {
            FileWriter writer = new FileWriter(config.getFile());
            writer.write(contentPrettyPrint);
            writer.close();
            return true;
         } catch (IOException var5) {
            return false;
         }
      }
   }

   public Config findConfig(String configName) {
      if (configName == null) {
         return null;
      } else {
         Iterator var2 = this.getContents().iterator();

         Config config;
         do {
            if (!var2.hasNext()) {
               if ((new File(configDirectory, configName + ".neiron")).exists()) {
                  return new Config(configName);
               }

               return null;
            }

            config = (Config)var2.next();
         } while(!config.getName().equalsIgnoreCase(configName));

         return config;
      }
   }

   public boolean deleteConfig(String configName) {
      if (configName == null) {
         return false;
      } else {
         Config config;
         if ((config = this.findConfig(configName)) == null) {
            return false;
         } else {
            File f = config.getFile();
            this.getContents().remove(config);
            return f.exists() && f.delete();
         }
      }
   }

   public static ArrayList<Config> getLoadedConfigs() {
      return loadedConfigs;
   }

   static {
      configDirectory = new File(Minecraft.getInstance().gameDir, "\\Neiron\\configs");
      loadedConfigs = new ArrayList();
   }
}
