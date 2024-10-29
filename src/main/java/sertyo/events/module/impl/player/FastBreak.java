package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.Utility;


@ModuleAnnotation(
   name = "FastBreak",
   category = Category.PLAYER
)
public class FastBreak extends Module {
   @EventTarget
   public void onUpdate(EventUpdate eventUpdate) {
      Utility.mc.playerController.blockHitDelay = 0;
      if (Utility.mc.playerController.curBlockDamageMP > 0.8F) {
         Utility.mc.playerController.curBlockDamageMP = 1.0F;
      }

   }
}
