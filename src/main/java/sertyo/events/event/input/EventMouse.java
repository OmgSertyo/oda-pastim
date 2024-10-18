package sertyo.events.event.input;

import com.darkmagician6.eventapi.events.Event;

public class EventMouse implements Event {
   private int button;

   public int getButton() {
      return this.button;
   }

   public EventMouse(int button) {
      this.button = button;
   }
}
