package sertyo.events.module.impl.render;


import org.lwjgl.glfw.GLFW;
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

   public ClickGuiModule() {
      this.bind = GLFW.GLFW_KEY_RIGHT_SHIFT;
   }

   public void onEnable() {
      super.onEnable();
      Utility.mc.displayGuiScreen(Main.getInstance().getCsGui());
      Main.getInstance().getModuleManager().getModule(ClickGuiModule.class).setToggled(false);
   }

}
