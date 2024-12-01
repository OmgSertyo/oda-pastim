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
        size = new Vector2f(171, 379);
        animation.setDirection(Direction.FORWARDS);
        position = new Vector2f(sr.scaledWidth / 2f - size.x / 2f, sr.scaledHeight / 2f - size.y / 2f);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);

        RenderUtil.Render2D.drawRoundedRect(position.x + 95, position.y + 5, 171, 379, 0, ColorUtil.rgba(130, 130, 130, 40));

    }



}