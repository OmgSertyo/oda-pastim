package sertyo.events.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CEntityActionPacket;
import sertyo.events.event.packet.EventSendPacket;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.utility.Utility;
import sertyo.events.utility.move.MovementUtility;


@ModuleAnnotation(
   name = "AutoSprint",
   category = Category.MOVEMENT
)
public class AutoSprint extends Module {
   public static BooleanSetting omnidirectional = new BooleanSetting("All Directions", false);

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (Utility.mc.player != null) {
         if (Utility.mc.player.getFoodStats().getFoodLevel() > 6 && !Utility.mc.player.isSneaking() && !Utility.mc.player.collidedHorizontally) {
            Utility.mc.player.setSprinting(omnidirectional.get() ? MovementUtility.isMoving() : Utility.mc.player.movementInput.forwardKeyDown);
         }

      }
   }

   @EventTarget
   public void onSendPacket(EventSendPacket event) {
      if (omnidirectional.get() && event.getPacket() instanceof CEntityActionPacket) {
         CEntityActionPacket packet = (CEntityActionPacket)event.getPacket();
         if (packet.getAction() == CEntityActionPacket.Action.START_SPRINTING) {
            event.setCancelled(true);
         }

         if (packet.getAction() == CEntityActionPacket.Action.STOP_SPRINTING) {
            event.setCancelled(true);
         }
      }

   }

   public void onDisable() {
      super.onDisable();
      if (Utility.mc.player != null) {
         Utility.mc.player.setSprinting(false);
      }
   }
}
