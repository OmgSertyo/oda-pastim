package sertyo.events.command.impl;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.Main;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.command.CommandManager;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.Random;

import net.minecraft.client.Minecraft;
import sertyo.events.manager.config.ConfigManager;

@Command(
        name = "cfg",
        description = "Позволяет управлять конфигами чита"
)
public class ConfigCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(net.minecraft.util.text.TextFormatting.GRAY + "Ошибка в использовании" + TextFormatting.WHITE + ":");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "cfg load " + TextFormatting.GRAY + "<" + TextFormatting.RED + "name" + TextFormatting.GRAY + ">");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "cfg save " + TextFormatting.GRAY + "<" + TextFormatting.RED + "name" + TextFormatting.GRAY + ">");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "cfg delete " + TextFormatting.GRAY + "<" + TextFormatting.RED + "name" + TextFormatting.GRAY + ">");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "cfg list" + TextFormatting.GRAY + " - список конфигов");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "cfg dir" + TextFormatting.GRAY + " - открыть папку с конфигами");
   }
   private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz124567890";

   public static String generateRandomName(int length) {
      StringBuilder builder = new StringBuilder();
      Random random = new Random();
      for (int i = 0; i < length; i++) {
         int index = random.nextInt(ALPHA_NUMERIC_STRING.length());
         builder.append(ALPHA_NUMERIC_STRING.charAt(index));
      }
      return builder.toString();
   }
   public void execute(String[] args) throws Exception {
      if (args.length >= 2) {
         String var2 = args[1];
         byte var3 = -1;
         switch(var2.hashCode()) {
            case -1335458389:
               if (var2.equals("delete")) {
                  var3 = 4;
               }
               if (var2.equals("сlup")) {
                  var3 = 5;
               }
               break;
            case 99469:
               if (var2.equals("dir")) {
                  var3 = 0;
               }
               break;
            case 3322014:
               if (var2.equals("list")) {
                  var3 = 1;
               }
               break;
            case 3327206:
               if (var2.equals("load")) {
                  var3 = 2;
               }
               break;
            case 3522941:
               if (var2.equals("save")) {
                  var3 = 3;
               }
         }

         switch(var3) {
            case 0:
               Runtime.getRuntime().exec("explorer " + new File(Minecraft.getInstance().gameDir, "\\Neiron\\configs"));

               break;
            case 1:
               File file = new File(Minecraft.getInstance().gameDir, "\\Neiron\\configs");
               if (ConfigManager.getLoadedConfigs().isEmpty()) {
                  this.sendMessage("Конфигурации не найдены.");
                  break;
               } else {
                  this.sendMessage(TextFormatting.GREEN + "Список конфигураций: ");
                  File[] var5 = (File[])Objects.requireNonNull(file.listFiles());
                  int var6 = var5.length;

                  for(int var7 = 0; var7 < var6; ++var7) {
                     File s = var5[var7];
                     this.sendMessage(s.getName().replaceAll(".neiron", ""));
                  }

                  return;
               }
            case 2:
               if (Main.getInstance().getConfigManager().loadConfig(args[2])) {
                  this.sendMessage(TextFormatting.GRAY + "Конфигурация " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " была загружена.");
               } else {
                  this.sendMessage(TextFormatting.GRAY + "Конфигурация " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " не была загружена. Скорее всего, она не существует.");
               }
               break;
            case 3:
               Main.getInstance().getConfigManager().saveConfig(args[2]);
               this.sendMessage(TextFormatting.GRAY + "Конфигурация " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " была сохранена.");
               break;
               case 5:
               Main.getInstance().getConfigManager().saveConfig(args[2]);
               this.sendMessage(TextFormatting.GRAY + "Конфигурация " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " была загружена.");
               break;
            case 4:
               if (Main.getInstance().getConfigManager().deleteConfig(args[2])) {
                  this.sendMessage(TextFormatting.GRAY + "Конфигурация " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " была удалена.");
               } else {
                  this.sendMessage(TextFormatting.GRAY + "Конфигурация " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " не была удалена. Скорее всего, она не существует.");
               }
         }
      } else {
         this.error();
      }

   }
}
