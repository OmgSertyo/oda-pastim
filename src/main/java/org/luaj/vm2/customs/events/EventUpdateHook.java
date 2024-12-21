package org.luaj.vm2.customs.events;


import org.luaj.vm2.customs.EventHook;
import sertyo.events.event.player.EventUpdate;

public class EventUpdateHook extends EventHook {

    private EventUpdate update;

    public EventUpdateHook(EventUpdate event) {
        super(event);
        this.update = event;
    }

    public double getPosY() {
        return this.update.getPosY();
    }

    public double getPosX() {
        return this.update.getPosX();
    }

    public double getPosZ() {
        return this.update.getPosZ();
    }

    public float getRotationPitch() {
        return this.update.getRotationPitch();
    }

    public float getRotationYaw() {
        return this.update.getRotationYaw();
    }

    public boolean isOnGround() {
        return this.update.isOnGround();
    }

    public void setPosY(double posY) {
        this.update.setPosY(posY);;
    }

    public void setPosX(double posX) {
        this.update.setPosX(posX);
    }

    public void setOnGround(boolean onGround) {
        this.update.setOnGround(onGround);
    }

    public void setPosZ(double posZ) {
        this.update.setPosZ(posZ);
    }

    public void setRotationPitch(float rotationPitch) {
        this.update.setRotationPitch(rotationPitch);
    }

    public void setRotationYaw(float rotationYaw) {
        this.update.setRotationYaw(rotationYaw);
    }

    @Override
    public String getName() {
        return "update_event";
    }
}
