package sertyo.events.module.impl.player;

import com.darkmagician6.eventapi.EventTarget;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.FreeCamera;
import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.math.vector.Vector3d;
import sertyo.events.event.packet.EventPacket;
import sertyo.events.event.player.EventLivingUpdate;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.module.setting.impl.NumberSetting;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.move.MovementUtility;


@ModuleAnnotation(
   name = "FreeCam",
   category = Category.PLAYER
)
public class FreeCam extends Module {
   private final NumberSetting speed = new NumberSetting(
           "Скорость по XZ",
           1.0f,
           0.1f,
           5.0f,
           0.05f
   );
   private final NumberSetting motionY = new NumberSetting(
           "Скорость Y",
           0.5f,
           0.1f,
           1,
           0.05f
   );
   private Vector3d clientPosition = null;
   public FreeCamera player = null;
   boolean oldIsFlying;

   @EventTarget
   public void onLivingUpdate(EventLivingUpdate event) {
         if (player != null) {
            player.noClip = true;
            player.setOnGround(false);
            MovementUtility.setMotion(speed.get());
            if (Minecraft.getInstance().gameSettings.keyBindJump.isKeyDown()) {
               player.setPosition(player.getPosX(), player.getPosY() + motionY.get(), player.getPosZ());
            }
            if (Minecraft.getInstance().gameSettings.keyBindSneak.isKeyDown()) {
               player.setPosition(player.getPosX(), player.getPosY() - motionY.get(), player.getPosZ());
            }
            player.abilities.isFlying = true;

         }

   }

   @EventTarget
   public void onMotion(EventPacket event) {
         if (event.getPacket() instanceof CPlayerPacket p) {
            if (p.moving) {
               p.x = player.getPosX();
               p.y = player.getPosY();
               p.z = player.getPosZ();
            }
            p.onGround = player.isOnGround();
            if (p.rotating) {
               p.yaw = player.rotationYaw;
               p.pitch = player.rotationPitch;
            }
      }
   }
   @EventTarget
   public void onrender(EventRender2D event) {
         handleRender2DEvent(event);
   }

   @Override
   public void onEnable() {
      super.onEnable();
      if (mc.player == null) {
         return;
      }
      mc.player.setJumping(false);
      initializeFakePlayer();
      addFakePlayer();
      player.spawn();
      mc.player.movementInput = new MovementInput();
      mc.player.moveForward = 0;
      mc.player.moveStrafing = 0;
      mc.setRenderViewEntity(player);
   }

   /**
    * Обработчик события onDisable.
    * Удаляет фейкового игрока из мира.
    */
   @Override
   public void onDisable() {
      super.onDisable();
      if (mc.player == null) {
         return;
      }
      removeFakePlayer();
      mc.setRenderViewEntity(null);
      mc.player.movementInput = new MovementInputFromOptions(mc.gameSettings);

   }
   private void handleRender2DEvent(EventRender2D renderEvent) {
      MainWindow resolution = mc.getMainWindow();

      if (clientPosition == null) {
         return;
      }

      int xPosition = (int) (player.getPosX() - mc.player.getPosX());
      int yPosition = (int) (player.getPosY() - mc.player.getPosY());
      int zPosition = (int) (player.getPosZ() - mc.player.getPosZ());

      String position = "X:" + xPosition + " Y:" + yPosition + " Z:" + zPosition;


      Fonts.gilroyBold[16].drawCenteredStringWithOutline(new MatrixStack(),
              position,
              resolution.getScaledWidth() / 2F,
              resolution.getScaledHeight() / 2F + 10,
              -1);
   }

   /**
    * Инициализирует фейкового игрока.
    * Устанавливает начальные значения позиции и углов поворота.
    */
   private void initializeFakePlayer() {
      clientPosition = mc.player.getPositionVec();
      player = new FreeCamera(1337228);
      player.copyLocationAndAnglesFrom(mc.player);
      player.rotationYawHead = mc.player.rotationYawHead;
   }

   /**
    * Добавляет фейкового игрока в мир и сохраняет текущую позицию игрока.
    */
   private void addFakePlayer() {
      clientPosition = mc.player.getPositionVec();
      mc.world.addEntity(1337228, player);
   }

   /**
    * Удаляет фейкового игрока из мира.
    * Восстанавливает состояния и позицию игрока.
    */
   private void removeFakePlayer() {
      resetFlying();
      mc.world.removeEntityFromWorld(1337228);
      player = null;
      clientPosition = null;
   }

   /**
    * Сбрасывает состояние полета игрока, если оно было выключено до работы модуля.
    */
   private void resetFlying() {
      if (oldIsFlying) {
         mc.player.abilities.isFlying = false;
         oldIsFlying = false;
      }
   }
}
