package sertyo.events.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.EnderPearlEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector3d;
import sertyo.events.event.render.EventRender3D;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.MultiBooleanSetting;
import sertyo.events.utility.Utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.opengl.GL11;
import sertyo.events.utility.render.ColorUtility;

@ModuleAnnotation(
   name = "Predictions",
   category = Category.RENDER
)
public class Predictions extends Module {
   private final MultiBooleanSetting objects = new MultiBooleanSetting("Objects", Arrays.asList("Pearls", "Arrows"));
   public HashMap<Entity, Vector3d> lastPoss = new HashMap();
   public HashMap<Entity, Integer> i1 = new HashMap();

   @EventTarget
   public void onRender(EventRender3D eventRender3D) {
      double ix = -(Utility.mc.player.lastTickPosX + (Utility.mc.player.getPosX() - Utility.mc.player.lastTickPosX) * (double)eventRender3D.getPartialTicks());
      double iy = -(Utility.mc.player.lastTickPosY + (Utility.mc.player.getPosY() - Utility.mc.player.lastTickPosY) * (double)eventRender3D.getPartialTicks());
      double iz = -(Utility.mc.player.lastTickPosZ + (Utility.mc.player.getPosZ() - Utility.mc.player.lastTickPosZ) * (double)eventRender3D.getPartialTicks());
      GlStateManager.pushMatrix();
      GlStateManager.translated(ix, iy, iz);
      GlStateManager.enableDepthTest();
      GlStateManager.enableBlend();
      GlStateManager.disableTexture();
      GL11.glEnable(2848);
      GL11.glLineWidth(1.5F);
      GL11.glBegin(1);
      Iterator var8 = Utility.mc.world.getAllEntities().iterator();

      while(var8.hasNext()) {
         Entity entity = (Entity)var8.next();
         if (this.objects.get(0) && entity instanceof EnderPearlEntity) {
            this.drawPrediction(entity, (double)((EnderPearlEntity)entity).getGravityVelocity(), 0.800000011920929D);
         }

         if (this.objects.get(1) && entity instanceof ArrowEntity) {
            this.drawPrediction(entity, 0.05D, 0.6000000238418579D);
         }
      }

      GL11.glEnd();
      GL11.glDisable(2848);
      GlStateManager.enableTexture();
      GlStateManager.enableDepthTest();
      GlStateManager.disableBlend();
      GlStateManager.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.popMatrix();
   }

   private void drawPrediction(Entity e, double g, double water) {
      double motionX = e.motion.x;
      double motionY = e.motion.y;
      double motionZ = e.motion.z;
      double x = e.getPosX();
      double y = e.getPosY();
      double z = e.getPosZ();

      for(int i = 0; i < 300; ++i) {
         ColorUtility.setColor(Arraylist.getColor(i * 4));
         Vector3d lastPos = new Vector3d(x, y, z);
         x += motionX;
         y += motionY;
         z += motionZ;
         if (Utility.mc.world.getBlockState(new BlockPos((int)x, (int)y, (int)z)).getBlock() == Blocks.WATER) {
            motionX *= water;
            motionY *= water;
            motionZ *= water;
         } else {
            motionX *= 0.99D;
            motionY *= 0.99D;
            motionZ *= 0.99D;
         }

         motionY -= g;
         Vector3d pos = new Vector3d(x, y, z);
         if (shouldEntityHit(pos.add(0, 1, 0), pos.add(0, 1, 0)) || pos.y <= 0) {
            break;
         }

         if (y <= 0.0D) {
            break;
         }

         if (e.motion.z != 0.0D || e.motion.x != 0.0D || e.motion.y != 0.0D) {
            this.lastPoss.put(e, new Vector3d(lastPos.x, lastPos.y, lastPos.z));
            GL11.glVertex3d(lastPos.x, lastPos.y, lastPos.z);
            GL11.glVertex3d(x, y, z);
            this.i1.put(e, i);
         }
      }
   }
   private boolean shouldEntityHit(Vector3d pearlPosition, Vector3d lastPosition) {
      final RayTraceContext rayTraceContext = new RayTraceContext(
              lastPosition,
              pearlPosition,
              RayTraceContext.BlockMode.COLLIDER,
              RayTraceContext.FluidMode.NONE,
              mc.player
      );
      final BlockRayTraceResult blockHitResult = mc.world.rayTraceBlocks(rayTraceContext);

      return blockHitResult.getType() == RayTraceResult.Type.BLOCK;
   }
}
