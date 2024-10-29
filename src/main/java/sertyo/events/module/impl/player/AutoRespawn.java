package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.screen.Screen;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.Utility;


@ModuleAnnotation(
   name = "AutoRespawn",
   category = Category.PLAYER
)
public class AutoRespawn extends Module {
   @EventTarget
   public void onUpdateEvent(EventUpdate eventUpdate) {
      if (Utility.mc.currentScreen instanceof DeathScreen) {
         Utility.mc.player.respawnPlayer();
         Utility.mc.displayGuiScreen((Screen) null);
      }

   }
}
