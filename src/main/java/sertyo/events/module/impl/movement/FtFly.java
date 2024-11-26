package sertyo.events.module.impl.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.multiplayer.PlayerController;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import sertyo.events.event.player.EventMotion;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.math.MouseUtil;
import sertyo.events.utility.misc.ChatUtility;
import sertyo.events.utility.misc.TimerHelper;

@ModuleAnnotation(name = "FtFly", category = Category.MOVEMENT)
public class FtFly extends Module {
    TimerHelper stopWatch = new TimerHelper();

    @EventTarget
    public void onUpdate(EventMotion event) {

        if (!mc.player.collidedHorizontally) {
            return;
        }
        long speed = 200;
        if (stopWatch.hasReached(speed)) {
            event.setOnGround(true);
            mc.player.setOnGround(true);
            mc.player.collidedVertically = true;
            mc.player.collidedHorizontally = true;
            mc.player.isAirBorne = true;
            mc.player.jump();
            placeFenceStack(event);
            stopWatch.reset();
        }
    }

    private void placeFenceStack(EventMotion motion) {
        int slotFence = getSlotForFence();
        if (slotFence == -1) {
            ChatUtility.addChatMessage("Заборы не найдены!");
            toggle();
            return;
        }

        int lastSlot = mc.player.inventory.currentItem;
        mc.player.inventory.currentItem = slotFence;

        BlockPos playerPos = mc.player.getPosition();

        for (int i = 1; i <= 2; i++) {
            BlockPos fencePos = playerPos.up(i);
            if (mc.world.getBlockState(fencePos).isAir()) {
                placeFenceBlock(fencePos, Hand.MAIN_HAND, mc.playerController, mc.world, mc.player);
            }
        }

        mc.player.inventory.currentItem = lastSlot;
    }
    private void placeFenceBlock(BlockPos pos, Hand hand, PlayerController playerController, World world, ClientPlayerEntity player) {
        if (!world.getBlockState(pos).isAir()) {
            return;
        }

        Direction direction = Direction.UP;
        BlockPos tPos = pos.down();
        BlockRayTraceResult traceResult = new BlockRayTraceResult(
                new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5),
                direction, tPos, false);

        playerController.processRightClick(player, (ClientWorld) world, hand);
        player.swingArm(hand);
    }


    private int getSlotForFence() {
        for (int i = 0; i < 36; i++) {
            if (mc.player.inventory.getStackInSlot(i).getItem() == Items.END_ROD) {
                return i;
            }
        }
        return -1;
    }


}
