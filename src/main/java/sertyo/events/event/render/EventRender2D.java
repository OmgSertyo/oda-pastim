package sertyo.events.event.render;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.MainWindow;

public class EventRender2D implements Event {
   private MainWindow resolution;
   private float partialTicks;

   public MainWindow getResolution() {
      return this.resolution;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }

   public EventRender2D(MainWindow resolution, float partialTicks) {
      this.resolution = resolution;
      this.partialTicks = partialTicks;
   }
}
