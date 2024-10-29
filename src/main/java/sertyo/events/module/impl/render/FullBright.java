package sertyo.events.module.impl.render;


import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import sertyo.events.Main;
import sertyo.events.event.player.EventMotion;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;

@ModuleAnnotation(name = "FullBright", category = Category.PLAYER)
public class FullBright extends Module {
    @Override
    public void onEnable() {
       if (Main.canUpdate())
            mc.player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));

    }

}

