package sertyo.events.module.impl.render;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import jhlabs.vecmath.Matrix4f;
import jhlabs.vecmath.Vector4f;
import lombok.SneakyThrows;
import lombok.val;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.MemoryStack;
import sertyo.events.Main;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.impl.util.NameProtect;
import sertyo.events.module.setting.impl.MultiBooleanSetting;
import sertyo.events.ui.menu.main.CVec4;
import sertyo.events.utility.Utility;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.font.styled.StyledFont;
import sertyo.events.utility.math.MathUtility;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.RenderUtility;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.stream.Collectors;

@ModuleAnnotation(
   name = "EntityESP",
   category = Category.RENDER
)
public class EntityESP extends Module {
   public MultiBooleanSetting elements = new MultiBooleanSetting("Elements", Arrays.asList("Box", "Health"));
   public HashMap<Vector4f, PlayerEntity> positions = new HashMap<>();
   public CVec4 player = new CVec4(1,1,1,1);
   @EventTarget
   public void onRender2D(EventRender2D event) {
      {

         int uid = 0;


         val window = mc.getMainWindow();

         int[] viewport = new int[]{
                 0, 0,
                 window.getScaledWidth(), window.getScaledHeight()
         };
         var nameProtect = Main.getInstance().getModuleManager().getModule(NameProtect.class);
         var nameProtectEnabled = nameProtect.isEnabled();
         var customNameProtected = Main.username;
         for (Entity e : mc.world.getAllEntities()) {
            if (!(e instanceof PlayerEntity) || (e == mc.player && mc.gameSettings.getPointOfView().func_243192_a()))
               continue;

            float ab = 1f;

            AxisAlignedBB bb;

            {
               double x = e.lastTickPosX + (e.getPosX() - e.lastTickPosX) * mc.getRenderPartialTicks();
               double y = e.lastTickPosY + (e.getPosY() - e.lastTickPosY) * mc.getRenderPartialTicks();
               double z = e.lastTickPosZ + (e.getPosZ() - e.lastTickPosZ) * mc.getRenderPartialTicks();
               double width = e.getWidth() / 1.5;
               double height = e.getHeight() + ((e.isSneaking() || (e == mc.player && mc.player.isSneaking()) ? 0.15D : 0.2D));
               AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);

               bb = axisAlignedBB;
            }

            List<Vector3d> points = List.of(
                    new Vector3d(bb.minX, bb.minY, bb.minZ), new Vector3d(bb.maxX, bb.minY, bb.minZ),
                    new Vector3d(bb.maxX, bb.minY, bb.maxZ), new Vector3d(bb.minX, bb.minY, bb.maxZ),

                    new Vector3d(bb.minX, bb.maxY, bb.minZ), new Vector3d(bb.maxX, bb.maxY, bb.minZ),
                    new Vector3d(bb.maxX, bb.maxY, bb.maxZ), new Vector3d(bb.minX, bb.maxY, bb.maxZ)
            );

            val projectedPoints = points.stream()
                    .map(point -> new Vector3f((float) point.x, (float) point.y, (float) point.z))
                    .map(point -> {
                       double[] project = RenderUtil.Render2D.worldToScreen(point.x, point.y, point.z);
                       if (project != null)
                          return new Vector3f((float) project[0], (float) project[1], 0);
                       return new Vector3f(0, 0, 0);
                    }).collect(Collectors.toList());

            projectedPoints.sort(Comparator.comparingDouble(Vector3f::getX));

            float x = projectedPoints.get(0).x;
            float x2 = projectedPoints.get(projectedPoints.size() - 1).x;

            if ((x2 - x) >= mc.getMainWindow().getScaledWidth()) continue;

            projectedPoints.sort(Comparator.comparingDouble(Vector3f::getY));

            float y = projectedPoints.get(0).y;
            float y2 = projectedPoints.get(projectedPoints.size() - 1).y;

            if (x == 0 && x2 == 0) continue;

            float bw = x2 - x;
            float bh = y2 - y;

            renderBoxHP(e, x, y, bw, bh);
            if (e == mc.player) {
               player = new CVec4(x,y,bw,bh);
            }
            { // TODO: Render Entity Name! ( With Outline? )
               StyledFont font = Fonts.msBold[14]; // Условный размер, возможно стоит меньше

               //font.drawCenterX(name, x + bw / 2f, y + bh, -1);


               ITextComponent text = e.getDisplayName();
               TextComponent name = (TextComponent) text;

               String friendPrefix = Main.getInstance().getFriendManager().isFriend(e.getName().getString())
                       ? TextFormatting.GREEN + "[F] "
                       : "";
               ITextComponent friendText = ITextComponent.getTextComponentOrEmpty(friendPrefix);

               TextComponent friendPrefixComponent = (TextComponent) friendText;
               if (Main.getInstance().getFriendManager().isFriend(e.getName().getString()) && (nameProtectEnabled)) {
                  friendPrefixComponent.append(new StringTextComponent(customNameProtected));
               } else {
                  friendPrefixComponent.append(name);
               }
               name = friendPrefixComponent;
               if (((LivingEntity) e).getHealth() == 1000) {
                  name.append(new StringTextComponent(" " + TextFormatting.RED + "Invisible"));
               }else {
                  name.append(new StringTextComponent(" " + TextFormatting.RED + (int) ((LivingEntity) e).getHealth() + "HP"));
               }
               var z = x + bw / 2f
                       - mc.fontRenderer.getStringPropertyWidth(name) / 2f;
               var yz = y - mc.fontRenderer.FONT_HEIGHT - 2;


               var q = 1;
               float w = q * 2;
               RenderUtil.Render2D.drawRoundedRect(z - w, yz - q,
                       mc.fontRenderer.getStringPropertyWidth(name) + w,
                       mc.fontRenderer.FONT_HEIGHT + w, 1.5f, new Color(31, 31, 31, 100).getRGB());
               mc.fontRenderer.renderITextComponent(new MatrixStack(), name,
                       z, yz, -1);

               if (e instanceof PlayerEntity player){
                  float posY = (float) (yz);
                  float posX = (float) (z - w + (mc.fontRenderer.getStringPropertyWidth(name) + w) / 2);
                  float maxOffsetY = (font.getFontHeight() + 3);
                  final List<ItemStack> stacks = new ArrayList<>();

                  stacks.add(player.getHeldItemMainhand());
                  stacks.add(player.getHeldItemOffhand());
                  player.getArmorInventoryList().forEach(stacks::add);

                  stacks.removeIf(wz -> wz.getItem() instanceof AirItem);
                  int totalSize = stacks.size() * 10;

                  maxOffsetY += 12;
                  int iterable = 0;
                  for (ItemStack stack : stacks) {
                     if (stack != null) {
                        RenderHelper.enableStandardItemLighting();
                        drawItemStack(stack, posX + iterable * 20 - totalSize + 2,
                                posY - maxOffsetY + 1, null, false);
                        RenderHelper.disableStandardItemLighting();
                        iterable++;
                        ArrayList<String> lines = new ArrayList<>();
                        buildEnchantments(lines, stack);

                        int i = 0;
                        for (String s : lines) {
                           font.drawString(s,
                                   posX +
                                           iterable * 20 - totalSize - 18,
                                   (float) posY - (maxOffsetY) - (7) - (i * 7),
                                   new Color(200, 200, 200, 255).getRGB());
                           i++;
                        }
                     }
                  }
               }
            }

            uid++;
         }

      }

   }

   public static void drawItemStack(ItemStack stack, double x, double y, String altText, boolean withoutOverlay) {

      GL11.glPushMatrix();
      GL11.glScalef(-0.01f * -200, -0.01f * -200, -0.01f * -200);
      GL11.glPushMatrix();
      GlStateManager.disableLighting();
      GL11.glPopMatrix();
      GlStateManager.scaled(0.5, 0.5, 0.5);
      GL11.glEnable(GL11.GL_DEPTH_TEST);
      mc.getItemRenderer().renderItemIntoGUI(stack, (int) x, (int) y);

      String count = "";
      if (stack.getCount() > 1) {
         count = stack.getCount() + "";
      }
      mc.getItemRenderer().renderItemOverlayIntoGUI(mc.fontRenderer, stack, (int) x, (int) y, count);
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableLighting();
      GL11.glPopMatrix();
      GL11.glDisable(GL11.GL_DEPTH_TEST);
   }

   public void renderBoxHP(Entity e, float x, float y, float bw, float bh) {
      float radius = (float) MathUtility.clamp((float) (Math.hypot(bh, bw) / 50), 1, 5);
      int colorSpeed = 20;

      boolean rb = false;

      Color c1 = new Color(255, 0, 0);
      Color c2 = new Color(255, 153, 0);
      Color c3 = new Color(34, 255, 0);
      Color c4 = new Color(0, 166, 255);
      Color c5 = new Color(208, 0, 255);



      float rhq = (float) MathUtility.clamp((float) (Math.hypot(bh, bw) / 5), 2, 5);

      IRenderCall outline = () -> { // TODO: Render Outline ( BackGround )
         float qq = 0.5f;
         float ww = qq * 2f;
         {
            //  RenderUtil.Render2D.drawRoundedRect(x - qq, y - qq,
            //            bw + ww
            //             , bh + ww
            //              , radius, java.awt.Color.BLACK.getRGB());
         }




         float value = ((PlayerEntity) e).getTotalArmorValue() / 20f;

         if (value > 0) {
            float hy = (float) (y + bh + rhq / 2f);

            float q = 1f;
            float w = q * 2f;
            RenderUtil.Render2D.drawRoundedRect(x, hy, bw, 4, 1, new Color(31, 31, 31).getRGB());

            Color br1 = new Color(0, 48, 120);
            Color br2 = new Color(92, 156, 253);

            RenderUtil.Render2D.drawGradientRound(x + q, hy + q, Math.min(1f, value) * bw - w, 4 - w, 0,
                    br1.getRGB(), br1.getRGB(),
                    br2.getRGB(), br2.getRGB());
         }
      };


      IRenderCall health = () -> { // TODO: Render Health + ( Armor ( todo ) )*
         float hx = (float) (x - rhq);
         float q = 1.25f;
         float w = q * 2f;
         //ShaderRender.roundedRectangle(hx, y, 4, bh, radius / 2f, new Color(31, 31, 31));
         int color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0].getRGB();
         int color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1].getRGB();
         RenderUtil.Render2D.drawGradientRound(hx + q, y + q, 4 - w, (bh - w) * Math.min(1, ((LivingEntity) e).getHealth() / ((LivingEntity) e).getMaxHealth()), radius / 2f,
                 color, color2,
                 color, color2);

      };
      if (elements.get(0)) {
         outline.execute();
      }
      if (elements.get(1)) {
         health.execute();
      }
   }

   public void renderOnlyHP(Entity e, float x, float y, float bw, float bh) {
      if (e instanceof LivingEntity entity) {
         float rhq = (float) MathUtility.clamp((float) (Math.hypot(bh, bw) / 5), 2, 5);
         float radius = (float) MathUtility.clamp((float) (Math.hypot(bh, bw) / 50), 1, 5);
         IRenderCall health = () -> { // TODO: Render Health + ( Armor ( todo ) )*
            float hx = (float) (x - rhq);
            float q = 1.25f;
            float w = q * 2f;
            RenderUtil.Render2D.drawRoundedRect(hx + q, y + q, 4 - w, bh, radius / 2f, new Color(31, 31, 31).getRGB());

            float factor = entity.getMaxHealth() / entity.getMaxHealth();
            Color c = factor > 0.8f ? Color.GREEN : factor > 0.4f ? Color.YELLOW : factor > 0f ? Color.RED : Color.GREEN;
            RenderUtil.Render2D.drawRoundedRect(hx + q, y + q, 4 - w, (bh - w) * Math.min(1, ((LivingEntity) e).getHealth() / ((LivingEntity) e).getMaxHealth()), radius / 2f,
                    c.getRGB());
         };
         health.execute();
      }
   }



   private static Matrix4f convertMatrices(net.minecraft.util.math.vector.Matrix4f matrix4f) {
      try (MemoryStack memoryStack = MemoryStack.stackPush()) {
         val buffer = memoryStack.mallocFloat(4 * 4);
         matrix4f.write(buffer);
         return new Matrix4f(buffer.array());
      }
   }

   public static void buildEnchantments(ArrayList<String> list, ItemStack stack) {
      Item item = stack.getItem();
      int protection = EnchantmentHelper.getEnchantmentLevel(Enchantments.PROTECTION, stack);
      int thorns = EnchantmentHelper.getEnchantmentLevel(Enchantments.THORNS, stack);
      int unbreaking = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack);
      int mending = EnchantmentHelper.getEnchantmentLevel(Enchantments.MENDING, stack);
      int feather = EnchantmentHelper.getEnchantmentLevel(Enchantments.FEATHER_FALLING, stack);
      int depth = EnchantmentHelper.getEnchantmentLevel(Enchantments.DEPTH_STRIDER, stack);
      int vanishing_curse = EnchantmentHelper.getEnchantmentLevel(Enchantments.VANISHING_CURSE, stack);
      int binding_curse = EnchantmentHelper.getEnchantmentLevel(Enchantments.BINDING_CURSE, stack);
      int sweeping = EnchantmentHelper.getEnchantmentLevel(Enchantments.SWEEPING, stack);
      int sharpness = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, stack);
      int looting = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack);
      int infinity = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack);
      int power = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
      int punch = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
      int flame = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack);
      int knockback = EnchantmentHelper.getEnchantmentLevel(Enchantments.KNOCKBACK, stack);
      int fireAspect = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_ASPECT, stack);
      int efficiency = EnchantmentHelper.getEnchantmentLevel(Enchantments.EFFICIENCY, stack);
      int silktouch = EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack);
      int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
      int fireprot = EnchantmentHelper.getEnchantmentLevel(Enchantments.FIRE_PROTECTION, stack);
      int blastprot = EnchantmentHelper.getEnchantmentLevel(Enchantments.BLAST_PROTECTION, stack);

      if (item instanceof AxeItem) {
         if (sharpness > 0) {
            list.add("Ос" + sharpness);
         }
         if (efficiency > 0) {
            list.add("Эф" + efficiency);
         }
         if (unbreaking > 0) {
            list.add("Пр" + unbreaking);
         }
      }
      if (item instanceof ArmorItem) {
         if (vanishing_curse > 0) {
            list.add("§cут ");
         }
         if (binding_curse > 0) {
            list.add("§cне ");
         }
         if (fireprot > 0) {
            list.add("Ог" + fireprot);
         }
         if (blastprot > 0) {
            list.add("Cп" + blastprot);
         }
         if (depth > 0) {
            list.add("Гл" + depth);
         }
         if (feather > 0) {
            list.add("Пе" + feather);
         }
         if (protection > 0) {
            list.add("За" + protection);
         }
         if (thorns > 0) {
            list.add("Ши" + thorns);
         }
         if (mending > 0) {
            list.add("По");
         }
         if (unbreaking > 0) {
            list.add("Пр" + unbreaking);
         }
      }
      if (item instanceof BowItem) {
         if (vanishing_curse > 0) {
            list.add("§cут");
         }
         if (binding_curse > 0) {
            list.add("§cне ");
         }
         if (infinity > 0) {
            list.add("Бе" + infinity);
         }
         if (power > 0) {
            list.add("Си" + power);
         }
         if (punch > 0) {
            list.add("От" + punch);
         }
         if (mending > 0) {
            list.add("По ");
         }
         if (flame > 0) {
            list.add("По" + flame);
         }
         if (unbreaking > 0) {
            list.add("Пр" + unbreaking);
         }
      }
      if (item instanceof SwordItem) {
         if (vanishing_curse > 0) {
            list.add("§cут ");
         }
         if (looting > 0) {
            list.add("До" + looting);
         }
         if (binding_curse > 0) {
            list.add("§cНе");
         }
         if (sweeping > 0) {
            list.add("По" + sweeping);
         }
         if (sharpness > 0) {
            list.add("Ос" + sharpness);
         }
         if (knockback > 0) {
            list.add("От" + knockback);
         }
         if (fireAspect > 0) {
            list.add("За" + fireAspect);
         }
         if (unbreaking > 0) {
            list.add("Пр" + unbreaking);
         }
         if (mending > 0) {
            list.add("Men ");
         }
      }
      if (item instanceof ToolItem) {
         if (unbreaking > 0) {
            list.add("Пр" + unbreaking);
         }
         if (mending > 0) {
            list.add("По ");
         }
         if (vanishing_curse > 0) {
            list.add("Ва ");
         }
         if (binding_curse > 0) {
            list.add("Би ");
         }
         if (efficiency > 0) {
            list.add("Эф" + efficiency);
         }
         if (silktouch > 0) {
            list.add("Ше" + silktouch);
         }
         if (fortune > 0) {
            list.add("Уд" + fortune);
         }
      }
   }
}
