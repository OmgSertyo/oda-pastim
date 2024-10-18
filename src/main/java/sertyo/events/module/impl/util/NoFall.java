package sertyo.events.module.impl.util;


import com.darkmagician6.eventapi.EventTarget;
import sertyo.events.event.player.EventMotion;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;

@ModuleAnnotation(name = "NoFall", category = Category.PLAYER)
public class NoFall extends Module {


        @EventTarget
    public void onEventmotion(EventUpdate e) {
            if (mc.player.ticksExisted % 3 == 0 && mc.player.fallDistance > 3) {
            //    mc.player.setpositionY(e.getPosY() + 0.2f);
            }
        }
    }

