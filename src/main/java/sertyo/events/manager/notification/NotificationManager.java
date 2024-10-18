package sertyo.events.manager.notification;

import sertyo.events.Main;
import sertyo.events.module.impl.render.Notifications;

import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {
   private static float defaultTime = 250.0F;
   private static final CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList();

   public static void notify(NotificationType type, String title, String description) {
      notify(new Notification(type, title, description));
   }

   public static void notify(NotificationType type, String title, String description, float time) {
      notify(new Notification(type, title, description, time));
   }

   private static void notify(Notification notification) {
      if (Main.getInstance().getModuleManager().getModule(Notifications.class).isEnabled()) {
         notifications.add(notification);
      }

   }

   public static float getDefaultTime() {
      return defaultTime;
   }

   public static void setDefaultTime(float defaultTime) {
      NotificationManager.defaultTime = defaultTime;
   }

   public static CopyOnWriteArrayList<Notification> getNotifications() {
      return notifications;
   }
}
