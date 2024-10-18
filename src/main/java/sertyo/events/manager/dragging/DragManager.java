package sertyo.events.manager.dragging;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import sertyo.events.module.Module;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.HashMap;

public class DragManager {
   private static final HashMap<String, Draggable> draggables = new HashMap();
   public static final File dragablesFile;
   private static final Gson GSON;

   public void init() throws IOException {
      if (!dragablesFile.exists()) {
         dragablesFile.createNewFile();
      } else {
         this.load();
      }

   }

   public void save() {
      try {
         Files.write(dragablesFile.toPath(), GSON.toJson(draggables.values()).getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public void load() {
      try {
         Draggable[] loadedDraggables = (Draggable[])GSON.fromJson(new String(Files.readAllBytes(dragablesFile.toPath()), StandardCharsets.UTF_8), Draggable[].class);
         Draggable[] var2 = loadedDraggables;
         int var3 = loadedDraggables.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Draggable draggable = var2[var4];
            if (draggable == null) {
               return;
            }

            Draggable currentDrag = (Draggable)draggables.get(draggable.getName());
            if (currentDrag != null) {
               currentDrag.setX(draggable.getX());
               currentDrag.setY(draggable.getY());
               draggables.put(draggable.getName(), currentDrag);
            }
         }
      } catch (IOException var7) {
         var7.printStackTrace();
      }

   }

   public static Draggable create(Module module, String name, int x, int y) {
      Draggable draggable = new Draggable(module, name, x, y);
      draggables.put(name, draggable);
      return draggable;
   }

   public static HashMap<String, Draggable> getDraggables() {
      return draggables;
   }

   static {
      dragablesFile = new File(Minecraft.getInstance().gameDir, "\\Neiron\\elements.neiron");
      GSON = (new GsonBuilder()).setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
   }
}
