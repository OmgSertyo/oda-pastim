package sertyo.events.module.impl.util;


import org.lwjgl.glfw.GLFW;
import sertyo.events.Main;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.Utility;
import sertyo.events.ui.ab.AutoBuyGui;

@ModuleAnnotation(
   name = "AutoBuyGUI",
   category = Category.RENDER
)
public class AutoBuyGUI extends Module {


   public AutoBuyGUI() {
      this.bind = GLFW.GLFW_KEY_F4;
   }

   public void onEnable() {
      super.onEnable();
      Utility.mc.displayGuiScreen(new AutoBuyGui());
      Main.getInstance().getModuleManager().getModule(AutoBuyGUI.class).setToggled(false);
   }

}
