package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;
import me.sertyo.j2c.J2c;
import sertyo.events.Main;
import sertyo.events.command.impl.AutoPerevod;
import sertyo.events.event.packet.EventReceivePacket;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.utility.misc.ChatUtility;
import sertyo.events.utility.misc.TimerHelper;

import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@J2c
@ModuleAnnotation(name = "CasinoBotHW", category = Category.UTIL)
public class CasinoBotHW extends Module  {
    private final TimerHelper timer = new TimerHelper();
    private PlayerEntity currentPlayer;
    private int played;
    private final Random randomizer = new Random();
    int gamesPlayed = 0;
    public static BooleanSetting perevodeee = new BooleanSetting("Переводить деньги", true);

    @Override
    public void onEnable() {
            if (perevodeee.get()) {
                if (AutoPerevod.command == null) {
                    if (Minecraft.getInstance().getCurrentServerData() != null) {
                        ChatUtility.addChatMessage("Вам необходимо добавть комманду для перевода P.s например .cmh " + Main.cheatProfile.getName() + " потом после установки и если игрок проиграет будет выполнена комманда /pay (никнейм который вы указали) 60% от пройгрыша например /pay " + Main.cheatProfile.getName() + " 1488");
                    }
                    toggle();
                } else {
                    super.onEnable();
                }
            } else {
                super.onEnable();

        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (timer.hasReached(30000L)) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            String playerName = player.getScoreboardName();
            player.sendChatMessage("!Привет! Я - Казино-Бот. Проверьте свою удачу! /pay " + playerName + " количество. В случае успеха, я удвою вашу сумму. Минимальная сумма для игры - 1000 монет. Если вам ничего не написало и деньги не скинуло, вы проиграли. Игр сыграно: " + played);
            timer.reset();
        }
    }
    @EventTarget
    public void onpacket(EventReceivePacket eventPacket) {
        IPacket packet = eventPacket.getPacket();
        if (packet instanceof SChatPacket chatPacket) {
            String messageContent = chatPacket.getChatComponent().getString();


            if (messageContent.contains("отправил вам") && messageContent.startsWith("▶")) {
                Pattern senderPattern = Pattern.compile("Игрок(?:\\s|\\W)+([\\w_]+)", Pattern.CASE_INSENSITIVE);
                Matcher senderMatcher = senderPattern.matcher(messageContent);
                String sender = "";
                if (senderMatcher.find()) {
                    sender = senderMatcher.group(1);
                } else {
                    return;
                }

                Pattern amountPattern = Pattern.compile("отправил вам(?:\\s|\\W)+(\\d+(?:\\.\\d+)?)", Pattern.CASE_INSENSITIVE);
                Matcher amountMatcher = amountPattern.matcher(messageContent.toLowerCase(Locale.ROOT));

                int sum = 0;
                if (amountMatcher.find()) {
                    String extractedAmount = amountMatcher.group(1);
                    try {
                        sum = (int) Double.parseDouble(extractedAmount);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        return;
                    }
                } else {
                    return;
                }

                if (sum < 999) {
                    String message = "/msg " + sender + " Минимальная сумма для участия - 1000 монет. ставка не возращается";
                    mc.player.sendChatMessage(message);
                    return;
                }

                boolean isWinner = randomizer.nextDouble() < 0.40;

                int reward = sum * 2;
                int summa = sum;
                gamesPlayed++;

                if (isWinner) {
                    String payMessage = "/pay " + sender + " " + reward;
                    mc.player.sendChatMessage("/bal");
                    ChatUtility.addChatMessage(payMessage);
                    mc.player.sendChatMessage(payMessage);
                    played++;
                } else {
                    if (perevodeee.get()) {
                        if (AutoPerevod.command != null) {
                            double reducedReward = summa / 0.60;
                            String pankiXoy = "/pay " + AutoPerevod.command + " " + reducedReward;

                            mc.player.sendChatMessage(pankiXoy);
                        }
                    } else {

                    }
                    played++;
                        if (gamesPlayed % 10 == 0) {
                            mc.player.sendChatMessage("/m " + sender + "Вы Проиграли.");

                    }
                }
            } else {
            }
        }
    }
}
