package sertyo.events.ui.csgui;


import com.mojang.blaze3d.matrix.MatrixStack;
import lombok.Data;
import net.minecraft.util.math.vector.Vector2f;
import sertyo.events.utility.math.MathUtility;
import sertyo.events.utility.render.RenderUtility;


import java.awt.*;

import static sertyo.events.utility.Utility.mc;

@Data
public class ScrollUtil {
    private double target, scroll, max;
    private double speed = 8F;
    private boolean enabled;
    private boolean autoReset;

    public ScrollUtil() {
        setEnabled(true);
        setAutoReset(true);
    }

    public void update() {
        if (enabled) {
            double wheel = mc.mouseHelper.getWheel() * (speed * 10F);
            double stretch = 0;
            target = Math.min(Math.max(target + wheel / 2, max - (wheel == 0 ? 0 : stretch)), (wheel == 0 ? 0 : stretch));
            resetWheel();
        }
        if (autoReset) resetWheel();
        scroll = lerp(scroll, target, 0.1F).floatValue();
    }


    public <T extends Number> T lerp(T input, T target, double step) {
        double start = input.doubleValue();
        double end = target.doubleValue();
        double result = start + step * (end - start);

        if (input instanceof Integer) {
            return (T) Integer.valueOf((int) Math.round(result));
        } else if (input instanceof Double) {
            return (T) Double.valueOf(result);
        } else if (input instanceof Float) {
            return (T) Float.valueOf((float) result);
        } else if (input instanceof Long) {
            return (T) Long.valueOf(Math.round(result));
        } else if (input instanceof Short) {
            return (T) Short.valueOf((short) Math.round(result));
        } else if (input instanceof Byte) {
            return (T) Byte.valueOf((byte) Math.round(result));
        } else {
            throw new IllegalArgumentException("Unsupported type: " + input.getClass().getSimpleName());
        }
    }


    private void resetWheel() {
        mc.mouseHelper.setWheel(0);
    }

    public void render(MatrixStack matrixStack, Vector2f position, float maxHeight) {
        render(matrixStack, position, maxHeight, 255);
    }

    public void render(MatrixStack matrixStack, Vector2f position, float maxHeight, float alpha) {
        double percentage = (getScroll() / getMax());
        double barHeight = maxHeight - ((getMax() / (getMax() - maxHeight)) * maxHeight);
        boolean allowed = (barHeight < maxHeight);
        if (!allowed) return;
        double scrollX = position.x;
        double scrollY = position.y + (maxHeight * percentage) - (barHeight * percentage);
        ScissorUtil.enable();
        ScissorUtil.scissor(mc.getMainWindow(), position.x, position.y, 0.5F, maxHeight);
        RenderUtility.Render2D.drawRect((float) scrollX, (float) scrollY, (float) scrollX + 0.5F, (float) scrollY + (float) barHeight, new Color(255, 255, 255, (int) MathUtility.clamp(0, 255, alpha)).hashCode());
        ScissorUtil.disable();
    }

    public void renderH(MatrixStack matrixStack, Vector2f position, float maxWidth) {
        renderH(matrixStack, position, maxWidth, 255);
    }

    public void renderH(MatrixStack matrixStack, Vector2f position, float maxWidth, float alpha) {
        double percentage = (getScroll() / getMax());
        double barWidth = maxWidth - ((getMax() / (getMax() - maxWidth)) * maxWidth);
        boolean allowed = (barWidth < maxWidth);
        if (!allowed) return;
        double scrollX = position.x + (maxWidth * percentage) - (barWidth * percentage);
        double scrollY = position.y;
        ScissorUtil.enable();
        ScissorUtil.scissor(mc.getMainWindow(), position.x, position.y, maxWidth, 0.5);
        RenderUtility.Render2D.drawRect( (float) scrollX, (float) scrollY, (float) (scrollX + barWidth), (float) (scrollY + 0.5), new Color(255, 255, 255, (int) MathUtility.clamp(0, 255, alpha)).hashCode());
        ScissorUtil.disable();
    }

    public void reset() {
        this.scroll = 0F;
        this.target = 0F;
    }

    public void setMax(float max, float height) {
        this.max = -max + height;
    }
}