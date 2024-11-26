package sertyo.events.command.impl;

import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import sertyo.events.Main;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.module.Module;
import sertyo.events.module.impl.render.ClickGuiModule;

import java.util.Iterator;

@Command(
        name = "bind",
        description = "Позволяет биндить модули"
)
public class BindCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(TextFormatting.GRAY + "Ошибка в использовании" + TextFormatting.WHITE + ":");
      this.sendMessage(TextFormatting.WHITE + ".bind add" + TextFormatting.GRAY + "<" + TextFormatting.RED + "name" + TextFormatting.GRAY + "> <" + TextFormatting.RED + "key" + TextFormatting.GRAY + "> - забиндить модуль");
      this.sendMessage(TextFormatting.WHITE + ".bind remove " + TextFormatting.GRAY + "<" + TextFormatting.RED + "name" + TextFormatting.GRAY + "> " + TextFormatting.WHITE + "none" + TextFormatting.GRAY + " - разбиндить модуль");
      this.sendMessage(TextFormatting.WHITE + ".bind list" + TextFormatting.GRAY + " - список всех биндов");
      this.sendMessage(TextFormatting.WHITE + ".bind clear" + TextFormatting.GRAY + " - очистить все бинды");
   }
   private void listBoundKeys() {
      sendMessage(TextFormatting.GRAY + "Список всех модулей с привязанными клавишами:");
      for (Module f : Main.getInstance().getModuleManager().getModules()) {
         if (f.bind == 0) continue;
         sendMessage(f.name + " [" + TextFormatting.GRAY + (GLFW.glfwGetKeyName(f.bind, -1) == null ? "" : GLFW.glfwGetKeyName(f.bind, -1)) + TextFormatting.RESET + "]");
      }
   }
   private void clearAllBindings() {
      for (Module f : Main.getInstance().getModuleManager().getModules()) {
         f.bind = 0;
      }
      sendMessage(TextFormatting.GREEN + "Все клавиши были отвязаны от модулей");
   }
   private void addKeyBinding(String moduleName, String keyName) {
      Integer key = null;

      try {
         key = KeyMappings.keyMap.get(keyName.toUpperCase());
      } catch (Exception e) {
         e.printStackTrace();
      }
      Module module = Main.getInstance().getModuleManager().getModule(moduleName);
      if (key != null) {
         if (module != null) {
            module.bind = key;
            sendMessage("Клавиша " + TextFormatting.GRAY + keyName + TextFormatting.WHITE + " была привязана к модулю " + TextFormatting.GRAY + module.name);
         } else {
            sendMessage("Модуль " + moduleName + " не был найден");
         }
      } else {
         sendMessage("Клавиша " + keyName + " не была найдена!");
      }
   }
   private void removeKeyBinding(String moduleName, String keyName) {
      for (Module f : Main.getInstance().getModuleManager().getModules()) {
         if (f.name.equalsIgnoreCase(moduleName)) {
            f.bind = 0;
            sendMessage("Клавиша " + TextFormatting.GRAY + keyName + TextFormatting.RESET + " была отвязана от модуля " + TextFormatting.GRAY + f.name);
         }
      }
   }
   public void execute(String[] args) throws Exception {
      try {
         if (args.length >= 2) {
            switch (args[1].toLowerCase()) {
               case "list" -> listBoundKeys();
               case "clear" -> clearAllBindings();
               case "add" -> {
                  if (args.length >= 4) {
                     addKeyBinding(args[2], args[3]);
                  } else {
                     error();
                  }
               }
               case "remove" -> {
                  if (args.length >= 4) {
                     removeKeyBinding(args[2], args[3]);
                  } else {
                     error();
                  }
               }
               default -> error();
            }
         } else {
            error();
         }
      } catch (Exception e) {
         e.printStackTrace();
      }

   }
}
