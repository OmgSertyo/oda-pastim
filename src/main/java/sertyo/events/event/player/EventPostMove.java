package sertyo.events.event.player;

import com.darkmagician6.eventapi.events.Event;

public class EventPostMove implements Event {
   private double horizontalMove;

   public double getHorizontalMove() {
      return this.horizontalMove;
   }

   public EventPostMove(double horizontalMove) {
      this.horizontalMove = horizontalMove;
   }
}
