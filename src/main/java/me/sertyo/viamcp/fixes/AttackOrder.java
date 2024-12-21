package me.sertyo.viamcp.fixes;

import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.RayTraceResult;
import me.sertyo.viamcp.ViaLoadingBase;
import sertyo.events.utility.Utility;

public class AttackOrder implements Utility {

    public static void sendConditionalSwing(RayTraceResult rayTraceResult, Hand hand) {
        if (rayTraceResult != null && rayTraceResult.getType() != RayTraceResult.Type.ENTITY) mc.player.swingArm(hand);
    }

    public static void sendFixedAttack(PlayerEntity entityIn, Entity target, Hand hand) {
        if (ViaLoadingBase.getInstance().getTargetVersion().isOlderThanOrEqualTo(ProtocolVersion.v1_8)) {
            mc.player.swingArm(hand);
            mc.playerController.attackEntity(entityIn, target);
        } else {
            mc.playerController.attackEntity(entityIn, target);
            mc.player.swingArm(hand);
        }
    }
}