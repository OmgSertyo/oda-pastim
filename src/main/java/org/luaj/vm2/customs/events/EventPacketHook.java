package org.luaj.vm2.customs.events;

import net.minecraft.network.play.client.CChatMessagePacket;
import net.minecraft.network.play.server.SChatPacket;
import org.luaj.vm2.customs.EventHook;
import sertyo.events.event.packet.EventReceivePacket;
import sertyo.events.event.packet.EventSendPacket;

public class EventPacketHook extends EventHook {

    private EventSendPacket sendPacket;
    private EventReceivePacket receivePacket;

    public EventPacketHook(EventSendPacket event) {
        super(event);
        this.sendPacket = event;
    }

    public EventPacketHook(EventReceivePacket event) {
        super(event);
        this.receivePacket = event;
    }

    public boolean isServerPacket(String string) {
        return switch (string.toLowerCase()) {
            case "spacketchat" -> receivePacket.getPacket() instanceof SChatPacket;
            default -> false;
        };
    }

    public boolean isClientPacket(String string) {
        return switch (string.toLowerCase()) {
            case "cpacketchatmessage" -> sendPacket.getPacket() instanceof CChatMessagePacket;
            default -> false;
        };
    }

    public void cancelSend() {
        sendPacket.setCancelled(true);
    }

    public void cancelReceive() {
        receivePacket.setCancelled(true);
    }


    @Override
    public String getName() {
        return "packet_event";
    }
}
