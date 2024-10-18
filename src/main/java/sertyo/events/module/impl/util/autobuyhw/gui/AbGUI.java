package sertyo.events.module.impl.util.autobuyhw.gui;


import org.lwjgl.glfw.GLFW;
import sertyo.events.Main;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.utility.Utility;

@ModuleAnnotation(
        name = "AbGUI",
        category = Category.RENDER
)
public class AbGUI extends Module {
    public static BooleanSetting blur = new BooleanSetting("Blur", true);
    public static NumberSetting blurRadius;

    public AbGUI() {
        this.bind = GLFW.GLFW_KEY_F9;
    }

    public void onEnable() {
        super.onEnable();
        Utility.mc.displayGuiScreen(new AutoBuyUI());
        Main.getInstance().getModuleManager().getModule(AbGUI.class).setToggled(false);
    }


}
