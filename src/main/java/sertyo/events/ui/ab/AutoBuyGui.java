package sertyo.events.ui.ab;

import com.mojang.blaze3d.matrix.MatrixStack;
import sertyo.events.Main;
import sertyo.events.ui.ab.manager.*;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.glfw.GLFW;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.Animation;

import java.awt.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static sertyo.events.utility.Utility.mc;


public class AutoBuyGui extends Screen {
    public double x, y, w = 200, h = 200;
    public ConcurrentLinkedQueue<AutoBuyButton> autoBuyButtons = new ConcurrentLinkedQueue<>();

    public SelectWindow selectWindow;

    public String search = "";
    public boolean write;
    public double scroll = 0, targetScroll;
    public int dot = 0;

    public AutoBuyGui() {
        super(ITextComponent.getTextComponentOrEmpty("ab"));

        x = (double) mc.getMainWindow().getScaledWidth() / 2 - w / 2;
        y = (double) mc.getMainWindow().getScaledHeight() / 2 - h / 2;

        selectWindow = new SelectWindow(x + 210, y, w, h);

        double y2 = y + 26;
        for (AutoBuyItem item : AutoBuyManager.getItems()) {
            autoBuyButtons.add(new AutoBuyButton(x, y2, w, 18, item, item instanceof CustomAutoBuyItem ? ((CustomAutoBuyItem) item).name : item.item.getName().getString()));
            y2 += 20;
        }
    }

    public void open() {

        selectWindow.open();
    }

    public void addItem(AutoBuyItem item) {
        double y2 = y + 26;
        for (AutoBuyButton a : autoBuyButtons) {
            y2 += 20;
        }
        autoBuyButtons.add(new AutoBuyButton(x, y2, w, 18, item, item instanceof CustomAutoBuyItem ? ((CustomAutoBuyItem) item).name : item.item.getName().getString()));
    }

    public double calcH() {
        return autoBuyButtons.stream().filter(f -> f.name.toLowerCase().contains(search.toLowerCase())).toList().size() * 20 + 4;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        targetScroll = MathHelper.clamp(targetScroll, -(calcH() - (h - 24)), 0);
        if (calcH() - (h - 24) < 0) {
            targetScroll = 0;
        }
        scroll = Animation.fast(scroll, targetScroll);
        Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
        Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      //  RenderUtil.Render2D.drawRoundedRect(x, y, w, h, 4, new Color(39, 37, 37, 255));
        RenderUtil.Render2D.drawShadow((float) x, (float) y + 4, (float) (w), (float) h, 4,
                new Color(ColorUtil.setAlpha(color.getRGB(), 175)).getRGB(),
                new Color(ColorUtil.setAlpha(color2.getRGB(), 175)).getRGB(),
                new Color(ColorUtil.setAlpha(color2.getRGB(), 175)).getRGB(),
                new Color(ColorUtil.setAlpha(color.getRGB(), 175)).getRGB());

        RenderUtil.Render2D.drawRoundedRect((float) x, (float) y + 4, (float) (w), (float) h, 4, new Color(28, 28, 28, 200).getRGB());
        if (dot > 20) dot = 0;
        if (write) dot++;


        double y2 = y + 26 + scroll;
        ScissorUtils.enableScissor(x, y + 24 - 14, x + w, y + h);
        for (AutoBuyButton autoBuyButton : autoBuyButtons) {
            autoBuyButton.y = y2;
            y2 += 20;
            if (autoBuyButton.y < y || autoBuyButton.y > y + h) continue;
            autoBuyButton.render(mouseX, mouseY);
        }
        ScissorUtils.disableScissor();

        selectWindow.render(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        selectWindow.click(mouseX, mouseY, button);
        if (isHover(x, y + 24 - 14, w, h - 24, mouseX, mouseY)) {
            for (AutoBuyButton autoBuyButton : autoBuyButtons) {
                autoBuyButton.click((int) mouseX, (int) mouseY, button);
            }
        }
        if (isHover(x, y + 4 - 14, w, 20, mouseX, mouseY)) {
            PrintStack.setCallback(this.getClass(), () -> write = false);
            write = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        selectWindow.key(keyCode);
        for (AutoBuyButton autoBuyButton : autoBuyButtons) {
            autoBuyButton.key(keyCode);
        }
        if (write) {
            if (keyCode == GLFW.GLFW_KEY_ENTER) {
                write = false;
            }
            if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
                search = getStringIgnoreLastChar(search);
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public static String getStringIgnoreLastChar(String str) {
        StringBuilder s = new StringBuilder();

        for (int i = 0; i < str.toCharArray().length - 1; i++) {
            s.append(str.toCharArray()[i]);
        }

        return s.toString();
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        selectWindow.charTyped(codePoint);
        for (AutoBuyButton autoBuyButton : autoBuyButtons) {
            autoBuyButton.charTyped(codePoint);
        }
        if (write) {
            search += codePoint;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        selectWindow.scroll(mouseX, mouseY, delta);
        if (isHover(x, y + 24, w, h - 24, mouseX, mouseY) && (calcH() - (h - 24) > 0)) {
            if (delta < 0) {
                if (targetScroll > -(calcH() - (h - 24))) {
                    targetScroll -= 15;
                }
                targetScroll = MathHelper.clamp(targetScroll, -(calcH() - (h - 24)), 0);
            }
            if (delta > 0) {
                if (targetScroll < 0) {
                    targetScroll += 15;
                }
                targetScroll = MathHelper.clamp(targetScroll, -(calcH() - (h - 24)), 0);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void onClose() {
        AutoBuyManager.save();
        IgnoreManager.save();
    }

    public static boolean isHover(double X, double Y, double W, double H, double mX, double mY) {
        return mX >= X&& mX <= (X + W)  && mY >= Y  && mY <= (Y + H) ;
    }
}
