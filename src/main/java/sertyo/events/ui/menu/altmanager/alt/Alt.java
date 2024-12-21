package sertyo.events.ui.menu.altmanager.alt;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Alt {
   private final String username;
   private String mask;
   private String password;
   private Status status;
   private String date;
   private ResourceLocation skin;

   public Alt(String username, String password, String date) {
      this(username, password, Status.Unchecked, date);
   }

   public Alt(String username, String password, Status status, String date) {
      this(username, password, "", status, date);
   }

   public Alt(String username, String password, String mask, Status status, String date) {
      this.username = username;
      this.password = password;
      this.mask = mask;
      this.status = status;
      this.date = date;
   }

   public static String getCurrentDate() {
      return (new SimpleDateFormat("dd/MM/yyyy HH:mm")).format(new Date());
   }

   public String getUsername() {
      return this.username;
   }

   public String getMask() {
      return this.mask;
   }

   public void setMask(String mask) {
      this.mask = mask;
   }

   public String getPassword() {
      return this.password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Status getStatus() {
      return this.status;
   }

   public void setStatus(Status status) {
      this.status = status;
   }

   public String getDate() {
      return this.date;
   }

   public void setDate(String date) {
      this.date = date;
   }

   public ResourceLocation getSkin() {
      return this.skin;
   }

   public void setSkin(ResourceLocation skin) {
      this.skin = skin;
   }

   public static enum Status {
      Working(TextFormatting.GREEN + "Working"),
      Banned(TextFormatting.RED + "Banned"),
      Unchecked(TextFormatting.YELLOW + "Unchecked"),
      NotWorking(TextFormatting.RED + "Not Working");

      private final String formatted;

      private Status(String string) {
         this.formatted = string;
      }

      public String toFormatted() {
         return this.formatted;
      }
   }
}
