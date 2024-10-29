package sertyo.events.manager.staff;

import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.util.ArrayList;
import java.util.List;

public class StaffManager {
   private final List<String> staff = new ArrayList();
   private static final File staffFile;

   public void init() throws IOException {
      if (!staffFile.exists()) {
         staffFile.createNewFile();
      } else {
         this.readStaff();
      }

   }

   public void addStaff(String name) {
      this.staff.add(name);
      this.updateFile();
   }

   public boolean isStaff(String name) {
      return this.staff.stream().anyMatch((isStaff) -> {
         return isStaff.equals(name);
      });
   }

   public void removeStaff(String name) {
      this.staff.removeIf((staff1) -> {
         return staff1.equalsIgnoreCase(name);
      });
   }

   public void clearStaff() {
      this.staff.clear();
      this.updateFile();
   }

   public void updateFile() {
      try {
         StringBuilder builder = new StringBuilder();
         this.staff.forEach((staff1) -> {
            builder.append(staff1).append("\n");
         });
         Files.write(staffFile.toPath(), builder.toString().getBytes(), new OpenOption[0]);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   private void readStaff() {
      try {
         BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(staffFile.getAbsolutePath()))));

         String line;
         while((line = reader.readLine()) != null) {
            this.staff.add(line);
         }

         reader.close();
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public List<String> getStaff() {
      return this.staff;
   }

   static {
      staffFile = new File(Minecraft.getInstance().gameDir, "\\Neiron\\staff.neiron");
   }
}
