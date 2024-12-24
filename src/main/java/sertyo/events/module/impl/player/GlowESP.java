package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;

import java.util.Iterator;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.entity.Entity;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;

@ModuleAnnotation(
        name = "GlowESP",
        category = Category.PLAYER
)
public class GlowESP extends Module {
    public GlowESP() {
    }
    public static boolean notstarted = true;
    @EventTarget
    public void onUpdate(EventUpdate update) {
        Iterator<AbstractClientPlayerEntity> player = mc.world.getPlayers().iterator();

        while(player.hasNext()) {
            Entity entity = (Entity) player;
            entity.setGlowing(true);
        }
        notstarted = true;
    }
    @Override
    public void onEnable() {
        super.onEnable();
            Iterator<AbstractClientPlayerEntity> svo = mc.world.getPlayers().iterator();

            while (svo.hasNext()) {
                Entity var2 = (Entity) svo.next();
                if (var2 != null) {
                    var2.setGlowing(true);
                }

        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        if (notstarted) {
            Iterator<AbstractClientPlayerEntity> svo = mc.world.getPlayers().iterator();

            while (svo.hasNext()) {
                Entity var2 =  svo.next();
                if (var2 != null) {
                    var2.setGlowing(false);
                }
            }
        }
    }
}