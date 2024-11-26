package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.play.client.CHeldItemChangePacket;
import net.minecraft.network.play.client.CPlayerTryUseItemOnBlockPacket;
import net.minecraft.network.play.server.SMultiBlockChangePacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import sertyo.events.event.packet.EventPacket;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.event.render.EventRender2D;
import sertyo.events.event.render.EventRender3D;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.Utility;
import sertyo.events.utility.render.ColorUtility;
import sertyo.events.utility.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;

import static sertyo.events.utility.Utility.mc;
@ModuleAnnotation(
        name = "AncientXray",
        category = Category.UTIL
)
public class XRay extends Module {
    private final ArrayList<BlockPos> ores = new ArrayList<>();


        @EventTarget
        public void onPacket(EventPacket e) {
            if (e.getPacket() instanceof SMultiBlockChangePacket packet) {
                packet.func_244310_a((blockPos, blockState) -> {
                    BlockPos bp = blockPos.add(0,0,0);
                    if (blockState.getBlock().equals(Blocks.ANCIENT_DEBRIS) && !ores.contains(bp)) {
                        ores.add(bp);
                    }
                });
            }
        }

        @EventTarget
        public void onRender3D(EventRender3D e) {
            ores.removeIf(pos -> !mc.world.getBlockState(pos).getBlock().equals(Blocks.ANCIENT_DEBRIS));
            ores.forEach(pos -> RenderUtil.Render3D.drawBlockBox(pos, new Color(0xC3A278).getRGB()));
        }
}