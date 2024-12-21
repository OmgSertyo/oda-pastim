package org.luaj.vm2.lib.custom;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.ZeroArgFunction;
import sertyo.events.Main;
import sertyo.events.module.Module;

public class UtilsLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaValue library = tableOf();
        library.set("setState", new setstate());
        library.set("getName", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(Main.cheatProfile.getName());
            }
        });

        library.set("getUid", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(Main.cheatProfile.getId());
            }
        });

        library.set("getRole", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(Main.cheatProfile.getRoleName());
            }
        });

        library.set("getTill", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf("Error");
            }
        });
        library.set("currentTimeMillis", new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return LuaValue.valueOf(System.currentTimeMillis());
            }
        });
        env.set("utils", library);
        return library;
    }

    static class setstate extends TwoArgFunction {

        @Override
        public LuaValue call(LuaValue arg1, LuaValue arg2) {
            sertyo.events.module.Module function = (sertyo.events.module.Module) arg1.checkuserdata();
            function.setToggled(arg2.toboolean());
            return arg2;
        }
    }

}
