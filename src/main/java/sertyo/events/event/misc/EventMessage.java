package sertyo.events.event.misc;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventMessage extends EventCancellable {
   private String message;

   public String getMessage() {
      return this.message;
   }

   public EventMessage(String message) {
      this.message = message;
   }
}
