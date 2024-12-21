package sertyo.events.module;




import com.darkmagician6.eventapi.EventTarget;
import sertyo.events.module.impl.movement.*;
import sertyo.events.module.impl.util.*;
import sertyo.events.module.impl.player.*;
import sertyo.events.module.impl.render.*;
import sertyo.events.module.impl.combat.*;



import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ModuleManager {
   private final List<Module> modules = new ArrayList();

   public ModuleManager() {
      this.registerModule(new ClickGuiModule());
      this.registerModule(new EventConverter());
      this.registerModule(new HWHelper());
      this.registerModule(new GodModeRW());
      this.registerModule(new TabCompleteCrasher());
      this.registerModule(new PostProcessing());
      this.registerModule(new AhHelper());
      this.registerModule(new FtFly());
      this.registerModule(new ElytraRecast());
      this.registerModule(new AntiAFK());
      this.registerModule(new XRay());
      this.registerModule(new AutoRespawn());
      this.registerModule(new FastBreak());
      this.registerModule(new FreeCam());
      this.registerModule(new InventoryMove());
      this.registerModule(new Irc());
      this.registerModule(new ItemScroller());
      this.registerModule(new EventHelperFunTime());
      this.registerModule(new ItemSwapFix());
      this.registerModule(new MiddleClick());
      this.registerModule(new Speed());
      this.registerModule(new NoDelay());
      this.registerModule(new NoPush());
      this.registerModule(new NoSlow());
      this.registerModule(new AutoSprint());
      this.registerModule(new Phase());
      this.registerModule(new PasswordHider());
      this.registerModule(new NoServerRotations());
      this.registerModule(new SamoKill());
      this.registerModule(new XCarry());
      this.registerModule(new TriggerBot());
      this.registerModule(new VulcanESP());
      this.registerModule(new WaterSpeed());
      this.registerModule(new ChestStealer());
      this.registerModule(new SpotifyMENU());
      this.registerModule(new CasinoBotHW());
      this.registerModule(new KillAura());
      this.registerModule(new BadTrip());
      this.registerModule(new Optimization());
      this.registerModule(new BlockESP());
      this.registerModule(new EntityESP());
      this.registerModule(new Glint());
      this.registerModule(new JumpCircle());
      this.registerModule(new KillEffect());
      this.registerModule(new Predictions());
      this.registerModule(new AutoLoot());
      this.registerModule(new NameProtect());
      this.registerModule(new Hud());
      this.registerModule(new NoFall());
      this.registerModule(new FullBright());
      this.registerModule(new GlowESP());
      this.registerModule(new AutoBuyGUI());
      this.registerModule(new NoRender());
      this.registerModule(new Arraylist());
   }

   public void registerModule(Module module) {
      this.modules.add(module);
   }

   public List<Module> getModules() {
      return this.modules;
   }

   public Module[] getModulesFromCategory(Category category) {
      return (Module[])this.modules.stream().filter((module) -> {
         return module.category == category;
      }).toArray((x$0) -> {
         return new Module[x$0];
      });
   }

   public Module getModule(Class<? extends Module> classModule) {
      Iterator var2 = this.modules.iterator();

      Module module;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         module = (Module)var2.next();
      } while(module == null || module.getClass() != classModule);

      return module;
   }

   public Module getModule(String name) {
      Iterator var2 = this.modules.iterator();

      Module module;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         module = (Module)var2.next();
      } while(module == null || !module.getName().equalsIgnoreCase(name));

      return module;
   }
}
