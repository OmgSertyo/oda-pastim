package sertyo.events.command.impl;

import net.minecraft.util.text.TextFormatting;
import sertyo.events.Main;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.command.CommandManager;

import java.util.Iterator;

@Command(
   name = "help",
   description = "help"
)
public class HelpCommand extends CommandAbstract {
   public void execute(String[] args) throws Exception {
      this.sendMessage(TextFormatting.GRAY + "Список команд: ");
      Iterator var2 = Main.getInstance().getCommandManager().getCommands().iterator();

      while(var2.hasNext()) {
         CommandAbstract command = (CommandAbstract)var2.next();
         if (!(command instanceof HelpCommand)) {
            this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + command.name + TextFormatting.GRAY + " - " + command.description);
         }
      }

   }

   public void error() {
      this.sendMessage(TextFormatting.RED + "Error");
   }
}
