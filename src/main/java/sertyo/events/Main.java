package sertyo.events;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.SneakyThrows;
import lombok.experimental.NonFinal;
import lombok.Getter;
import me.sertyo.viamcp.ViaMCP;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Session;
import me.sertyo.j2c.J2c;
import org.lwjgl.glfw.GLFW;

import sertyo.events.command.CommandManager;
import sertyo.events.command.impl.GpsCommand;
import sertyo.events.command.impl.KeyMappings;
import sertyo.events.event.input.EventInputKey;
import sertyo.events.event.input.EventMouse;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.manager.config.ConfigManager;
import sertyo.events.manager.dragging.DragManager;
import sertyo.events.manager.friend.FriendManager;
import sertyo.events.manager.staff.StaffManager;
import sertyo.events.manager.theme.ThemeManager;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleManager;
import sertyo.events.module.impl.player.GlowESP;

import sertyo.events.module.scripts.ScriptManager;
import sertyo.events.ui.autobuy.impl.ItemManager;
import sertyo.events.ui.csgui.CsGui;
import sertyo.events.ui.menu.altmanager.alt.AltFileManager;
import sertyo.events.ui.menu.main.NeironMainMenu;
import sertyo.events.utility.misc.Language;
import sertyo.events.utility.math.ScaleMath;
import sertyo.events.utility.render.ShaderUtil;
import sovokguard.protect.ApiContacts;

import static sertyo.events.utility.Utility.mc;
@Getter
@J2c
public class Main {

    public static String name = "Neiron";
    public static String version = "1.16.5 edition";
    public static String build = "1.0.1";
    @Getter
    private static final Main instance = new Main();
    private final ConfigManager configManager = new ConfigManager();
    private CommandManager commandManager;
    private FriendManager friendManager;
    private ScriptManager scriptManager;
    public static String username;
    private ScaleMath scaleMath;
    public static ViaMCP viaMCP;

    private ModuleManager moduleManager;
    private NeironMainMenu mainMenu;
    private ThemeManager themeManager;
    private CsGui csGui;
    private StaffManager staffManager;
    public static boolean unhooked = false;
    private DragManager dragManager;
    private AltFileManager altFileManager;
    public static long startTime;
    @NonFinal
    Language language = Language.RUS;

    @SneakyThrows
    public void start() {
        ApiContacts.start();
        GlowESP.notstarted = false;
        this.dragManager = new DragManager();
            this.dragManager.init();


        this.scaleMath = new ScaleMath(2);
        viaMCP = new ViaMCP();
        altFileManager = new AltFileManager();
        altFileManager.init();
        mc.session = new Session("SVotestVZVZV", "", "", "mojang");
        ShaderUtil.init();
        EventManager.register(this);
        this.moduleManager = new ModuleManager();
        ItemManager.register();
        this.themeManager = new ThemeManager();
        this.commandManager = new CommandManager();
        this.mainMenu = new NeironMainMenu();
        csGui = new CsGui();
        this.friendManager = new FriendManager();
        this.staffManager = new StaffManager();
        this.staffManager.init();
        this.scriptManager = new ScriptManager();
        this.scriptManager.init();
        this.scriptManager.parseAllScripts();
        startTime = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }


    public void shutdown() {
        this.dragManager.save();
        this.altFileManager.saveAll();
        this.configManager.saveConfig("autocfg");
    }
    //For change language
    public void toggleLanguage() {
        if (language == Language.RUS) {
            language = Language.UKR;
        } else {
            language = Language.RUS;
        }
    }
    @EventTarget
    public void onInputKey(EventInputKey eventInputKey) {
        for (Module module : this.moduleManager.getModules()) {
            if (module.getBind() == eventInputKey.getKey()) {
                module.toggle();
            }
        }
        if (eventInputKey.getKey() == GLFW.GLFW_KEY_F6) mc.displayGuiScreen(new sertyo.events.ui.autobuy.AutoBuyGui());

    }
    @EventTarget
    public void onMouse(EventMouse eventMouse) {
        for (Module module : this.moduleManager.getModules()) {
            if (module.getMouseBind() == eventMouse.getButton() && eventMouse.getButton() > 2) {
                module.toggle();
            }
        }
    }
    @EventTarget
    public void onRender2D(EventRender2D eventMouse) {
        if (GpsCommand.enabled) {
            GpsCommand.drawArrow(new MatrixStack());
        }

    }
    public static String getKey(int integer) {
        if (integer < 0) {
            return switch (integer) {
                case -100 -> I18n.format("key.mouse.left");
                case -99 -> I18n.format("key.mouse.right");
                case -98 -> I18n.format("key.mouse.middle");
                default -> "MOUSE" + (integer + 101);
            };
        } else {
            return (GLFW.glfwGetKeyName(integer, -1) == null ? KeyMappings.reverseKeyMap.get(integer) : GLFW.glfwGetKeyName(integer, -1)) ;
        }
    }
    public static boolean canUpdate() {
        return mc.player != null && mc.world != null;
    }
}
