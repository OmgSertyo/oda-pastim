package sertyo.events.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.eventbus.Subscribe;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.network.play.client.*;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import sertyo.events.event.player.EventAction;
import sertyo.events.event.player.EventMotion;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.impl.combat.KillAura;

@ModuleAnnotation(name="Speed", category= Category.MOVEMENT)
public class Speed
        extends Module {


    public Speed() {
    }

    boolean unpPressShift = false;
    @Override
    public void onDisable() {
        if (!mc.player.isSneaking() && unpPressShift) {
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.RELEASE_SHIFT_KEY));
            unpPressShift = false;
        }
    }

    @EventTarget
    public void onMotion(EventAction e) {
        BlockPos pos = mc.player.getPosition().getY() != mc.player.getPosY() ? mc.player.getPosition() : mc.player.getPosition().down();
        if (mc.player.isOnGround() && !mc.player.movementInput.jump) {
            mc.player.connection.sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.UP));
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.PRESS_SHIFT_KEY));
            mc.player.setVelocity(mc.player.getMotion().x * 1.16, mc.player.getMotion().y, mc.player.getMotion().z * 1.16);
            unpPressShift = true;
        } else if (unpPressShift) {
            mc.player.connection.sendPacket(new CEntityActionPacket(mc.player, CEntityActionPacket.Action.RELEASE_SHIFT_KEY));
            unpPressShift = false;
        }
    }
}
