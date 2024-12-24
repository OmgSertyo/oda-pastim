package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import net.java.games.input.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.EditSignScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.client.CClickWindowPacket;
import sertyo.events.event.packet.EventPacket;
import sertyo.events.event.packet.EventSendPacket;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.utility.Utility;
import sertyo.events.utility.misc.TimerHelper;
import sertyo.events.utility.move.MovementUtility;

import java.util.ArrayList;
import java.util.List;


@ModuleAnnotation(
   name = "InventoryMove",
   category = Category.PLAYER
)
public class InventoryMove extends Module {
   public static BooleanSetting safe = new BooleanSetting("Ft обход", false);
   public TimerHelper wait = new TimerHelper();
   private final List<IPacket<?>> packet = new ArrayList<>();
   public void onUpdate() {
      if (mc.player != null) {

         final KeyBinding[] pressedKeys = {mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack,
                 mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump,
                 mc.gameSettings.keyBindSprint};
         if (!wait.hasReached(400)) {
            for (KeyBinding keyBinding : pressedKeys) {
               keyBinding.setPressed(false);
            }
            return;
         }


         if (mc.currentScreen instanceof ChatScreen || mc.currentScreen instanceof EditSignScreen) {
            return;
         }

         updateKeyBindingState(pressedKeys);
         if(safe.get()) {
            if (!(mc.currentScreen instanceof InventoryScreen) && !packet.isEmpty() && MovementUtility.isMoving()) {
               new Thread(() -> {
                  wait.reset();
                  try {
                     Thread.sleep(100);
                  } catch (InterruptedException ex) {
                     throw new RuntimeException(ex);
                  }
                  for (IPacket p : packet) {
                     mc.player.connection.sendPacket(p);
                  }
                  packet.clear();
               }).start();
            }
         } else {
            KeyBinding[] keys = new KeyBinding[]{mc.gameSettings.keyBindForward, mc.gameSettings.keyBindBack, mc.gameSettings.keyBindLeft, mc.gameSettings.keyBindRight, mc.gameSettings.keyBindJump, mc.gameSettings.keyBindSprint};
            if (!(mc.currentScreen instanceof ChatScreen) && !(mc.currentScreen instanceof EditSignScreen)) {
               KeyBinding[] var2 = keys;
               int var3 = keys.length;

               for (int var4 = 0; var4 < var3; ++var4) {
                  KeyBinding keyBinding = var2[var4];
                  keyBinding.setPressed(InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), keyBinding.getDefault().getKeyCode()));
               }
            }
         }
      }
   }

   public void onPacket(EventSendPacket e) {
      if (e.getPacket() instanceof CClickWindowPacket p && MovementUtility.isMoving()) {
         if (mc.currentScreen instanceof InventoryScreen) {
            packet.add(p);
            e.setCancelled(true);
         }
      }
   }

   private void updateKeyBindingState(KeyBinding[] keyBindings) {
      for (KeyBinding keyBinding : keyBindings) {
         boolean isKeyPressed = InputMappings.isKeyDown(mc.getMainWindow().getHandle(), keyBinding.getDefault().getKeyCode());
         keyBinding.setPressed(isKeyPressed);
      }
   }
   @EventTarget
   public void onUpdate(EventUpdate event) {
      onUpdate();
   }
   @EventTarget
   public void onPacket2(EventSendPacket event) {
      onPacket(event);
   }
}
