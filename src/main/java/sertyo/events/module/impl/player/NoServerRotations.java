package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.server.SPlayerPositionLookPacket;
import sertyo.events.event.packet.EventReceivePacket;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.Utility;


@ModuleAnnotation(
   name = "NoServerRotations",
   category = Category.PLAYER
)
public class NoServerRotations extends Module {
   @EventTarget
   public void onPacket(EventReceivePacket eventPacket) {
      if (eventPacket.getPacket() instanceof SPlayerPositionLookPacket) {
         SPlayerPositionLookPacket packet = (SPlayerPositionLookPacket)eventPacket.getPacket();
         packet.yaw = Utility.mc.player.rotationYaw;
         packet.pitch = Utility.mc.player.rotationPitch;
      }

   }
}
