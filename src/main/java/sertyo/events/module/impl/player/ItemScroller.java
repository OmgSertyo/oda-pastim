package sertyo.events.module.impl.player;


import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.NumberSetting;

@ModuleAnnotation(
   name = "ItemScroller",
   category = Category.PLAYER
)
public class ItemScroller extends Module {
   public static NumberSetting delay = new NumberSetting("Delay", 80.0F, 0.0F, 1000.0F, 1.0F);
}
