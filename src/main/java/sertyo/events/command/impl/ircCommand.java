package sertyo.events.command.impl;

import net.minecraft.util.text.TextFormatting;
import org.json.JSONObject;
import sertyo.events.Main;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.command.CommandManager;


@Command(
        name = "irc",
        description = "Чат между юзерами"
)
public class ircCommand extends CommandAbstract {
    public void error() {
        this.sendMessage(TextFormatting.GRAY + "Ошибка в использовании" + TextFormatting.WHITE + ":");
        this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + "irc "  + TextFormatting.RED + "сообщение");
    }



    public void execute(String[] args) throws Exception {

            this.sendMessage("Error");
    }
}
