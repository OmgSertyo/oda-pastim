package sertyo.events.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CEntityActionPacket;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.move.MovementUtility;
@ModuleAnnotation(name = "ElytraFly", category = Category.MOVEMENT)
public class ElytraRecast extends Module {

   @EventTarget
    public void onUpdate(EventUpdate e) {
        if (!mc.player.isElytraFlying() && mc.player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() == Items.ELYTRA && MovementUtility.isMoving()) {
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.START_FALL_FLYING));
            mc.player.startFallFlying();
        }

        if (mc.player.isOnGround() && mc.player.isElytraFlying()) {
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                // rotate pitch -45 (10 тиков)
                mc.player.setRotationYawHead(-45);
            } else {
                // rotate pitch 90 (2 тика)
                mc.player.setRotationYawHead(90);
                mc.player.jump();
                mc.player.motion.y = 0.08;
            }
        }
    }
}