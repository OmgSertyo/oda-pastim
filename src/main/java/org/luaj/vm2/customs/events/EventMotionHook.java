package org.luaj.vm2.customs.events;

import org.luaj.vm2.customs.EventHook;
import sertyo.events.event.player.EventMotion;

public class EventMotionHook extends EventHook {

    private EventMotion motion;

    public EventMotionHook(EventMotion event) {
        super(event);
        this.motion = event;
    }

    public void setYaw(float yaw) {
        motion.setYaw(yaw);
    }

    public float getYaw() {
        return motion.getYaw();
    }

    public float getPitch() {
        return motion.getPitch();
    }

    public void setPitch(float pitch) {
        motion.setPitch(pitch);
    }

    public void setGround(boolean x) {
        motion.setOnGround(x);
    }

    @Override
    public String getName() {
        return "motion_event";
    }
}
