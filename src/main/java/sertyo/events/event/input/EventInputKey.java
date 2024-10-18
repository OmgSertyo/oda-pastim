package sertyo.events.event.input;

import com.darkmagician6.eventapi.events.Event;

public class EventInputKey implements Event {
   private int key;

   public EventInputKey(int key) {
      this.key = key;
   }

   public int getKey() {
      return this.key;
   }

   public void setKey(int key) {
      this.key = key;
   }
}
