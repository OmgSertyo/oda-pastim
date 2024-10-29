package sertyo.events.utility.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import sertyo.events.utility.Utility;
import sertyo.events.utility.math.MathUtility;

public class RotationUtility implements Utility {
   public static Vector2f getDeltaForCoord(Vector2f rot, Vector3d point) {
      ClientPlayerEntity client = Minecraft.getInstance().player;
      double x = point.x - client.getPosX();
      double y = point.y - client.getEyePosition(1.0F).y;
      double z = point.z - client.getPosZ();
      double dst = Math.sqrt(Math.pow(x, 2.0D) + Math.pow(z, 2.0D));
      float yawToTarget = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90.0D);
      float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(y, dst)));
      float yawDelta = MathHelper.wrapDegrees(yawToTarget - rot.x);
      float pitchDelta = pitchToTarget - rot.y;
      return new Vector2f(yawDelta, pitchDelta);
   }

   public static Vector2f getRotationForCoord(Vector3d point) {
      ClientPlayerEntity client = Minecraft.getInstance().player;
      double x = point.x - client.getPosX();
      double y = point.y - client.getEyePosition(1.0F).y;
      double z = point.z - client.getPosZ();
      double dst = Math.sqrt(Math.pow(x, 2.0D) + Math.pow(z, 2.0D));
      float yawToTarget = (float)MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(z, x)) - 90.0D);
      float pitchToTarget = (float)(-Math.toDegrees(Math.atan2(y, dst)));
      return new Vector2f(yawToTarget, pitchToTarget);
   }

   public static float[] getCorrectRotation(Vector3d original, boolean flag, float value) {
      Vector3d vec = new Vector3d(mc.player.getPosX(), mc.player.getBoundingBox().minY + (double)mc.player.getEyeHeight(), mc.player.getPosZ());
      double var4 = original.x - vec.x;
      double var6 = original.y - (mc.player.getPosY() + (double)mc.player.getEyeHeight() - (double)value);
      double var8 = original.z - vec.z;
      double var10 = Math.sqrt(var4 * var4 + var8 * var8);
      float var12 = (float)(Math.toDegrees(Math.atan2(var8, var4)) - 90.0D) + (float)(flag ? MathUtility.getRandomInRange(-2, 2) : 0);
      float var13 = (float)(-Math.toDegrees(Math.atan2(var6, var10))) + (float)(flag ? MathUtility.getRandomInRange(-2, 2) : 0);
      var12 = mc.player.rotationYaw + GCDFixUtility.getFixedRotation(MathHelper.wrapDegrees(var12 - mc.player.rotationYaw));
      var13 = mc.player.rotationPitch + GCDFixUtility.getFixedRotation(MathHelper.wrapDegrees(var13 - mc.player.rotationPitch));
      var13 = MathHelper.clamp(var13, -90.0F, 90.0F);
      return new float[]{var12, var13};
   }
}
