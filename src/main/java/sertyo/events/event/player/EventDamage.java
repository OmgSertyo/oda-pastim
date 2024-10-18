package sertyo.events.event.player;

import com.darkmagician6.eventapi.events.Event;

public class EventDamage implements Event {
   private final DamageType damageType;

   public DamageType getDamageType() {
      return this.damageType;
   }

   public EventDamage(DamageType damageType) {
      this.damageType = damageType;
   }

   public static enum DamageType {
      FALL,
      ARROW,
      ENDER_PEARL;
   }
}
