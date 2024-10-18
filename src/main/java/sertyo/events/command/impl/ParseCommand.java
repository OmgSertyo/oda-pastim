package sertyo.events.command.impl;

import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.Minecraft;

import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.utility.Utility;
import sertyo.events.utility.misc.ChatUtility;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Command(
   name = "parse",
   description = "Парсит школо-воров деняг с маминой карты"
)
public class ParseCommand extends CommandAbstract implements Utility {
   private static final File parseDir;

   public void error() {
   }

   public void execute(String[] args) throws Exception {
      if (mc.isSingleplayer()) {
         Collection<NetworkPlayerInfo> playerInfos = mc.player.connection.getPlayerInfoMap();
         List<String> donate = new ArrayList<>();

         File file = new File(parseDir, mc.getCurrentServerData().serverIP + ".txt");
         FileWriter fileWriter = new FileWriter(file);

         for (NetworkPlayerInfo playerInfo : playerInfos) {
            if (playerInfo.getPlayerTeam().getPrefix().getString().length() >= 3) {
               String text = TextFormatting.getTextWithoutFormattingCodes(playerInfo.getPlayerTeam().getPrefix().getString().substring(0, playerInfo.getPlayerTeam().getPrefix().getString().length() - 1));
               if (!donate.contains(text))
                  donate.add(text);
            }
         }
         if (donate.size() == 0) {
            ChatUtility.addChatMessage("Донатеры в табе не найдены!");
            return;
         }

         try {
            for (String don : donate) {
               fileWriter.append("// " + don + "\n\n");
               for (NetworkPlayerInfo playerInfo : playerInfos) {
                  if (playerInfo.getPlayerTeam().getPrefix().getString().contains(don))
                     fileWriter.append(playerInfo.getGameProfile().getName() + "\n");
               }
               fileWriter.append("\n");
            }

            fileWriter.flush();
            fileWriter.close();
            try {
               Runtime.getRuntime().exec("explorer " + parseDir);
            } catch (IOException e) {
               e.printStackTrace();
            }

            ChatUtility.addChatMessage("Успешно.");
         } catch (Exception e) {
            ChatUtility.addChatMessage("Ошибка. Обратитесь к разработчику");
            ChatUtility.addChatMessage(e.getMessage());
         }
      }
   }

   static {
      parseDir = new File(Minecraft.getInstance().gameDir, "\\Neiron\\parser");
   }
}
