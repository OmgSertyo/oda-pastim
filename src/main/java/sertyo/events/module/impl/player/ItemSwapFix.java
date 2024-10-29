package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.server.SHeldItemChangePacket;
import sertyo.events.event.packet.EventReceivePacket;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.Utility;


@ModuleAnnotation(
   name = "ItemSwapFix",
   category = Category.PLAYER
)
public class ItemSwapFix extends Module {
   @EventTarget
   public void onPacket(EventReceivePacket eventPacket) {
      if (eventPacket.getPacket() instanceof SHeldItemChangePacket) {
         SHeldItemChangePacket packetHeldItemChange = (SHeldItemChangePacket) eventPacket.getPacket();
         if (packetHeldItemChange.getHeldItemHotbarIndex() != Utility.mc.player.inventory.currentItem) {
            Utility.mc.player.connection.sendPacket(new CHeldItemChangePacket(Utility.mc.player.inventory.currentItem));
            eventPacket.setCancelled(true);
         }
      }

   }
}
