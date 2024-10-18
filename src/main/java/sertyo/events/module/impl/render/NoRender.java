package sertyo.events.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.network.play.server.SSpawnExperienceOrbPacket;
import net.minecraft.potion.Effects;
import sertyo.events.event.packet.EventReceivePacket;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.event.render.EventOverlay;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.MultiBooleanSetting;
import sertyo.events.utility.Utility;


import java.util.Arrays;
import java.util.Iterator;



@ModuleAnnotation(
   name = "NoRender",
   category = Category.RENDER
)
public class NoRender extends Module {
   public static MultiBooleanSetting element = new MultiBooleanSetting("Elements", Arrays.asList("Fire", "Boss Bar", "Scoreboard", "Totem", "Armor Stand", "Bad Effects", "Skylight", "Explosions", "Particles", "Exp Orbs", "Hurt Cam", "Weather", "Armor"));

   @EventTarget
   public void onOverlayRender(EventOverlay event) {
      if (element.get(0) && event.getOverlayType().equals(EventOverlay.OverlayType.FIRE) || element.get(1) && event.getOverlayType().equals(EventOverlay.OverlayType.BOSS_BAR) || element.get(2) && event.getOverlayType().equals(EventOverlay.OverlayType.SCOREBOARD) || element.get(3) && event.getOverlayType().equals(EventOverlay.OverlayType.TOTEM_ANIMATION) || element.get(11) && event.getOverlayType().equals(EventOverlay.OverlayType.WEATHER) || element.get(6) && event.getOverlayType().equals(EventOverlay.OverlayType.SKYLIGHT) || element.get(10) && event.getOverlayType().equals(EventOverlay.OverlayType.HURT_CAM) || element.get(12) && event.getOverlayType().equals(EventOverlay.OverlayType.ARMOR) || element.get(8) && event.getOverlayType().equals(EventOverlay.OverlayType.PARTICLES)) {
         event.setCancelled(true);
      }

   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (element.get(11)) {
         Utility.mc.world.setRainStrength(0.0F);
         Utility.mc.world.setThunderStrength(0.0F);
      }

      if (element.get(5) && Utility.mc.player.isPotionActive(Effects.BLINDNESS) || Utility.mc.player.isPotionActive(Effects.NAUSEA)) {
         Utility.mc.player.removePotionEffect(Effects.NAUSEA);
         Utility.mc.player.removePotionEffect(Effects.BLINDNESS);
      }

      if (element.get(4)) {
         Iterator var2 = Utility.mc.world.loadedTileEntityList.iterator();

         while(var2.hasNext()) {
            Entity entity = (Entity)var2.next();
            if (entity instanceof ArmorStandEntity) {
               Utility.mc.world.removeEntity(entity);
            }
         }
      }

   }

   @EventTarget
   public void onPacketReceive(EventReceivePacket event) {
      if (element.get(7) && event.getPacket() instanceof SExplosionPacket || element.get(9) && event.getPacket() instanceof SSpawnExperienceOrbPacket) {
         event.setCancelled(true);
      }

   }
}
