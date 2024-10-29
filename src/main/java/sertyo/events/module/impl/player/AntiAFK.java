package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.MultiBooleanSetting;
import sertyo.events.utility.Utility;


import java.util.Arrays;

@ModuleAnnotation(
   name = "AntiAFK",
   category = Category.PLAYER
)
public class AntiAFK extends Module {
   public final MultiBooleanSetting actions = new MultiBooleanSetting("Actions", Arrays.asList("Click", "Jump", "Message"), true, () -> {
      return true;
   });

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (Utility.mc.player.isAlive() && Utility.mc.player.getHealth() > 0.0F) {
         if (this.actions.get(0) && Utility.mc.player.ticksExisted % 60 == 0) {
            Utility.mc.clickMouse();
         }

         if (this.actions.get(1) && Utility.mc.player.ticksExisted % 40 == 0 && !Utility.mc.gameSettings.keyBindJump.isPressed() && Utility.mc.player.onGround) {
            Utility.mc.player.jump();
         }

         if (this.actions.get(2) && Utility.mc.player.ticksExisted % 400 == 0) {
            Utility.mc.player.sendChatMessage("/sertyo");
         }
      }

   }
}
