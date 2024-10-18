package sertyo.events.utility.render.animation.impl;

import sertyo.events.utility.render.animation.Animation;
import sertyo.events.utility.render.animation.Direction;

public class EaseInOutQuad extends Animation {
   public EaseInOutQuad(int ms, float endPoint) {
      super(ms, endPoint);
   }

   public EaseInOutQuad(int ms, float endPoint, Direction direction) {
      super(ms, endPoint, direction);
   }

   protected float getEquation(float x) {
      double x1 = (double)(x / (float)this.duration);
      return (float)(x1 < 0.5D ? 2.0D * Math.pow(x1, 2.0D) : 1.0D - Math.pow(-2.0D * x1 + 2.0D, 2.0D) / 2.0D);
   }
}
