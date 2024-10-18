package sertyo.events.utility.render.animation;

import sertyo.events.utility.misc.TimerHelper;

public abstract class Animation {
   public TimerHelper timerUtil = new TimerHelper();
   protected int duration;
   protected float endPoint;
   protected Direction direction;

   public Animation(int ms, float endPoint) {
      this.duration = ms;
      this.endPoint = endPoint;
      this.direction = Direction.FORWARDS;
   }

   public Animation(int ms, float endPoint, Direction direction) {
      this.duration = ms;
      this.endPoint = endPoint;
      this.direction = direction;
   }

   public boolean finished(Direction direction) {
      return this.isDone() && this.direction.equals(direction);
   }

   public float getLinearOutput() {
      return 1.0F - (float)this.timerUtil.getTimePassed() / (float)this.duration * this.endPoint;
   }

   public float getEndPoint() {
      return this.endPoint;
   }

   public void setEndPoint(float endPoint) {
      this.endPoint = endPoint;
   }

   public void reset() {
      this.timerUtil.reset();
   }

   public boolean isDone() {
      return this.timerUtil.hasReached((double)this.duration);
   }

   public void changeDirection() {
      this.setDirection(this.direction.opposite());
   }

   public Direction getDirection() {
      return this.direction;
   }

   public void setDirection(Direction direction) {
      if (this.direction != direction) {
         this.direction = direction;
         this.timerUtil.setTime(System.currentTimeMillis() - ((long)this.duration - Math.min((long)this.duration, this.timerUtil.getTimePassed())));
      }

   }

   public void setDuration(int duration) {
      this.duration = duration;
   }

   protected boolean correctOutput() {
      return false;
   }

   public long getTimePassed() {
      return this.timerUtil.getTimePassed();
   }

   public float getOutput() {
      if (this.direction == Direction.FORWARDS) {
         return this.isDone() ? this.endPoint : this.getEquation((float)this.timerUtil.getTimePassed()) * this.endPoint;
      } else if (this.isDone()) {
         return 0.0F;
      } else if (this.correctOutput()) {
         float revTime = (float)Math.min((long)this.duration, Math.max(0L, (long)this.duration - this.timerUtil.getTimePassed()));
         return this.getEquation(revTime) * this.endPoint;
      } else {
         return (1.0F - this.getEquation((float)this.timerUtil.getTimePassed())) * this.endPoint;
      }
   }

   protected abstract float getEquation(float var1);
}
