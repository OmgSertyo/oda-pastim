package sertyo.events.module.impl.render;

import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.Utility;

@ModuleAnnotation(name = "FullBright", category = Category.PLAYER)
public class FullBright extends Module {
    @Override
    public void onEnable() {
            mc.player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
    }
    public void onDisable() {
        if (Utility.mc.player != null) {
            Utility.mc.player.removeActivePotionEffect(Effects.NIGHT_VISION);
        }

        super.onDisable();
    }
}

