package sertyo.events.command.impl;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.Main;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.command.CommandManager;


import java.io.File;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import sertyo.events.manager.config.ConfigManager;

@Command(
        name = "cmh",
        description = "Ставит комманду для казино бота в формате /pay nickname"
)
public class AutoPerevod extends CommandAbstract {
    public static String command = null;
    public void error() {
        this.sendMessage(net.minecraft.util.text.TextFormatting.GRAY + "Ошибка в использовании" + TextFormatting.WHITE + ":");
        this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + ".cmh " + TextFormatting.GRAY + "<" + TextFormatting.RED + " nickname " + TextFormatting.GRAY + ">");

    }

    public void execute(String[] args) throws Exception {
        if (args.length >= 1) {
            command = args[1];
        } else {
            this.error();
        }

    }
}
