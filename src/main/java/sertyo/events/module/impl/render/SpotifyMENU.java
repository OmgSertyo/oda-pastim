package sertyo.events.module.impl.render;


import org.lwjgl.glfw.GLFW;
import sertyo.events.Main;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.BooleanSetting;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.ui.csgui.spotify.Spotify;
import sertyo.events.utility.Utility;

@ModuleAnnotation(
        name = "SpotifyMenu",
        category = Category.RENDER
)
public class SpotifyMENU extends Module {

    public SpotifyMENU() {
    }

    public void onEnable() {
        super.onEnable();
        Utility.mc.displayGuiScreen(new Spotify());
        toggle();
    }


}
