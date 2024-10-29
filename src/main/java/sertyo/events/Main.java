package sertyo.events;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import com.jagrosh.discordipc.entities.User;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.Session;
import net.minecraft.util.text.StringTextComponent;
import obf.sertyo.nativeobf.Native;
import sertyo.events.command.CommandManager;
import sertyo.events.command.impl.GpsCommand;
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
import sertyo.events.utility.DiscordPresence;
import sertyo.events.utility.render.ScaleMath;
import sertyo.events.utility.render.ShaderUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import static sertyo.events.Protect.*;
import static sertyo.events.utility.Utility.mc;
@Native
public class Main {
    public static String name = "Neiron";
    public static String version = "1.16.5 edition";
    public static String build = "1.0.1";
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
    private ActivationLogic activationLogic;
    private CsGui csGui;
    public static AutoBuyGui abGui;
    public static AutoBuy autoBuy = new AutoBuy();
    public static User me;
    private StaffManager staffManager;

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

    public static boolean unhooked = false;
    public static boolean checkking = true;
    private DragManager dragManager;
    public void startprotect() {
        try {
             //getServer();
            username = "Sertyo";
            password = "4oTiLisiyPlakiPlaki";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

       // check(username, password);
          getUID2();
        setrole();


    }
    public void start() {
        startprotect();
        GlowESP.notstarted = false;

    //    DiscordPresence.startDiscord();
        this.dragManager = new DragManager();
        try {
            this.dragManager.init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        new AutoBuy();

        this.activationLogic = new ActivationLogic();
        new ActivationLogic();
        mc.session = new Session("Sane4kaSnimaeshS", "", "", "mojang");
        ShaderUtil.init();
        System.out.println("Event inited");
        EventManager.register(this);
        String status = checkServerStatus();
        this.moduleManager = new ModuleManager();

        if (checkking) {
            if ("false".equals(status)) {
                System.out.println("[!] Фри версия сейчас не доступа.");
                System.exit(-1488);
            } else if (!"true".equals(status)) {
                System.out.println("[!] Панкихой.");
            }
        }
        addstart();
        this.themeManager = new ThemeManager();
        this.commandManager = new CommandManager();
        this.mainMenu = new NeironMainMenu();
        csGui = new CsGui(new StringTextComponent("A"));
        this.friendManager = new FriendManager();
      //  this.configManager.loadConfig("autocfg");
        this.abGui = new AutoBuyGui();

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

        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }
    public StaffManager getStaffManager() {
        return this.staffManager;
    }

    public void shutdown() {
        this.dragManager.save();
        this.configManager.saveConfig("autocfg");
        AutoBuyManager.save();
        IgnoreManager.save();
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
    public ActivationLogic getActivationLogic() {
        return this.activationLogic;
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
    public static boolean canUpdate() {
        if (mc == null) return false;
        return mc.player != null && mc.world != null;
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
    @EventTarget
    public void onRender2D(EventRender2D eventMouse) {
        if (GpsCommand.enabled) {
            GpsCommand.drawArrow(new MatrixStack());
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
