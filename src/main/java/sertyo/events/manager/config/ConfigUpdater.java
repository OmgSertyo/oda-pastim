package sertyo.events.manager.config;

import com.google.gson.JsonObject;

public interface ConfigUpdater {
   JsonObject save();

   void load(JsonObject var1);
}
