package sertyo.events.ui.testGUI;


import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.glfw.GLFW;
import sertyo.events.Main;
import sertyo.events.utility.IWrapper;
import sertyo.events.utility.Utility;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.math.ScaleUtils;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.ScaleMath;
import sertyo.events.utility.render.Vec2i;
import sertyo.events.utility.render.animation.Animation;
import sertyo.events.utility.render.animation.Direction;
import sertyo.events.utility.render.animation.impl.DecelerateAnimation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Panel extends Screen implements Utility {

    private final Animation animation = new DecelerateAnimation(300, 1);
    private Vector2f position = new Vector2f(0, 0);
    private Vector2f size = new Vector2f(0, 0);
    private float animationSecond;
    public static float scrollingOut;
    public static float scrolling;
    private float animationFirst;

    public Panel() {
        super(new StringTextComponent("ClickGui"));
        scrolling = 0;

    }

    @Override
    protected void init() {
        super.init();
        size = new Vector2f(340, 270);
        animation.setDirection(Direction.FORWARDS);
        position = new Vector2f(sr.scaledWidth / 2f - size.x / 2f, sr.scaledHeight / 2f - size.y / 2f);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        Vec2i fixed = Main.getInstance().getScaleMath().getMouse(mouseX, mouseY, sr);
        mouseX = fixed.getX();
        mouseY = fixed.getY();

        mc.gameRenderer.setupOverlayRendering(2);

        if (animation.finished(Direction.BACKWARDS)) mc.displayGuiScreen(null);

        ScaleUtils.scaleStart(position.x - (width + size.x) / 2f, position.y + size.y / 2f, animation.getOutput());
        renderPanel(matrixStack, mouseX, mouseY);
        ScaleUtils.scaleEnd();

        scrollingOut = ScaleUtils.lerp(scrollingOut, scrolling, 10);
        mc.gameRenderer.setupOverlayRendering();
    }

    private void renderPanel(MatrixStack matrixStack, int mouseX, int mouseY) {
        if (animation.isDone()) IWrapper.blurQueue.add(() -> RenderUtil.Render2D.drawRoundedRect(position.x, position.y, size.x, size.y, 10, ColorUtil.rgba(20, 20, 20, 200)));

        RenderUtil.Render2D.drawRoundedRect(position.x, position.y, size.x, size.y, 10, ColorUtil.rgba(20, 20, 20, 200));
        RenderUtil.Render2D.drawRoundedCorner(position.x + 5, position.y + 5, 90, size.y - 10, 8, ColorUtil.rgba(20, 20, 20, 130), RenderUtil.Render2D.Corner.LEFT);

        RenderUtil.Render2D.drawRoundedRect(position.x + 95, position.y + 5, 0.5f, size.y - 10, 0, ColorUtil.rgba(130, 130, 130, 40));
        RenderUtil.Render2D.drawRoundedRect(position.x + 10, position.y + 35, 80, 0.5f, 0, ColorUtil.rgba(130, 130, 130, 40));
        RenderUtil.Render2D.drawRoundedRect(position.x + 10, position.y + size.y - 35, 80, 0.5f, 0, ColorUtil.rgba(130, 130, 130, 40));
        //Fonts.fontBold[32].drawCenteredString(matrixStack, ClientUtils.gradient("Wissend"), position.x + 100 / 2f, position.y + 12.5f, -1);
        Fonts.msMedium[14].drawString(matrixStack, "Main", position.x + 10, position.y + 40, ColorUtil.rgba(200, 200, 200, 150));
        Fonts.msMedium[14].drawString(matrixStack, "Other", position.x + 10, position.y + 175, ColorUtil.rgba(200, 200, 200, 150));

        renderUserProfile(matrixStack);


    }


    private void renderUserProfile(MatrixStack matrixStack) {

        Fonts.msMedium[16].drawString(matrixStack, "Heliw", position.x + 34, position.y + size.y - 27.5f, -1);
        Fonts.msMedium[16].drawString(matrixStack, "UID: 1", position.x + 34, position.y + size.y - 18.5f, ColorUtil.rgba(255, 255, 255, 100));
    }




    @Override
    public boolean isPauseScreen() {
        return false;
    }
}