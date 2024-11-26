//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.module;

import sertyo.events.utility.render.animation.Animation;
import sertyo.events.utility.render.animation.impl.DecelerateAnimation;

public enum Category {
   COMBAT("Combat", "a"),
   MOVEMENT("Movement", "b"),
   PLAYER("Player", "c"),
   RENDER("Render", "d"),
   UTIL("Util", "e"),
   CONFIGS("Configs", "f", true),
   PROFILE("Profile", "g"),
   THEMES("Themes", "h", true);

   private final String name;
   private final String icon;
   private boolean bottom = false;
   private final Animation animation = new DecelerateAnimation(340, 1.0F);

    Category(String name, String icon) {
      this.name = name;
      this.icon = icon;
   }

    Category(String name, String icon, boolean bottom) {
      this.name = name;
      this.icon = icon;
      this.bottom = bottom;
   }

   public String getName() {
      return this.name;
   }

   public String getIcon() {
      return this.icon;
   }

   public boolean isBottom() {
      return this.bottom;
   }

   public Animation getAnimation() {
      return this.animation;
   }
}
