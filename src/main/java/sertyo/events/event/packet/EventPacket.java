package sertyo.events.event.packet;

import com.darkmagician6.eventapi.events.Event;
import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.network.IPacket;

public class EventPacket extends EventCancellable implements Event {

    private IPacket packet;

    public EventPacket(IPacket packet) {
        this.packet = packet;
    }

    public IPacket getPacket() {
        return packet;
    }



}
