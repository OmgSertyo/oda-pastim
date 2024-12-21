package org.luaj.vm2.customs;

import sertyo.events.event.packet.EventReceivePacket;
import sertyo.events.event.packet.EventSendPacket;
import sertyo.events.event.player.EventMotion;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.event.render.EventRender3D;

public class EventHook {

    public EventUpdate eventUpdate;
    public EventMotion eventMotion;
    public EventRender2D eventRender2D;
    public EventRender3D eventRender3D;
    public EventReceivePacket eventReceivePacket;
    public EventSendPacket eventSendPacket;

    public EventHook(EventUpdate event) {
        this.eventUpdate = event;
    }

    public EventHook(EventMotion event) {
        this.eventMotion = event;
    }

    public EventHook(EventRender2D event) {
        this.eventRender2D = event;
    }

    public EventHook(EventRender3D event) {
        this.eventRender3D = event;
    }

    public EventHook(EventReceivePacket event) {
        this.eventReceivePacket = event;
    }

    public EventHook(EventSendPacket event) {
        this.eventSendPacket = event;
    }



    public String getName() {
        return "default";
    }
}
