package sertyo.events.ui.menu.altmanager;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.renderer.RenderSkybox;
import net.minecraft.client.renderer.RenderSkyboxCube;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import sertyo.events.ui.menu.main.NeironMainMenu;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.font.Fonts;


import java.awt.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static sertyo.events.utility.Utility.mc;

public class AltManagerScreen extends Screen implements INestedGuiEventHandler {

    public static final RenderSkyboxCube PANORAMA_RESOURCES = new RenderSkyboxCube(new ResourceLocation("textures/gui/title/background/panorama"));
    private static final ResourceLocation PANORAMA_OVERLAY_TEXTURES = new ResourceLocation("textures/gui/title/background/panorama_overlay.png");
    private final RenderSkybox panorama = new RenderSkybox(PANORAMA_RESOURCES);

    private final Screen lastScreen;

    private long firstRenderTime;

    public static CopyOnWriteArrayList<Alt> alts = new CopyOnWriteArrayList<>();

    boolean initbuttons = false;


    public AltManagerScreen(Screen parentScreen) {
        super(new StringTextComponent(""));
        this.lastScreen = parentScreen;
    }

    float scroll = 0;

    int windowWidth = 350;
    int windowHeight = 225;

    int windowX, windowY;

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.disableCull();
        GL11.glBegin(7);
        GL11.glVertex2f(-1.0F, -1.0F);
        GL11.glVertex2f(-1.0F, 1.0F);
        GL11.glVertex2f(1.0F, 1.0F);
        GL11.glVertex2f(1.0F, -1.0F);
        GL11.glEnd();
        GL20.glUseProgram(0);
        GlStateManager.popMatrix();
        mc.gameRenderer.setupOverlayRendering();

        if(buttons.isEmpty()) initbuttons = false;

        if (this.firstRenderTime == 0L) {
            this.firstRenderTime = Util.milliTime();
        }
        Minecraft mc = Minecraft.getInstance();
        float f = (float) (Util.milliTime() - this.firstRenderTime) / 1000.0F;
//        shaderUtil.init();
//        shaderUtil.setUniformf("resolution", minecraft.getMainWindow().getScaledWidth(), minecraft.getMainWindow().getScaledHeight());
//        shaderUtil.setUniformf("time", (System.currentTimeMillis() - MainMenuScreen.initTime) / 1000f);
//        shaderUtil.drawQuads();
//        shaderUtil.unload();
//        GaussianBlur.renderBlur(20);
        int x = minecraft.getMainWindow().getScaledWidth();
        int y = minecraft.getMainWindow().getScaledHeight();



         windowX = (x / 2) - (windowWidth / 2);
         windowY = (y / 2) - (windowHeight / 2);


        RenderUtil.Render2D.drawShadow(windowX, windowY, windowWidth, windowHeight, 8, new Color(17, 17, 17,100).getRGB());
        RenderUtil.Render2D.drawRoundedRect(windowX, windowY, windowWidth, windowHeight, 6, new Color(17, 17, 17,130).getRGB());

        Fonts.msBold[16].drawCenteredString(matrixStack, "Account Manager ", x / 2 + 16,  (y / 2) - (windowHeight / 2) + 10, -1);

        int offset = 0;
        for(Widget button : buttons){
            button.x = windowX + 25 + offset;
            button.y = windowY + windowHeight - button.getHeightRealms() - 8;
            offset+= button.getWidth() + 6;
        }


        int maxScroll = 34 * alts.size();

        if(alts.size() > 5){
            maxScroll = 34 * alts.size() - 150;
        }

        if(scroll <= -maxScroll){
            scroll = -maxScroll;
        }

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        //RenderUtil.Render2D.scaleStart(windowX, windowY + 27, (windowWidth * 2),  windowHeight + 50);

        int altOffset = 0;
        for(Alt alt : alts){
            alt.x = windowX + (windowWidth / 2) - alt.width / 2;
            alt.y = windowY + + scroll + 30 + altOffset;
            altOffset+= alt.height + 4;
            alt.draw(mouseX, mouseY);
        }

        if(!initbuttons){
            this.addButton(new Button(10, 10, 150, 15, new TranslationTextComponent("Add"), (p_lambda$init$1_1_) ->
            {
                alts.add(new Alt());
            }));
            this.addButton(new Button(10, 10, 150, 15, new TranslationTextComponent("Clear all"), (p_lambda$init$1_1_) ->
            {
                alts.clear();
            }));
            initbuttons = true;
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(keyCode == GLFW.GLFW_KEY_ESCAPE){
            Minecraft.getInstance().displayGuiScreen(new NeironMainMenu());
        }
        for(Alt alt : alts) {
            alt.keyPressed(keyCode, scanCode, modifiers);
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Alt alt : alts){
            boolean check = alt.getY() > this.buttons.get(0).y - 20;
            if (!check){
                alt.mouseClicked((int) mouseX, (int) mouseY, button);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        for(Alt alt : alts){
            alt.charTyped(codePoint, modifiers);
        }
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {

        if(scroll >= 0){
            scroll = 0;
        }

        if(alts.size() > 5) {
            if (delta > 0) {
                this.scroll += 12f;
            } else if (delta < 0) {
                this.scroll -= 12f;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    public static class Button extends AbstractButton {

        public static final net.minecraft.client.gui.widget.button.Button.ITooltip field_238486_s_ = (button, matrixStack, mouseX, mouseY) -> {
        };
        protected final net.minecraft.client.gui.widget.button.Button.IPressable onPress;
        protected final net.minecraft.client.gui.widget.button.Button.ITooltip onTooltip;

        public Button(int x, int y, int width, int height, ITextComponent title, net.minecraft.client.gui.widget.button.Button.IPressable pressedAction) {
            this(x, y, width, height, title, pressedAction, field_238486_s_);
        }

        public Button(int x, int y, int width, int height, ITextComponent title, net.minecraft.client.gui.widget.button.Button.IPressable pressedAction, net.minecraft.client.gui.widget.button.Button.ITooltip onTooltip) {
            super(x, y, width, height, title);
            this.onPress = pressedAction;
            this.onTooltip = onTooltip;
        }

        public float animation;

        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            GlStateManager.pushMatrix();
            animation = MathHelper.lerp(animation, isHovered() ? 1f : 0, 10);

            RenderUtil.Render2D.drawRoundedRect(x + width / 2f - 50, y + height / 2f - Fonts.msBold[17].getFontHeight() / 2f, 100, 15, 2, ColorUtil.setAlpha(0, 70));
            Fonts.msBold[17].drawCenteredString(matrixStack, this.getMessage().getString(), x + width / 2f, y + height / 2f - Fonts.msBold[17].getFontHeight() / 2f + 1.5f, Color.GRAY.brighter().getRGB());
            GlStateManager.popMatrix();
        }

        public void onPress() {
            this.onPress.onPress(this);
        }

    }
}
