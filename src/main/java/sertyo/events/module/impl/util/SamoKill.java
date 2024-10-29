package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.DamageSource;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;

@ModuleAnnotation(name = "Suicide", category = Category.UTIL)
public class SamoKill extends Module {
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player != null) {
            mc.player.setHealth(0);
            mc.player.removed = true;
        }
        toggle();
    }
}
