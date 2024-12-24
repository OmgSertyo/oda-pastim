package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.eventbus.Subscribe;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.util.Hand;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;


@ModuleAnnotation(name = "AutoLootHW", category = Category.UTIL)
public class AutoLoot extends Module {


    @EventTarget
    private void onUpdate(EventUpdate event) {
        for (Entity entity : mc.world.getAllEntities()) {
            if (entity instanceof VillagerEntity || entity instanceof WanderingTraderEntity) {
                if (!((AbstractVillagerEntity) entity).getHeldItemMainhand().isEmpty() && mc.player.getDistance(entity) <= mc.playerController.getBlockReachDistance()) {
                    mc.playerController.interactWithEntity(mc.player, entity, Hand.MAIN_HAND);
                    mc.player.swingArm(Hand.MAIN_HAND);
                    mc.player.sendChatMessage("/ah sell 30000000");
                }
            }
        }
    }
}