package sertyo.events.module.impl.util;

import com.darkmagician6.eventapi.EventTarget;

import net.minecraft.network.play.client.CPlayerPacket;
import net.minecraft.util.math.MathHelper;
import sertyo.events.event.player.EventUpdate;
import sertyo.events.module.Category;
import sertyo.events.module.Module;
import sertyo.events.module.ModuleAnnotation;
import sertyo.events.utility.Utility;
import sertyo.events.utility.move.MovementUtility;

@ModuleAnnotation(
        name = "Phase",
        category = Category.UTIL
)
public class Phase extends Module {

    private String lastMessage = "";
    private int amount;
    private int line;

    @EventTarget
    public void onUpdate(EventUpdate event) {
        MovementUtility.setMotion(0);

        float yaw = (float) Math.toRadians((float) MathHelper.roundUp((int) MathHelper.wrapDegrees(MovementUtility.getDirection()), 45));
        double x = Utility.mc.player.getPosX() + -MathHelper.sin(yaw) * 0.0001;
        double z = Utility.mc.player.getPosZ() + MathHelper.cos(yaw) * 0.0001;

        double x2 = Utility.mc.player.getPosX() + -MathHelper.sin(yaw) * 1.6;
        double z2 = Utility.mc.player.getPosZ() + MathHelper.cos(yaw) * 1.6;
        double y = Utility.mc.player.getPosY();


        Utility.mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x, y, z, false));
        Utility.mc.player.connection.sendPacket(new CPlayerPacket.PositionPacket(x2, y, z2, true));

        Utility.mc.player.setPosition(x2, y, z2);

        }
    }

