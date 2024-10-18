//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.utility;

import sertyo.events.Main;
import discordrpc.DiscordEventHandlers;
import discordrpc.DiscordRPC;
import discordrpc.DiscordRichPresence;
import discordrpc.helpers.RPCButton;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import javax.imageio.ImageIO;

public class DiscordPresence {
    private static Thread rpcThread;
    private static final long lastTimeMillis = System.currentTimeMillis();
    public static String avatarUrl;
    public static BufferedImage avatar;

    public DiscordPresence() {
    }

    public static void startDiscord() {
        DiscordEventHandlers eventHandlers = (new DiscordEventHandlers.Builder()).ready((user) -> {
            if (user.avatar != null) {
                avatarUrl = "https://cdn.discordapp.com/avatars/" + user.userId + "/" + user.avatar + ".png";

                try {
                    URLConnection url = (new URL(avatarUrl)).openConnection();
                    url.setRequestProperty("User-Agent", "Mozilla/5.0");
                    avatar = ImageIO.read(url.getInputStream());
                } catch (Exception var2) {
                }
            }

        }).build();
        DiscordRPC.INSTANCE.Discord_Initialize("1218150697892184124", eventHandlers, true, "");
        rpcThread = new Thread(() -> {
            while(true) {
                DiscordRPC.INSTANCE.Discord_RunCallbacks();
                updatePresence();

                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException var1) {
                }
            }
        });
        rpcThread.start();
    }

    public static void shutdownDiscord() {
        if (rpcThread != null) {
            rpcThread.interrupt();
            DiscordRPC.INSTANCE.Discord_Shutdown();
        }

    }

    private static void updatePresence() {
        DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder();
        builder.setStartTimestamp(lastTimeMillis / 1000L);
        builder.setState("Build: #" + Main.version + " (" + Main.build + ")");
        builder.setLargeImage("logo");
        if (avatarUrl != null) {
            String var10001 = avatarUrl;
            String var10002 = "Sertyo";
            builder.setSmallImage(var10001, var10002 + " / " + "Developer");
        }

        //builder.setButtons(RPCButton.create("�������", "https://t.me/neironoffdlc"), RPCButton.create("��� �����", "https://www.youtube.com/watch?v=0t2Yl1Sl-ZU"));
        DiscordRPC.INSTANCE.Discord_UpdatePresence(builder.build());
    }
}
