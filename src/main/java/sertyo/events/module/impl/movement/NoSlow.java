//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.util.Hand;
import sertyo.events.event.packet.EventPacket;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.ModeSetting;


@ModuleAnnotation(
        name = "NoSlow",
        category = Category.MOVEMENT
)
public class NoSlow extends Module {
   public final ModeSetting mode = new ModeSetting("Mode", "Matrix", new String[]{"Matrix", "Grim", "Funtime water"});

   public NoSlow() {
   }
   @EventTarget
   public void onPacket(EventPacket event) {
      switch (this.mode.get()) {
         case "Funtime water":
            if(mc.player.isSwimming() && mc.player.isInWater()) {
               boolean isCurrentlyFalling = mc.player.fallDistance > 0.725;
               float multiplier;

               event.setCancelled(true);

               if (mc.player.isOnGround() && !mc.player.movementInput.jump) {
                  if (mc.player.ticksExisted % 2 == 0) {
                     boolean isStrafing = mc.player.moveStrafing != 0.0F;
                     multiplier = isStrafing ? 0.4F : 0.5F;
                     mc.player.motion.x *= multiplier;
                     mc.player.motion.z *= multiplier;
                  }
               } else if (isCurrentlyFalling) {
                  boolean isSpeedyFalling = mc.player.fallDistance > 1.4;
                  multiplier = isSpeedyFalling ? 0.95F : 0.97F;
                  mc.player.motion.x *= multiplier;
                  mc.player.motion.z *= multiplier;
               }
            }
      }
   }
   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.player.isHandActive()) {
         switch (this.mode.get()) {

            case "Matrix":
               ClientPlayerEntity var10000;
               float motionMultiplier;
               if (mc.player.onGround && !mc.player.movementInput.jump) {
                  if (mc.player.ticksExisted % 2 == 0) {
                     motionMultiplier = mc.player.moveStrafing == 0.0F ? 0.5F : 0.4F;
                     var10000 = mc.player;
                     var10000.motion.x *= (double)motionMultiplier;
                     var10000 = mc.player;
                     var10000.motion.z *= (double)motionMultiplier;
                  }
               } else if ((double)mc.player.fallDistance > 0.725) {
                  motionMultiplier = mc.player.moveStrafing == 0.0F ? 0.95F : 0.97F;
                  var10000 = mc.player;
                  var10000.motion.x *= (double)motionMultiplier;
                  var10000 = mc.player;
                  var10000.motion.z *= (double)motionMultiplier;
               }
               break;
            case "Grim":
               if (mc.player.getActiveHand() == Hand.OFF_HAND) {
                  mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem % 8 + 1));
                  mc.player.connection.sendPacket(new CHeldItemChangePacket(mc.player.inventory.currentItem));
               }
         }
      }

   }

   public boolean isNeeded() {
      return !this.mode.get().equals("Grim") || mc.player.getActiveHand() == Hand.OFF_HAND;
   }
}
