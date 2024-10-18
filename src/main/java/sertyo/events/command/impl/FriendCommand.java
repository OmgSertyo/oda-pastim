package sertyo.events.command.impl;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.Minecraft;
import sertyo.events.Main;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.command.CommandManager;

@Command(
   name = "friend",
   description = "????????? ????????? ??????? ??????"
)
public class FriendCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(TextFormatting.GRAY + "?????? ? ?????????????:");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "friend add " + TextFormatting.GRAY + "<" + TextFormatting.RED + "name" + TextFormatting.GRAY + "> - ???????? ?????? ? ??????");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "friend remove " + TextFormatting.GRAY + "<" + TextFormatting.RED + "name" + TextFormatting.GRAY + "> - ??????? ?????? ?? ??????");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "friend list" + TextFormatting.GRAY + " - ?????????? ?????? ??????");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "friend clear" + TextFormatting.GRAY + " - ???????? ?????? ??????");
   }

   public void execute(String[] args) throws Exception {
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
            this.sendMessage(TextFormatting.GRAY + "Нельзя добавить самого себя в друзья :)");
         } else if (Main.getInstance().getFriendManager().getFriends().contains(args[2])) {
            this.sendMessage(TextFormatting.GRAY + "Игрок " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " уже есть в списке друзей.");
         } else {
            Main.getInstance().getFriendManager().addFriend(args[2]);
            this.sendMessage(TextFormatting.GRAY + "Игрок " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " успешно добавлен в друзья!");
         }
         break;
      case 1:
         if (Main.getInstance().getFriendManager().isFriend(args[2])) {
            Main.getInstance().getFriendManager().removeFriend(args[2]);
            this.sendMessage(TextFormatting.GRAY + "Игрок " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " успешно удален из списка друзей.");
         } else {
            this.sendMessage(TextFormatting.GRAY + "Игрока " + TextFormatting.RED + args[2] + TextFormatting.GRAY + " нет в списке друзей.");
         }
         break;
      case 2:
         if (Main.getInstance().getFriendManager().getFriends().isEmpty()) {
            this.sendMessage(TextFormatting.GRAY + "Ты дединсайд без друзей :(");
         } else {
            Main.getInstance().getFriendManager().clearFriend();
            this.sendMessage(TextFormatting.GRAY + "Удалил всех друзей!");
         }
         break;
      case 3:
         if (Main.getInstance().getFriendManager().getFriends().isEmpty()) {
            this.sendMessage(TextFormatting.GRAY + "Ты дединсайд без друзей :(");
            return;
         }

         this.sendMessage(TextFormatting.GRAY + "Список друзей: ");
         Main.getInstance().getFriendManager().getFriends().forEach((friend) -> {
            this.sendMessage(friend.getName());
         });
      }

   }
}
