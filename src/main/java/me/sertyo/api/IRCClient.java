package me.sertyo.api;

import lombok.Getter;
import lombok.Setter;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import sertyo.events.utility.misc.ChatUtility;

import java.net.URI;
import java.util.Arrays;

import static sertyo.events.utility.Utility.mc;

public class IRCClient extends WebSocketClient {
    private static char SPLIT = '\u0000';
    @Setter
    @Getter
    private String username;
    @Getter
    @Setter
    private String client;

    public IRCClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected");
    }

    @Override
    public void onMessage(String message) {

        // Попробуйте определить правильный разделитель
        String[] split = message.split("\u0000");

            String client = split[0];
            String username = split[0];
            String msg = split[1];
            boolean active = mc.isSingleplayer();
            boolean active2 = mc.getCurrentServerData() != null;
            if (active || active2) {
                ChatUtility.addChatMessage(String.format("%s -> %s", username, msg));
            }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected");
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
    }

    public void sendMessage(String msg) {
        send( client + "\u0000" + username + "\u0000" + msg);
    }
}