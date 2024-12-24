package sertyo.events.command.impl;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.command.Command;
import sertyo.events.command.CommandAbstract;
import sertyo.events.module.impl.render.Arraylist;
import sertyo.events.utility.misc.ChatUtility;
import sertyo.events.utility.font.Fonts;

import java.awt.*;

import static sertyo.events.utility.Utility.mc;
import static sertyo.events.utility.render.RenderUtility.Render2D.drawShadow;
import static sertyo.events.utility.render.RenderUtility.Render2D.drawTriangle;

@Command(
        name = "gps",
        description = "Указывает корды до точки"
)
public class GpsCommand extends CommandAbstract {
    public static boolean enabled;

    public static Vector3d vector3d;
    public static void drawArrow(MatrixStack stack) {
        if (!enabled)
            return;

        double x = vector3d.x - mc.getRenderManager().info.getProjectedView().getX();
        double z = vector3d.z - mc.getRenderManager().info.getProjectedView().getZ();

        double cos = MathHelper.cos((float) (mc.player.rotationYaw * (Math.PI * 2 / 360)));
        double sin = MathHelper.sin((float) (mc.player.rotationYaw * (Math.PI * 2 / 360)));
        double rotY = -(z * cos - x * sin);
        double rotX = -(x * cos + z * sin);
        double dst = Math.sqrt(Math.pow(vector3d.x - mc.player.getPosX(), 2) + Math.pow(vector3d.z - mc.player.getPosZ(), 2));

        float angle = (float) (Math.atan2(rotY, rotX) * 180 / Math.PI);
        double x2 = 75 * MathHelper.cos((float) Math.toRadians(angle)) + mc.getMainWindow().getScaledWidth() / 2f;
        double y2 = 75 * (mc.player.rotationPitch / 90) * MathHelper.sin((float) Math.toRadians(angle)) + mc.getMainWindow().getScaledHeight() / 2f;

        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.translated(x2, y2, 0);
        GlStateManager.rotatef(angle, 0, 0, 1);

        Color clr = Arraylist.getColor(100);

        drawShadow(-3F, -3F, 8, 6F, 8, clr.getRGB());
        drawTriangle(-4, -1F, 4F, 7F, new Color(0, 0, 0, 32));
        drawTriangle(-3F, 0F, 3F, 5F, new Color(clr.getRGB()));
        GlStateManager.rotatef(90, 0, 0, 1);

        Fonts.gilroy[14].drawCenteredStringWithOutline(stack, (int) dst + "m", 0, 15, -1);

        GlStateManager.enableBlend();
        GlStateManager.popMatrix();
    }
    public void execute(String[] args) throws Exception {
        if (args.length > 1) {
            if (args[1].equalsIgnoreCase("off")) {
                ChatUtility.addChatMessage(TextFormatting.GRAY + "Навигатор выключен!");

                enabled = false;
                vector3d = null;
                return;
            }
            if (args.length == 3) {
                int x = Integer.parseInt(args[1]), y = Integer.parseInt(args[2]);
                enabled = true;
                vector3d = new Vector3d(x, 0, y);
                ChatUtility.addChatMessage(TextFormatting.GRAY + "Навигатор включен! Координаты " + x + ";" + y);
            }
        } else {
            error();
        }
    }
    public void error() {
        ChatUtility.addChatMessage("" + TextFormatting.GRAY + TextFormatting.GRAY + ":");
        ChatUtility.addChatMessage("" + TextFormatting.WHITE + ".way " + TextFormatting.WHITE + "<" + TextFormatting.GRAY + "x, z" + TextFormatting.RED + ">");
        ChatUtility.addChatMessage("" + TextFormatting.WHITE + ".way " + TextFormatting.WHITE + "<" + TextFormatting.GRAY + "off" + TextFormatting.RED + ">");
    }
}
