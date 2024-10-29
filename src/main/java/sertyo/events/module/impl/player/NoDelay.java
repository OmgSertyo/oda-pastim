package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.utility.Utility;


@ModuleAnnotation(
   name = "NoDelay",
   category = Category.PLAYER
)
public class NoDelay extends Module {
   public BooleanSetting noJumpDelay = new BooleanSetting("Jump", true);
   public BooleanSetting noRightClickDelay = new BooleanSetting("Right Click", false);

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (this.noJumpDelay.get()) {
         Utility.mc.player.jumpTicks = 0;
      }

      if (this.noRightClickDelay.get()) {
         Utility.mc.rightClickDelayTimer = 0;
      }

   }

   public void onDisable() {
      if (Utility.mc.player != null) {
         Utility.mc.player.jumpTicks = 10;
         Utility.mc.rightClickDelayTimer = 6;
      }

      super.onDisable();
   }
}
