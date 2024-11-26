package sertyo.events.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.tileentity.*;
import sertyo.events.event.render.EventRender3D;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.ColorSetting;

import java.awt.Color;
import java.util.Arrays;
import java.util.Iterator;

import sertyo.events.module.setting.impl.MultiBooleanSetting;
import sertyo.events.utility.Utility;
import sertyo.events.utility.render.RenderUtility;

@ModuleAnnotation(
   name = "BlockESP",
   category = Category.RENDER
)
public class BlockESP extends Module {
   private final MultiBooleanSetting blocks = new MultiBooleanSetting("Blocks", Arrays.asList("Spawner", "Chest", "Ender Chest", "Shulker", "End Portal"));
   private final ColorSetting spawnerColor;
   private final ColorSetting chestColor;
   private final ColorSetting enderChestColor;
   private final ColorSetting shulkerColor;
   private final ColorSetting endPortalColor;

   public BlockESP() {
      this.spawnerColor = new ColorSetting("Spawner Color", Color.YELLOW.getRGB(), () -> {
         return this.blocks.get(0);
      });
      this.chestColor = new ColorSetting("Chest Color", Color.ORANGE.getRGB(), () -> {
         return this.blocks.get(1);
      });
      this.enderChestColor = new ColorSetting("Ender Chest Color", Color.MAGENTA.getRGB(), () -> {
         return this.blocks.get(2);
      });
      this.shulkerColor = new ColorSetting("Shulker Color", Color.CYAN.getRGB(), () -> {
         return this.blocks.get(3);
      });
      this.endPortalColor = new ColorSetting("End Portal Color", Color.GRAY.getRGB(), () -> {
         return this.blocks.get(4);
      });
   }

   @EventTarget
   public void onRender3D(EventRender3D event) {
      Iterator var2 = Utility.mc.world.loadedTileEntityList.iterator();

      while(var2.hasNext()) {
         TileEntity entity = (TileEntity)var2.next();
         if (this.blocks.get(0) && entity instanceof MobSpawnerTileEntity) {
            RenderUtility.drawBlockBox(entity.getPos(), this.spawnerColor.getColor().getRGB());
         }

         if (this.blocks.get(1) && entity instanceof ChestTileEntity) {
            RenderUtility.drawBlockBox(entity.getPos(), this.chestColor.getColor().getRGB());
         }

         if (this.blocks.get(2) && entity instanceof EnderChestTileEntity) {
            RenderUtility.drawBlockBox(entity.getPos(), this.enderChestColor.getColor().getRGB());
         }

         if (this.blocks.get(3) && entity instanceof ShulkerBoxTileEntity) {
            RenderUtility.drawBlockBox(entity.getPos(), this.shulkerColor.getColor().getRGB());
         }

         if (this.blocks.get(4) && entity instanceof EndPortalTileEntity) {
            RenderUtility.drawBlockBox(entity.getPos(), this.endPortalColor.getColor().getRGB());
         }
      }

   }
}
