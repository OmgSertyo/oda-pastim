package sertyo.events;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.experimental.NonFinal;
import me.sertyo.api.profile.CheatProfile;
import lombok.Getter;
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

import sertyo.events.ui.csgui.CsGui;
import sertyo.events.ui.menu.main.NeironMainMenu;
import sertyo.events.ui.ab.ActivationLogic;
import sertyo.events.ui.ab.AutoBuy;
import sertyo.events.ui.ab.AutoBuyGui;
import sertyo.events.ui.ab.font.main.IFont;
import sertyo.events.ui.ab.manager.AutoBuyManager;
import sertyo.events.ui.ab.manager.IgnoreManager;
import sertyo.events.ui.testGUI.Panel;
import sertyo.events.utility.Language;
import sertyo.events.utility.render.ScaleMath;
import sertyo.events.utility.render.ShaderUtil;
import sertyo.events.utility.render.ShaderUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

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
    public static String username;
    private ScaleMath scaleMath;
    private ModuleManager moduleManager;
    private NeironMainMenu mainMenu;
    private ThemeManager themeManager;
    private ActivationLogic activationLogic;
    private CsGui csGui;
    public static AutoBuyGui abGui;
    private StaffManager staffManager;
    public static boolean unhooked = false;
    private DragManager dragManager;
    public static long startTime;
    public static CheatProfile cheatProfile;
    public static boolean hold_mouse0;
    @NonFinal
    Language language = Language.RUS;

    public void start() {
        cheatProfile = CheatProfile.create();
        cheatProfile.downloadAvatar(true);
        addstart();
        GlowESP.notstarted = false;
        this.dragManager = new DragManager();
        try {
            this.dragManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new AutoBuy();
        this.activationLogic = new ActivationLogic();
        this.scaleMath = new ScaleMath(2);
        new ActivationLogic();
        mc.session = new Session("SVotestVZVZV", "", "", "mojang");
        ShaderUtil.init();
        ShaderUtils.init();
        System.out.println("Event inited");
        EventManager.register(this);
        this.moduleManager = new ModuleManager();
        this.themeManager = new ThemeManager();
        this.commandManager = new CommandManager();
        this.mainMenu = new NeironMainMenu();
        csGui = new CsGui();
        this.friendManager = new FriendManager();
        abGui = new AutoBuyGui();
        AutoBuyManager.init();
        this.staffManager = new StaffManager();
        try {
            this.staffManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        AutoBuyManager.load();
        IgnoreManager.load();
        IFont.init();
        startTime = System.currentTimeMillis();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }


    public void addstart() {
        String urlString = "http://t981877h.beget.tech/api/counter.php"; // URL до вашей PHP страницы

        try {
            // Открываем соединение с сервером
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestMethod("GET");

            // Устанавливаем таймер на закрытие через 10 секунд
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // Закрываем соединение
                    connection.disconnect();
                    System.out.println("Connection closed after 10 seconds.");
                }
            }, 10000); // 10 000 миллисекунд = 10 секунд

            // Ожидаем завершения выполнения
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void shutdown() {
        this.dragManager.save();
        this.configManager.saveConfig("autocfg");
        AutoBuyManager.save();
        IgnoreManager.save();
    }
    public static boolean canUpdate() {
        return mc.player != null && mc.world != null;
    }
    @EventTarget
    public void onInputKey(EventInputKey eventInputKey) {
        for (Module module : this.moduleManager.getModules()) {
            if (module.getBind() == eventInputKey.getKey()) {
                module.toggle();
            }
        }
        if (eventInputKey.getKey() == GLFW.GLFW_KEY_F6) {
            mc.displayGuiScreen(new Panel());
        }
    }
    public void toggleLanguage() {
        if (language == Language.RUS) {
            language = Language.UKR;
        } else {
            language = Language.RUS;
        }
    }
    @EventTarget
    public void onMouse(EventMouse eventMouse) {
        if (eventMouse.getButton() == 0) hold_mouse0 = false;
        if (eventMouse.getButton() == 1) hold_mouse0 = true;

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
}
