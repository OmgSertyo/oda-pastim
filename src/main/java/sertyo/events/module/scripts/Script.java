package sertyo.events.module.scripts;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.compiler.DumpState;
import org.luaj.vm2.customs.EntityHook;
import sertyo.events.module.Category;
import sertyo.events.module.Module;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.luaj.vm2.globals.Standarts.standardGlobals;

public class Script {

    public String scriptName = "";
    public Module mod;

    public String moduleName;
    public Category category;

    public LuaValue chunk;
    public Globals globals;

    public List<EntityHook> entityHookList = new ArrayList<>();

    public Script(String scriptName) {
        this.scriptName = scriptName;
    }

    public void load() {
        globals = standardGlobals();
        chunk = globals.loadfile("scripts/" + scriptName);
        chunk.call();
        if (globals.get("setName") != LuaValue.NIL)
            moduleName = globals.get("setName").call().toString();
        else {
            moduleName = "NIL";
        }

        category = Category.SCRIPTS;
        constInit();
    }

    public void processScript(Globals globals, InputStream script, String chunkname, OutputStream out) throws IOException {
        try {
            script = new BufferedInputStream(script);
            Prototype chunk = globals.compilePrototype(script, chunkname);
            DumpState.dump(chunk, out, false, DumpState.NUMBER_FORMAT_DEFAULT, false);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            script.close();
        }
    }

    private void constInit() {
        globals.set("x", 1);
        globals.set("y", 2);
        globals.set("z", 3);

        globals.set("MAIN_HAND", 341);
        globals.set("OFF_HAND", 351);

        globals.set("WATER", 2012);
        globals.set("LAVA", 2013);
    }

}

