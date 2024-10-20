package sertyo.events.command.impl;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;
import sertyo.events.Main;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.command.CommandManager;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.Minecraft;
import sertyo.events.manager.config.ConfigManager;
import sertyo.events.utility.ab.AutoBuy;
import sertyo.events.utility.ab.manager.HistoryItem;

import static sertyo.events.utility.Utility.mc;

@Command(
        name = "ab",
        description = "Включает выключает аб"
)
public class AbCommand extends CommandAbstract {
    public static String command = null;
    public void error() {
        this.sendMessage(net.minecraft.util.text.TextFormatting.GRAY + "Ошибка в использовании" + TextFormatting.WHITE + ":");
      //  this.sendMessage(TextFormatting.WHITE + CommandManager.getPrefix() + ".cmh " + TextFormatting.GRAY + "<" + TextFormatting.RED + " nickname " + TextFormatting.GRAY + ">");

    }
    private static final List<HistoryItem> historyItem = new ArrayList<>();

    public void execute(String[] args) throws Exception {
        if (args.length >= 0) {

            boolean ah = mc.currentScreen != null && mc.currentScreen.getTitle().getString().contains("Аукцион");
                AutoBuy.enabled = !AutoBuy.enabled;
                mc.ingameGUI.getChatGUI().printChatMessage(ITextComponent.getTextComponentOrEmpty("Автобай " + (AutoBuy.enabled ? "включен." : "выключен.")));
        } else {
            this.error();
        }

    }
}

