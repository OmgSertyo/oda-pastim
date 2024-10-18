package sertyo.events.command.impl;

import net.minecraft.util.text.TextFormatting;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.command.CommandManager;

@Command(
        name = "prefix",
        description = "Изменяет префикс для команд"
)
public class PrefixCommand extends CommandAbstract {
   public void error() {
      this.sendMessage(TextFormatting.GRAY + "Ошибка в использовании" + TextFormatting.WHITE + ":");
      this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "prefix " + TextFormatting.GRAY + "<" + TextFormatting.RED + "symbol" + TextFormatting.GRAY + ">");
   }

   public void execute(String[] args) throws Exception {
      CommandManager.setPrefix(args[1]);
      this.sendMessage(TextFormatting.GRAY + "Префикс успешно изменен на " + TextFormatting.RED + args[1]);
   }
}
