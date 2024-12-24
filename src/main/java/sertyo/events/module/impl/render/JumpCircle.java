package sertyo.events.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.event.render.EventRender3D;
import sertyo.events.module.Category;
import sertyo.events.module.Module;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.Utility;
import sertyo.events.utility.math.AntiAliasingUtility;
import sertyo.events.utility.render.animation.AnimationMath;

@ModuleAnnotation(
   name = "JumpCircle",
   category = Category.RENDER
)
public class JumpCircle extends Module {
   public static ArrayList<JumpCircle.Circle> circles = new ArrayList();

   @EventTarget
   public void onUpdate(EventUpdate event) {
      circles.removeIf((circle) -> {
         return circle.age > 990.0D;
      });
   }

   @EventTarget
   public void onRender(EventRender3D eventRender3D) {
      JumpCircle.Circle c;
      for(Iterator var2 = circles.iterator(); var2.hasNext(); c.age = (double) AnimationMath.fast((float)c.age, 1000.0F, 2.0F)) {
         c = (JumpCircle.Circle)var2.next();
         this.renderCircle(c, eventRender3D.getPartialTicks());
      }

   }

   private void renderCircle(JumpCircle.Circle circle, float partialTicks) {
      if (!(circle.age >= 1000.0D)) {
         double ix = -(Utility.mc.player.lastTickPosX + (Utility.mc.player.getPosX() - Utility.mc.player.lastTickPosX) * (double)partialTicks);
         double iy = -(Utility.mc.player.lastTickPosY + (Utility.mc.player.getPosY() - Utility.mc.player.lastTickPosY) * (double)partialTicks);
         double iz = -(Utility.mc.player.lastTickPosZ + (Utility.mc.player.getPosZ() - Utility.mc.player.lastTickPosZ) * (double)partialTicks);
         GlStateManager.pushMatrix();
         GL11.glDepthMask(false);
         GlStateManager.enableDepthTest();
         GlStateManager.translated(ix, iy, iz);
         GlStateManager.enableBlend();
         GL11.glBlendFunc(770, 771);
         GlStateManager.disableTexture();
         GL11.glDisable(2884);
         GL11.glShadeModel(7425);
         GL11.glDisable(3008);
         AntiAliasingUtility.hook(true, false, false);
         GlStateManager.alphaFunc(516, 0.0F);
         GL11.glBegin(8);

         int i;
         Color color;
         double x;
         double z;
         for(i = 0; i <= 360; ++i) {
            color = Arraylist.getColor(i);
            x = Math.cos((double)i * 3.141592653589793D / 180.0D);
            z = Math.sin((double)i * 3.141592653589793D / 180.0D);
            GL11.glColor4d((double)((float)(new Color(color.getRGB())).getRed() / 255.0F), (double)((float)(new Color(color.getRGB())).getGreen() / 255.0F), (double)((float)(new Color(color.getRGB())).getBlue() / 255.0F), 0.0D);
            GL11.glVertex3d(circle.x + x * circle.age / 1000.0D * 1.5D, circle.y, circle.z + z * circle.age / 1000.0D * 1.5D);
            GL11.glColor4d((double)((float)(new Color(color.getRGB())).getRed() / 255.0F), (double)((float)(new Color(color.getRGB())).getGreen() / 255.0F), (double)((float)(new Color(color.getRGB())).getBlue() / 255.0F), 0.5D - circle.age / 2000.0D);
            GL11.glVertex3d(circle.x + x * (circle.age / 1000.0D), circle.y, circle.z + z * (circle.age / 1000.0D));
         }

         GL11.glEnd();
         GL11.glBegin(8);

         for(i = 0; i <= 360; ++i) {
            color = Arraylist.getColor(i);
            x = Math.cos((double)i * 3.141592653589793D / 180.0D);
            z = Math.sin((double)i * 3.141592653589793D / 180.0D);
            GL11.glColor4d((double)((float)(new Color(color.getRGB())).getRed() / 255.0F), (double)((float)(new Color(color.getRGB())).getGreen() / 255.0F), (double)((float)(new Color(color.getRGB())).getBlue() / 255.0F), 0.9D - circle.age / 1100.0D);
            GL11.glVertex3d(circle.x + x * (circle.age / 1000.0D), circle.y, circle.z + z * (circle.age / 1000.0D));
            GL11.glColor4d((double)((float)(new Color(color.getRGB())).getRed() / 255.0F), (double)((float)(new Color(color.getRGB())).getGreen() / 255.0F), (double)((float)(new Color(color.getRGB())).getBlue() / 255.0F), 0.0D);
            GL11.glVertex3d(circle.x + x * circle.age / 1000.0D * 0.5D, circle.y, circle.z + z * circle.age / 1000.0D * 0.5D);
         }

         GL11.glEnd();
         AntiAliasingUtility.unhook(true, false, false);
         GL11.glEnable(3008);
         GlStateManager.enableTexture();
         GlStateManager.disableBlend();
         GL11.glEnable(2884);
         GlStateManager.clearColor(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDepthMask(true);
         GlStateManager.popMatrix();
      }
   }

   public static class Circle {
      double x;
      double y;
      double z;
      double age;

      public Circle(double x, double y, double z) {
         this.x = x;
         this.y = y;
         this.z = z;
         this.age = 0.0D;
      }
   }
}
