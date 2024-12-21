package org.luaj.vm2.globals;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.custom.*;

public class Standarts {
    public static Globals standardGlobals() {
        Globals globals = new Globals();
        globals.load(new BaseLib()); //ok
        globals.load(new Bit32Lib()); //ok
        globals.load(new MathLib()); //ok
        globals.load(new TableLib()); //ok
        globals.load(new StringLib()); //ok
        globals.load(new WorldLib());
        globals.load(new UtilsLib());
        LoadState.install(globals);
        LuaC.install(globals);
        return globals;
    }
}
