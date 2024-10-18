package sertyo.events.command;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.event.misc.EventMessage;
import sertyo.events.utility.misc.ChatUtility;

public class CommandHandler {
   public CommandManager commandManager;

   public CommandHandler(CommandManager commandManager) {
      this.commandManager = commandManager;
   }

   @EventTarget
   public void onMessage(EventMessage event) {
      String msg = event.getMessage();
      if (msg.startsWith(CommandManager.getPrefix())) {
         event.setCancelled(true);
         if (!this.commandManager.execute(msg)) {
            ChatUtility.addChatMessage(TextFormatting.GRAY + "Такой команды не существует.");
         }
      }

   }
}
