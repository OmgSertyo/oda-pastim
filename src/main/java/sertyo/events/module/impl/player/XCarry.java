package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CCloseWindowPacket;
import sertyo.events.event.packet.EventSendPacket;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;

@ModuleAnnotation(
   name = "XCarry",
   category = Category.PLAYER
)
public class XCarry extends Module {
   @EventTarget
   public void onSendPacket(EventSendPacket event) {
      if (event.getPacket() instanceof CCloseWindowPacket) {
         event.setCancelled(true);
      }

   }
}
