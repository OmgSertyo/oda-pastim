package sertyo.events;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.util.Session;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;
import sertyo.events.command.CommandManager;
import sertyo.events.event.input.EventInputKey;
import sertyo.events.event.input.EventMouse;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.manager.config.ConfigManager;
import sertyo.events.manager.dragging.DragManager;
import sertyo.events.manager.friend.FriendManager;
import sertyo.events.manager.theme.ThemeManager;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleManager;
import sertyo.events.module.impl.player.GlowESP;
import sertyo.events.ui.csgui.CsGui;
import sertyo.events.ui.menu.main.NeironMainMenu;
import sertyo.events.utility.DiscordPresence;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.ScaleMath;
import sertyo.events.utility.render.ShaderUtil;

import java.io.IOException;
import java.util.Iterator;

import static sertyo.events.Protect.*;
import static sertyo.events.utility.Utility.mc;

public class Main {
    public static String name = "Neiron";
    public static String version = "1.16.5 edition";
    public static String build = "1.0.0";
    private static final Main instance = new Main();
    private final ConfigManager configManager = new ConfigManager();
    private CommandManager commandManager;
    private FriendManager friendManager;
    public static Integer uid;
    public static final int PORT = 6024;
    public static String username;
    private final ScaleMath scaleMath = new ScaleMath(2);
    public static String role;
    public static String password;
    private ModuleManager moduleManager;
    private NeironMainMenu mainMenu;
    private ThemeManager themeManager;
    private CsGui csGui;
    public static boolean unhooked = false;
    private DragManager dragManager;
    public void startprotect() {
        try {
            // getServer();
            username = "Sertyo";
            password = "ZalupaBebra";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
      //  check(username, password);
          getUID2();
        setrole();
       /* try {
            checkhwid();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

    }
    public void start() {
        startprotect();
        GlowESP.notstarted = false;
       // DiscordPresence.startDiscord();
        this.dragManager = new DragManager();
        try {
            this.dragManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        mc.session = new Session("twink_voremommy_", "", "", "mojang");
        ShaderUtil.init();
        System.out.println("Event inited");
        EventManager.register(this);
        this.moduleManager = new ModuleManager();

        getModuleManager().getModule("GlowESP").onDisable();
        this.themeManager = new ThemeManager();
        this.commandManager = new CommandManager();
        this.mainMenu = new NeironMainMenu();
        csGui = new CsGui(new StringTextComponent("A"));
        this.friendManager = new FriendManager();
       // this.configManager.loadConfig("autocfg");



        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }
    public void shutdown() {
        if (getModuleManager().getModule("GlowESP").isEnabled()) {
            getModuleManager().getModule("GlowESP").onDisable();
        }
        this.dragManager.save();
        this.configManager.saveConfig("autocfg");
    }
    public DragManager getDragManager() {
        return this.dragManager;
    }

    public static Main getInstance() {
        return instance;
    }
    public CsGui getCsGui() {
        return this.csGui;
    }

    public FriendManager getFriendManager() {
        return this.friendManager;
    }

    public ScaleMath getScaleMath() {
        return this.scaleMath;
    }
    public ModuleManager getModuleManager() {
        return this.moduleManager;
    }
    public ThemeManager getThemeManager() {
        return this.themeManager;
    }
    public NeironMainMenu getMainMenu() {
        return this.mainMenu;
    }
    @EventTarget
    public void onInputKey(EventInputKey eventInputKey) {
        Iterator var2 = this.moduleManager.getModules().iterator();
        while(var2.hasNext()) {
            Module module = (Module)var2.next();
            if (module.getBind() == eventInputKey.getKey()) {
                module.toggle();
            }
        }

    }
    @EventTarget
    public void onMouse(EventMouse eventMouse) {
        Iterator var2 = this.moduleManager.getModules().iterator();

        while(var2.hasNext()) {
            Module module = (Module)var2.next();
            if (module.getMouseBind() == eventMouse.getButton() && eventMouse.getButton() > 2) {
                module.toggle();
            }
        }

    }
    public ConfigManager getConfigManager() {
        return this.configManager;
    }
    public CommandManager getCommandManager() {
        return this.commandManager;
    }


    public int getUid() {
        return uid;
    }
    public static String getRole() {
        return role;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
