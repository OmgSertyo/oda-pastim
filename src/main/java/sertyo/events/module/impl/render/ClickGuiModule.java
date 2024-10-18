package sertyo.events.module.impl.render;


import sertyo.events.Main;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.utility.Utility;

@ModuleAnnotation(
   name = "ClickGui",
   category = Category.RENDER
)
public class ClickGuiModule extends Module {
   public static BooleanSetting blur = new BooleanSetting("Blur", true);
   public static NumberSetting blurRadius;

   public ClickGuiModule() {
      this.bind = 54;
   }

   public void onEnable() {
      super.onEnable();
      Utility.mc.displayGuiScreen(Main.getInstance().getCsGui());
      Main.getInstance().getModuleManager().getModule(ClickGuiModule.class).setToggled(false);
   }

   static {
      BooleanSetting var10007 = blur;
      var10007.getClass();
      blurRadius = new NumberSetting("Blur Iterations", 5.0F, 1.0F, 15.0F, 1.0F, var10007::get);
   }
}
