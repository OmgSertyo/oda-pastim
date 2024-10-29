package sertyo.events.command.impl;


import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.Main;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.command.CommandManager;
import sertyo.events.module.impl.render.StaffList;

@Command(
   name = "staff",
   description = "????????? ???????? ??????? ? StaffList"
)
public class StaffCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(TextFormatting.GRAY + "?????? ? ?????????????:");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff add " + TextFormatting.GRAY + "<" + TextFormatting.RED + "name" + TextFormatting.GRAY + "> - Добавляет чертил в StaffList");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff remove " + TextFormatting.GRAY + "<" + TextFormatting.RED + "name" + TextFormatting.GRAY + "> - Удаляет чертилу из StaffList");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff list" + TextFormatting.GRAY + " - Выводит чертил из StaffList");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "staff clear" + TextFormatting.GRAY + " - Удаляет всех чертил из StaffList");
   }

   public void execute(String[] args) throws Exception {
      if (args.length > 1) {
         String var2 = args[1];
         byte var3 = -1;
         switch(var2.hashCode()) {
         case -934610812:
            if (var2.equals("remove")) {
               var3 = 1;
            }
            break;
         case 96417:
            if (var2.equals("add")) {
               var3 = 0;
            }
            break;
         case 3322014:
            if (var2.equals("list")) {
               var3 = 3;
            }
            break;
         case 94746189:
            if (var2.equals("clear")) {
               var3 = 2;
            }
         }

         switch(var3) {
         case 0:
            if (args[2].equalsIgnoreCase(Minecraft.getInstance().getSession().getUsername())) {
               this.sendMessage(TextFormatting.GRAY + "Вы не можете добавить себя в StaffList");
            } else if (Main.getInstance().getStaffManager().getStaff().contains(args[2])) {
               this.sendMessage(TextFormatting.GRAY + "Чертила " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " уже есть в StaffList.");
            } else {
               Main.getInstance().getStaffManager().addStaff(args[2]);
               this.sendMessage(TextFormatting.GRAY + "Чертила " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " был добавлен в StaffList!");
               StaffList.updateList();
            }
            break;
         case 1:
            if (Main.getInstance().getStaffManager().isStaff(args[2])) {
               Main.getInstance().getStaffManager().removeStaff(args[2]);
               this.sendMessage(TextFormatting.GRAY + "Игрок" + TextFormatting.RED + args[2] + TextFormatting.GRAY + " успешно удалён из StaffList.");
               StaffList.updateList();
            } else {
               this.sendMessage(TextFormatting.GRAY + "Игрока " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " нет в StaffList.");
            }
            break;
         case 2:
            if (Main.getInstance().getStaffManager().getStaff().isEmpty()) {
               this.sendMessage(TextFormatting.GRAY + "StaffList пуст!");
            } else {
               Main.getInstance().getStaffManager().clearStaff();
               this.sendMessage(TextFormatting.GRAY + "StaffList очищен!");
               StaffList.updateList();
            }
            break;
         case 3:
            if (Main.getInstance().getStaffManager().getStaff().isEmpty()) {
               this.sendMessage(TextFormatting.GRAY + "StaffList пуст!");
               return;
            }

            this.sendMessage(TextFormatting.GRAY + "Staff: ");
            Main.getInstance().getStaffManager().getStaff().forEach(this::sendMessage);
            break;
         default:
            this.error();
         }
      } else {
         this.error();
      }

   }
}
