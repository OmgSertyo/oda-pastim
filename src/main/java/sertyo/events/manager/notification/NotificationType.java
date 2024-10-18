//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.manager.notification;

public enum NotificationType {
   SUCCESS("s"),
   ERROR("r"),
   INFO("s"),
   WARNING("r");

   private final String icon;

   public String getIcon() {
      return this.icon;
   }

   private NotificationType(String icon) {
      this.icon = icon;
   }
}
