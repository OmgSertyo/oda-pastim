//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.manager.notification;

import sertyo.events.Main;
import sertyo.events.utility.misc.TimerHelper;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.Animation;
import sertyo.events.utility.render.animation.impl.DecelerateAnimation;
import sertyo.events.utility.font.Fonts;


import java.awt.*;
import java.util.Objects;

public class Notification {
   private final NotificationType type;
   private final String title;
   private final String description;
   private final float time;
   private final TimerHelper timerHelper;
   private final Animation animation;

   public Notification(NotificationType type, String title, String description) {
      this(type, title, description, NotificationManager.getDefaultTime());
   }

   public Notification(NotificationType type, String title, String description, float time) {
      this.timerHelper = new TimerHelper();
      this.type = type;
      this.title = title;
      this.description = description;
      this.time = time;
      this.animation = new DecelerateAnimation(350, 1.0F);
   }

   public void render(float x, float y, float width, float height, float alpha) {
      Color bgColor = Color.decode("#1B1C2C");
      Color strokeColor = Color.decode("#26273B");
      Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
      Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      RenderUtil.Render2D.drawShadow(x + 1.0F, y - 1.0F, width, height + 2.0F, 5, ColorUtil.applyOpacity(strokeColor, alpha).getRGB(), ColorUtil.applyOpacity(bgColor, alpha).getRGB());
      RenderUtil.Render2D.drawRoundedRect(x + 1.0F, y - 1.0F, width, height + 2.0F, 5, ColorUtil.applyOpacity(strokeColor, alpha).getRGB());
      RenderUtil.Render2D.drawRoundedRect(x + 2.0F, y, width - 2.0F, height, 5, ColorUtil.applyOpacity(bgColor, alpha).getRGB());
      if (Objects.equals(this.type.getIcon(), NotificationType.SUCCESS.getIcon())) {
         Fonts.icomoon[24].drawString("b", x + 9.0F, y + 8.5F, Color.GREEN.getRGB());
         RenderUtil.Render2D.drawShadow(x + 9.0F, y + 6.5F, (float)Fonts.icomoon[24].getWidth("b"), (float)(Fonts.icomoon[24].getFontHeight() + 3), 7, new Color(76, 122, 52, 84).getRGB());
      } else if (Objects.equals(this.type.getIcon(), NotificationType.WARNING.getIcon())) {
         Fonts.icomoon[24].drawString("a", x + 8.0F, y + 8.5F, Color.RED.getRGB());
         RenderUtil.Render2D.drawShadow(x + 8.0F, y + 6.5F, (float)Fonts.icomoon[24].getWidth("a"), (float)(Fonts.icomoon[24].getFontHeight() + 3), 7, new Color(159, 55, 68, 84).getRGB());
      }

      Fonts.msBold[15].drawString(this.title, x + 15.0F + (float)Fonts.icomoon[21].getWidth(this.type.getIcon()), y + 6.0F, (new Color(Color.WHITE.getRGB())).getRGB());
      Fonts.msBold[14].drawString(this.description, x + 15.0F + (float)Fonts.icomoon[21].getWidth(this.type.getIcon()), y + 16.0F, ColorUtil.applyOpacity(Color.WHITE, alpha).getRGB());
   }

   public NotificationType getType() {
      return this.type;
   }

   public String getTitle() {
      return this.title;
   }

   public String getDescription() {
      return this.description;
   }

   public float getTime() {
      return this.time;
   }

   public TimerHelper getTimerHelper() {
      return this.timerHelper;
   }

   public Animation getAnimation() {
      return this.animation;
   }
}
