package sertyo.events.utility.move;


import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import sertyo.events.utility.Utility;

import java.util.List;

public class MovementUtility implements Utility {
   public static double[] forward(double d) {
      float f = mc.player.movementInput.moveForward;
      float f2 = mc.player.movementInput.moveStrafe;
      float f3 = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
      if (f != 0.0F) {
         if (f2 > 0.0F) {
            f3 += (float)(f > 0.0F ? -45 : 45);
         } else if (f2 < 0.0F) {
            f3 += (float)(f > 0.0F ? 45 : -45);
         }

         f2 = 0.0F;
         if (f > 0.0F) {
            f = 1.0F;
         } else if (f < 0.0F) {
            f = -1.0F;
         }
      }

      double d2 = Math.sin(Math.toRadians((double)(f3 + 90.0F)));
      double d3 = Math.cos(Math.toRadians((double)(f3 + 90.0F)));
      double d4 = (double)f * d * d3 + (double)f2 * d * d2;
      double d5 = (double)f * d * d2 - (double)f2 * d * d3;
      return new double[]{d4, d5};
   }

   public static boolean reason(boolean water) {
      // Получаем текущие координаты игрока
      BlockPos playerPos = new BlockPos(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ());
      World world = mc.world;

// Проверяем, находится ли игрок в паутине
      boolean inWeb = world.getBlockState(playerPos.down()).getBlock() == Blocks.COBWEB;
      boolean critWater = (water &&
              (mc.world.getBlockState(new BlockPos(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ())).getFluidState().getFluid() instanceof WaterFluid ||
                      mc.world.getBlockState(new BlockPos(mc.player.getPosX(), mc.player.getPosY(), mc.player.getPosZ())).getFluidState().getFluid() instanceof LavaFluid) &&
              mc.world.getBlockState(new BlockPos(mc.player.getPosX(), mc.player.getPosY() + 1.0D, mc.player.getPosZ())).getBlock() instanceof AirBlock);
      return mc.player.isPotionActive(Effects.BLINDNESS) || mc.player.isOnLadder() || mc.player.isInWater() && !critWater || inWeb || mc.player.abilities.isFlying;
   }





   public static boolean isMoving() {
      return mc.player.movementInput.moveForward != 0.0F || mc.player.movementInput.moveStrafe != 0.0F;
   }


   public static float getDirection() {
      float rotationYaw = mc.player.rotationYaw;
      float strafeFactor = 0.0F;
      if (mc.player.movementInput.moveForward > 0.0F) {
         strafeFactor = 1.0F;
      }

      if (mc.player.movementInput.moveForward < 0.0F) {
         strafeFactor = -1.0F;
      }

      if (strafeFactor == 0.0F) {
         if (mc.player.movementInput.moveStrafe > 0.0F) {
            rotationYaw -= 90.0F;
         }

         if (mc.player.movementInput.moveStrafe < 0.0F) {
            rotationYaw += 90.0F;
         }
      } else {
         if (mc.player.movementInput.moveStrafe > 0.0F) {
            rotationYaw -= 45.0F * strafeFactor;
         }

         if (mc.player.movementInput.moveStrafe < 0.0F) {
            rotationYaw += 45.0F * strafeFactor;
         }
      }

      if (strafeFactor < 0.0F) {
         rotationYaw -= 180.0F;
      }

      return (float)Math.toRadians((double)rotationYaw);
   }

   public static void setMotion(double motion) {
      double forward = (double)mc.player.movementInput.moveForward;
      double strafe = (double)mc.player.movementInput.moveStrafe;
      float yaw = mc.player.rotationYaw;
      if (forward == 0.0D && strafe == 0.0D) {
         mc.player.setMotion(0, mc.player.getMotion().y, 0);
      } else {
         if (forward != 0.0D) {
            if (strafe > 0.0D) {
               yaw += (float)(forward > 0.0D ? -45 : 45);
            } else if (strafe < 0.0D) {
               yaw += (float)(forward > 0.0D ? 45 : -45);
            }

            strafe = 0.0D;
            if (forward > 0.0D) {
               forward = 1.0D;
            } else if (forward < 0.0D) {
               forward = -1.0D;
            }
         }

         double sin = (double)MathHelper.sin((float)Math.toRadians((double)(yaw + 90.0F)));
         double cos = (double)MathHelper.cos((float)Math.toRadians((double)(yaw + 90.0F)));
         mc.player.setMotion(forward * motion * cos + strafe * motion * sin, mc.player.getMotion().y, forward * motion * sin - strafe * motion * cos);
      }

   }

   public static float getMotion() {
      return (float)Math.sqrt(mc.player.getMotion().x * mc.player.getMotion().x + mc.player.getMotion().z * mc.player.getMotion().z);
   }

   public static boolean isInLiquid() {
      return mc.player.isInWater() || mc.player.isInLava();
   }
}
