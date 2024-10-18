package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import com.google.common.eventbus.Subscribe;
import java.util.Iterator;
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
    public void onUpdate(EventUpdate var1) {
        Iterator player = mc.world.getPlayers().iterator();

        while(player.hasNext()) {
            Entity entity = (Entity) player;
            if (entity != null) {
                entity.setGlowing(true);
            }
        }
        notstarted = true;
    }
    @Override
    public void onEnable() {
        super.onEnable();
            Iterator var1 = mc.world.getPlayers().iterator();

            while (var1.hasNext()) {
                Entity var2 = (Entity) var1.next();
                if (var2 != null) {
                    var2.setGlowing(true);
                }

        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        if (notstarted) {
            Iterator var1 = mc.world.getPlayers().iterator();

            while (var1.hasNext()) {
                Entity var2 = (Entity) var1.next();
                if (var2 != null) {
                    var2.setGlowing(false);
                }
            }
        }
    }
}