package sertyo.events.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.impl.combat.KillAura;
import sertyo.events.module.setting.impl.ModeSetting;
import sertyo.events.utility.Utility;

@ModuleAnnotation(
   name = "KillEffect",
   category = Category.RENDER
)
public class KillEffect extends Module {
   public ModeSetting effect = new ModeSetting("Effect", "Lightning Bolt", new String[]{"Lightning Bolt", "Blood Explosion"});

   @EventTarget
   public void onUpdate(EventUpdate event) {
      LivingEntity entity = KillAura.target;
      if (entity.getHealth() <= 0.0F || !entity.isAlive()) {
         if (this.effect.get().equals("Lightning Bolt")) {
            World world = Utility.mc.world;

            if (this.effect.get().equals("Lightning")) {
               LightningBoltEntity lightning = EntityType.LIGHTNING_BOLT.create(world);
               if (lightning != null) {
                  lightning.moveForced(Vector3d.copyCentered(entity.getPosition()));
                  world.addEntity(lightning);
               }
               world.playSound(null, new BlockPos(entity.getPosX(), entity.getPosY(), entity.getPosZ()), SoundEvents.ENTITY_LIGHTNING_BOLT_THUNDER, SoundCategory.WEATHER, 5.0F, 1.0F);
            } else if (this.effect.get().equals("Blood Explosion")) {
               world.playEvent(2001, new BlockPos(entity.getPosX(), entity.getPosYEye(), entity.getPosZ()), 152);
            }
         }

      }
   }
}
