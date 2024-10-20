package sertyo.events.module;




import sertyo.events.module.impl.movement.WaterSpeed;
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
      this.registerModule(new Notifications());
      this.registerModule(new TriggerBot());
      this.registerModule(new VulcanESP());
      this.registerModule(new WaterSpeed());
      this.registerModule(new ChestStealer());
      this.registerModule(new CasinoBotHW());
      this.registerModule(new AutoBuyGUI());
      this.registerModule(new AutoLoot());
      this.registerModule(new NameProtect());
      this.registerModule(new Hud());
      this.registerModule(new NoFall());
      this.registerModule(new FullBright());
      this.registerModule(new GlowESP());
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
