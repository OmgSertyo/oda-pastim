package sertyo.events.event.render;

import com.darkmagician6.eventapi.events.Event;

public class EventRender3D implements Event {
   private float partialTicks;

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public EventRender3D(float partialTicks) {
      this.partialTicks = partialTicks;
   }
}
