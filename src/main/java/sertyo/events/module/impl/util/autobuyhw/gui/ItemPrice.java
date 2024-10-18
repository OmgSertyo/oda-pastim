package sertyo.events.module.impl.util.autobuyhw.gui;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ItemPrice {
  private static final Map<String, Double> itemPrices = new HashMap<>();
  
  private static final File CONFIG_FILE = new File(Minecraft.getInstance().gameDir + "/config/itemPrices.json");
  
  public static void savePrices() {
    if (!CONFIG_FILE.exists()) {
      CONFIG_FILE.getParentFile().mkdirs(); // Создает директории, если они не существуют
        try {
            CONFIG_FILE.createNewFile(); // Создает сам файл
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
      (new Gson()).toJson(itemPrices, writer);
    } catch (IOException e) {
      e.printStackTrace();
    } 
  }
  
  public static void loadPrices() {
    if (!CONFIG_FILE.exists()) {
      CONFIG_FILE.getParentFile().mkdirs(); // Создает директории, если они не существуют
      try {
        CONFIG_FILE.createNewFile(); // Создает сам файл
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    if (CONFIG_FILE.exists())
      try (FileReader reader = new FileReader(CONFIG_FILE)) {
        Type type = (new TypeToken<Map<String, Double>>() {
          
          }).getType();
        Map<String, Double> loadedPrices = (Map<String, Double>)(new Gson()).fromJson(reader, type);
        itemPrices.clear();
        itemPrices.putAll(loadedPrices);
      } catch (IOException e) {
        e.printStackTrace();
      }  
  }
  
  public static void setPrice(String itemName, double price) {
    if (!CONFIG_FILE.exists()) {
      CONFIG_FILE.getParentFile().mkdirs(); // Создает директории, если они не существуют
      try {
        CONFIG_FILE.createNewFile(); // Создает сам файл
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    itemPrices.put(itemName, Double.valueOf(price));
  }
  
  public static double getPrice(String itemName) {
    if (!CONFIG_FILE.exists()) {
      CONFIG_FILE.getParentFile().mkdirs(); // Создает директории, если они не существуют
      try {
        CONFIG_FILE.createNewFile(); // Создает сам файл
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
    return ((Double)itemPrices.getOrDefault(itemName, Double.valueOf(0.0D))).doubleValue();
  }
}
