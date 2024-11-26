package sertyo.events.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;


@ModuleAnnotation(name = "WaterSpeed", category = sertyo.events.module.Category.MOVEMENT)
public class WaterSpeed extends sertyo.events.module.Module {
    private static final double[] SPEED = {1.0455, 1.0505, 1.0555};
    private static final long TIMECHANGE = 300;

    private long lastChangeTime = 0;
    private int currentSpeedIndex = 0;


    @EventTarget
    public void onUpdate(EventUpdate event) {
        long currentTime = System.currentTimeMillis();
        ClientPlayerEntity player = Minecraft.getInstance().player;

        if (player != null && player.isAlive() && player.isInWater()) {
            if (currentTime - lastChangeTime >= TIMECHANGE) {
                currentSpeedIndex = (currentSpeedIndex + 1) % SPEED.length;
                lastChangeTime = currentTime;
            }
            player.addPotionEffect(new EffectInstance(Effects.DOLPHINS_GRACE, 40));
            double speedBoost = SPEED[currentSpeedIndex];
          //  player.setMotion(
          //          player.motion.x * speedBoost,
           //          player.motion.y,
           //         player.motion.z
            //);
        }
    }
}