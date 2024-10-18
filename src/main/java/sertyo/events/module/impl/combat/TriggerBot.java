package sertyo.events.module.impl.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.RayTraceResult;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;

@ModuleAnnotation(name = "TriggerBot", category = Category.COMBAT)
public class TriggerBot extends Module {

    private BooleanSetting onlyCrits = new BooleanSetting("Только криты", true);

    public TriggerBot() {
    }

    @EventTarget
    public void onTick(EventUpdate e) {
        for (AbstractClientPlayerEntity entity : mc.world.getPlayers()) {
            if (entity == mc.player) continue;

            if (mc.objectMouseOver.getType() == RayTraceResult.Type.ENTITY && canAttack()) {
                mc.clickMouse();
            }
        }
    }

    public boolean canAttack() {
        final boolean reasonForAttack = mc.player.isPotionActive(Effects.BLINDNESS) || mc.player.isOnLadder() || mc.player.isInWater() && mc.player.areEyesInFluid(FluidTags.WATER) || mc.player.isRidingHorse() || mc.player.abilities.isFlying || mc.player.isElytraFlying();

        if (mc.player.getCooledAttackStrength(1.5F) < 0.93F) {
            return false;
        }

        if (!reasonForAttack && onlyCrits.get()) {
            return !mc.player.isOnGround() && mc.player.fallDistance > 0.0F;
        }
        return true;
    }

}