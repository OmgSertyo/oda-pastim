package sertyo.events.module.impl.util;


import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.event.packet.EventPacket;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.utility.misc.ChatUtility;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@ModuleAnnotation(name = "EventHelperFT", category = Category.UTIL)
public class EventHelperFunTime extends Module {

    private final BooleanSetting autoGps = new BooleanSetting("Авто точка", true);
    private final BooleanSetting event = new BooleanSetting("/event delay", true);
    private final BooleanSetting died = new BooleanSetting("Смерть", true);





    public enum Type {
        EVENT,
        DEATH
    }

    @EventTarget
    public void onPacket(EventPacket e) {
        if (mc.player == null || mc.world == null) return;
        if (autoGps.get()) {
            if (e.getPacket() instanceof SChatPacket p) {
                String raw = p.getChatComponent().getString().toLowerCase(Locale.ROOT);
                if (raw.contains("координатах") && raw.contains("╔══╦══╦══╦══╦══╦══╦══╦══╗") && mc.getCurrentServerData().serverIP.equals("mc.funtime.su") || mc.getCurrentServerData().serverIP.equals("play.funtime.su") && autoGps.get()) {
                    String coords = extractCoordinates(raw, Type.EVENT);
                    mc.player.sendChatMessage(".gps add " + coords);
                    ChatUtility.addChatMessage(TextFormatting.GREEN + "Поставил точку \"Событие\" на " + coords);
                }
                if (raw.contains("координаты") && raw.contains("||") && mc.getCurrentServerData().serverIP.equals("mc.funtime.su") || mc.getCurrentServerData().serverIP.equals("play.funtime.su") && event.get()) {
                    String coords = extractCoordinates(raw, Type.EVENT);
                    mc.player.sendChatMessage(".gps add " + coords);
                    ChatUtility.addChatMessage(TextFormatting.GREEN + "Поставил точку \"Событие\" на " + coords);
                }
                if (raw.contains("вы погибли") && raw.contains("вас убил") && mc.getCurrentServerData().serverIP.equals("mc.funtime.su") || mc.getCurrentServerData().serverIP.equals("play.funtime.su") && died.get()) {
                    String coords = extractCoordinates(raw, Type.DEATH);
                    mc.player.sendChatMessage(".gps add " + coords);
                    ChatUtility.addChatMessage(TextFormatting.GREEN + "Поставил точку \"Смерть\" на " + coords);
                }
            }
        }
    }

    public static String extractCoordinates(String input, Type type) {
        Pattern pattern;
        if (type == Type.EVENT) {
            pattern = Pattern.compile("\\[(-?\\d+)\\s+(-?\\d+)\\s+(-?\\d+)\\]");
        } else if (type == Type.DEATH) {
            pattern = Pattern.compile("\\[(-?\\d+\\.\\d+),\\s*(-?\\d+\\.\\d+),\\s*(-?\\d+\\.\\d+)\\]");
        } else {
            return null;
        }

        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            if (type == Type.EVENT) {
                return matcher.group(1) + " " + matcher.group(2) + " " + matcher.group(3);
            } else if (type == Type.DEATH) {
                int x = (int) Math.floor(Double.parseDouble(matcher.group(1)));
                int y = (int) Math.floor(Double.parseDouble(matcher.group(2)));
                int z = (int) Math.floor(Double.parseDouble(matcher.group(3)));
                return x + " " + y + " " + z;
            }
        }
        return null;
    }
}