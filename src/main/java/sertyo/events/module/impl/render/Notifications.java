package sertyo.events.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import sertyo.events.Main;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.manager.notification.Notification;
import sertyo.events.manager.notification.NotificationManager;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.render.animation.Animation;
import sertyo.events.utility.render.animation.Direction;
import sertyo.events.utility.font.Fonts;


import java.util.Iterator;


@ModuleAnnotation(
   name = "Notifications",
   category = Category.RENDER
)
public class Notifications extends Module {
   @EventTarget
   public void onRender2D(EventRender2D event) {
      float offset = 0.0F;
      float notificationHeight = 28.0F;
      int scaledWidth = Main.getInstance().getScaleMath().calc(event.getResolution().getScaledWidth());
      int scaledHeight = Main.getInstance().getScaleMath().calc(event.getResolution().getScaledHeight());
      Main.getInstance().getScaleMath().pushScale();
      Iterator var6 = NotificationManager.getNotifications().iterator();

      while(var6.hasNext()) {
         Notification notification = (Notification)var6.next();
         Animation animation = notification.getAnimation();
         animation.setDirection(notification.getTimerHelper().hasReached((double)notification.getTime()) ? Direction.BACKWARDS : Direction.FORWARDS);
         if (animation.finished(Direction.BACKWARDS)) {
            NotificationManager.getNotifications().remove(notification);
         } else {
            float notificationWidth = (float)(Math.max(Fonts.msBold[18].getWidth(notification.getTitle()), Fonts.msBold[15].getWidth(notification.getDescription())) + 30);
            float x = (float)scaledWidth - (notificationWidth + 5.0F) * animation.getOutput();
            float y = (float)scaledHeight - (offset + 18.0F + notificationHeight);
            notification.render(x, y, notificationWidth, notificationHeight, animation.getOutput());
            offset += (notificationHeight + 8.0F) * animation.getOutput();
         }
      }

      Main.getInstance().getScaleMath().popScale();
   }
}
