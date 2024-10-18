package sertyo.events.utility.misc;

import net.minecraft.util.Util;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.Main;
import sertyo.events.utility.Utility;


import static sertyo.events.utility.Utility.mc;

public class ChatUtility implements Utility {
   public static String chatPrefix;

   public static void addChatMessage(String message) {
      mc.player.sendMessage(new StringTextComponent(chatPrefix + message), Util.DUMMY_UUID);

   }

   static {
      chatPrefix = TextFormatting.DARK_GRAY + "[" + TextFormatting.BLUE + TextFormatting.BOLD + Main.name + TextFormatting.DARK_GRAY + "] >> " + TextFormatting.RESET;
   }
}
