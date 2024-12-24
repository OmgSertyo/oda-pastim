package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import java.util.Locale;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.event.packet.EventReceivePacket;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.misc.ChatUtility;

@ModuleAnnotation(name = "FTConvert", category = Category.UTIL)
public class EventConverter extends Module {


    private String convertTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = seconds % 3600 / 60;
        int remainingSeconds = seconds % 60;
        String result = "";

        if (hours > 0) {
            result += hours + " часов ";
        }
        if (minutes > 0) {
            result += minutes + " минут ";
        }
        if (remainingSeconds > 0 || result.isEmpty()) {
            result += remainingSeconds + " секунд";
        }

        return result.trim();
    }

    @EventTarget
    private void onPacket(EventReceivePacket event) {
        IPacket<?> packet = event.getPacket();
        if (packet instanceof SChatPacket chatPacket) {
            String message = chatPacket.getChatComponent().getString().toLowerCase(Locale.ROOT);

            if (message.contains("до следующего ивента:")) {
                int startIndex = message.indexOf(":") + 2;
                int endIndex = message.indexOf(" сек", startIndex);

                if (endIndex != -1) {
                    String timeString = message.substring(startIndex, endIndex).trim();
                    int timeInSeconds = Integer.parseInt(timeString);
                    String convertedTime = this.convertTime(timeInSeconds);

                    TextFormatting color = TextFormatting.GREEN;
                    String formattedMessage = color + "До следующего ивента: " + convertedTime;
                    ChatUtility.addChatMessage(formattedMessage);
                }
            }
        }
    }
}