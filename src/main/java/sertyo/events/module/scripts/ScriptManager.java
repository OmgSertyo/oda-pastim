package sertyo.events.module.scripts;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.jse.CoerceJavaToLua;
import org.luaj.vm2.customs.events.*;
import sertyo.events.Main;
import sertyo.events.event.packet.EventReceivePacket;
import sertyo.events.event.packet.EventSendPacket;
import sertyo.events.event.player.EventMotion;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.event.render.EventRender3D;
import sertyo.events.manager.notification.NotificationManager;
import sertyo.events.manager.notification.NotificationType;
import sertyo.events.module.Module;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScriptManager {
    public List<Script> scripts = new ArrayList<>();

    public void parseAllScripts() {
        File file = new File(Minecraft.getInstance().gameDir, "scripts");
        file.mkdirs();


        for (File file1 : file.listFiles()) {
            if (file1.getName().endsWith(".lua") || file1.getName().endsWith(".out")) {
                scripts.add(new Script(file1.getName()));
            }
        }
    }

    public void reload() {
        for (Script script : scripts) {
            script.load();
            script.globals.set("mod", LuaValue.userdataOf(script.mod));
            script.mod = new sertyo.events.module.Module(script.moduleName, script.category);
        }
    }

    public void init() {
        for (Script script : scripts) {
            script.load();
            System.out.println("Loaded script: " + script.scriptName);
            sertyo.events.module.Module luaFunc = new Module(script.moduleName, script.category) {
                @Override
                public void onEnable() {
                    super.onEnable();
                    if (script.globals.get("onEnable") != LuaValue.NIL) {
                        script.globals.get("onEnable").call();
                    }
                }

                @Override
                public void onDisable() {
                    super.onDisable();
                    if (script.globals.get("onDisable") != LuaValue.NIL) {
                        script.globals.get("onDisable").call();
                    }
                }

                @EventTarget
                public void e(EventUpdate eventUpdate) {
                    try {
                    script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventUpdateHook(eventUpdate)));
                    } catch (LuaError ex) {
                        NotificationManager.notify(NotificationType.ERROR, "Ошибка в скрипте: ", name, 5000L);
                        NotificationManager.notify(NotificationType.ERROR, "", ex.getMessage().toLowerCase(), 5000L);
                    }
                }

                @EventTarget
                public void e(EventMotion eventUpdate) {
                    try {
                    script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventMotionHook(eventUpdate)));
                    } catch (LuaError ex) {
                        NotificationManager.notify(NotificationType.ERROR, "Ошибка в скрипте: ", name, 5000L);
                        NotificationManager.notify(NotificationType.ERROR, "", ex.getMessage().toLowerCase(), 5000L);
                    }
                }

                @EventTarget
                public void e(EventRender2D eventUpdate) {
                    try {
                    script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventRenderHook(eventUpdate)));
                    } catch (LuaError ex) {
                        NotificationManager.notify(NotificationType.ERROR, "Ошибка в скрипте: ", name, 5000L);
                        NotificationManager.notify(NotificationType.ERROR, "", ex.getMessage().toLowerCase(), 5000L);
                    }
                }

                @EventTarget
                public void e(EventRender3D eventUpdate) {
                    try {
                    script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventRenderHook(eventUpdate)));
                    } catch (LuaError ex) {
                        NotificationManager.notify(NotificationType.ERROR, "Ошибка в скрипте: ", name, 5000L);
                        NotificationManager.notify(NotificationType.ERROR, "", ex.getMessage().toLowerCase(), 5000L);
                    }
                }

                @EventTarget
                public void e(EventReceivePacket eventUpdate) {
                    try {
                    script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventPacketHook(eventUpdate)));
                    } catch (LuaError ex) {
                        NotificationManager.notify(NotificationType.ERROR, "Ошибка в скрипте: ", name, 5000L);
                        NotificationManager.notify(NotificationType.ERROR, "", ex.getMessage().toLowerCase(), 5000L);
                    }
                }

                @EventTarget
                public void e(EventSendPacket eventUpdate) {
                    try {
                    script.globals.get("onEvent").call(CoerceJavaToLua.coerce(new EventPacketHook(eventUpdate)));
                    } catch (LuaError ex) {
                        NotificationManager.notify(NotificationType.ERROR, "Ошибка в скрипте: ", name, 5000L);
                        NotificationManager.notify(NotificationType.ERROR, "", ex.getMessage().toLowerCase(), 5000L);
                    }
                }
            };
            Main.getInstance().getModuleManager().registerModule(luaFunc);
        }
    }
}

