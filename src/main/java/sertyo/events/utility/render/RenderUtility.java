package sertyo.events.utility.render;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import jhlabs.image.GaussianFilter;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.*;
import net.optifine.util.TextureUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import sertyo.events.Main;
import sertyo.events.manager.theme.Theme;
import sertyo.events.manager.theme.Themes;
import sertyo.events.utility.Utility;
import sertyo.events.utility.font.Fonts;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.mojang.blaze3d.platform.GlStateManager.GL_QUADS;
import static com.mojang.blaze3d.platform.GlStateManager.disableBlend;
import static com.mojang.blaze3d.platform.GlStateManager.*;
import static com.mojang.blaze3d.systems.RenderSystem.enableBlend;
import static net.minecraft.client.renderer.vertex.DefaultVertexFormats.*;
import static org.lwjgl.opengl.GL11.*;
import static sertyo.events.utility.render.ColorUtil.injectAlpha;
import static sertyo.events.utility.render.RenderUtil.IntColor.*;
import static sertyo.events.utility.render.RenderUtil.IntColor.*;
import static sertyo.events.utility.render.RenderUtility.Render2D.shadowCache;
import static sertyo.events.utility.render.ShaderUtil.CORNER_ROUND_SHADER_TEXTURE;
import static sertyo.events.utility.render.ShaderUtil.GRADIENT_MASK_SHADER;


public class RenderUtility implements Utility {
    private static final Tessellator TESSELLATOR = Tessellator.getInstance();
    private static final BufferBuilder BUILDER;
    private static final Shader ROUNDED;
    private static final Shader ROUNDED_GRADIENT;
    private static final Shader GRADIENT_MASK;
    private static final Shader BLUR;
    public static void scale(float f, float f2, double d, Runnable runnable) {
        GL11.glPushMatrix();
        GL11.glTranslatef(f, f2, 0.0f);
        GL11.glScaled(d, d, 1.0);
        GL11.glTranslatef(-f, -f2, 0.0f);
        runnable.run();
        GL11.glPopMatrix();
    }

    public static void scale(float f, float f2, float f3, float f4, float f5, Runnable runnable) {
        GL11.glPushMatrix();
        GL11.glTranslatef(f + f3 / 2.0f, f2 + f4 / 2.0f, 0.0f);
        GL11.glScalef(f5, f5, 1.0f);
        GL11.glTranslatef(-(f + f3 / 2.0f), -(f2 + f4 / 2.0f), 0.0f);
        runnable.run();
        GL11.glPopMatrix();
    }
    public static int reAlphaInt(final int color,
                                 final int alpha) {
        return (MathHelper.clamp(alpha, 0, 255) << 24) | (color & 16777215);
    }

    public static void color(final int rgb) {
        GL11.glColor3f(getRed(rgb) / 255f, getGreen(rgb) / 255f, getBlue(rgb) / 255f);
    }

    public static boolean isInRegion(final int mouseX,
                                     final int mouseY,
                                     final int x,
                                     final int y,
                                     final int width,
                                     final int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public static void drawBlur(Runnable data, float radius) {
        StencilUtility.initStencilToWrite();
        data.run();
        StencilUtility.readStencilBuffer(1);
        GaussianBlur.blur(radius, 1);
        StencilUtility.uninitStencilBuffer();
    }

    public static boolean isInRegion(final double mouseX,
                                     final double mouseY,
                                     final float x,
                                     final float y,
                                     final float width,
                                     final float height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public static boolean isInRegion(final double mouseX,
                                     final double mouseY,
                                     final int x,
                                     final int y,
                                     final int width,
                                     final int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public static void scaleEnd() {
        GL11.glPopMatrix();
    }
    public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }

    public static boolean isHovered(double mouseX, double mouseY, double x, double y, double width, double height) {
        return mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
    }


    public static void applyGradientMask(float x, float y, float width, float height, float alpha, Color bottomLeft, Color topLeft, Color bottomRight, Color topRight, Runnable content) {
        GlStateManager.color((int) 0.0F, (int) 0.0F, (int) 0.0F, (int) 0.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GRADIENT_MASK.useProgram();
        GRADIENT_MASK.setupUniform2f("location", x * 2.0F, (float)Minecraft.getInstance().getMainWindow().getHeight() - height * 2.0F - y * 2.0F);
        GRADIENT_MASK.setupUniform2f("rectSize", width * 2.0F, height * 2.0F);
        GRADIENT_MASK.setupUniform1f("alpha", alpha);
        GRADIENT_MASK.setupUniform3f("color1", (float)bottomLeft.getRed() / 255.0F, (float)bottomLeft.getGreen() / 255.0F, (float)bottomLeft.getBlue() / 255.0F);
        GRADIENT_MASK.setupUniform3f("color2", (float)topLeft.getRed() / 255.0F, (float)topLeft.getGreen() / 255.0F, (float)topLeft.getBlue() / 255.0F);
        GRADIENT_MASK.setupUniform3f("color3", (float)bottomRight.getRed() / 255.0F, (float)bottomRight.getGreen() / 255.0F, (float)bottomRight.getBlue() / 255.0F);
        GRADIENT_MASK.setupUniform3f("color4", (float)topRight.getRed() / 255.0F, (float)topRight.getGreen() / 255.0F, (float)topRight.getBlue() / 255.0F);
        content.run();
        GRADIENT_MASK.unloadProgram();
        GlStateManager.disableBlend();
    }

    public static class IntColor {

        public static float[] rgb(final int color) {
            return new float[]{
                    (color >> 16 & 0xFF) / 255f,
                    (color >> 8 & 0xFF) / 255f,
                    (color & 0xFF) / 255f,
                    (color >> 24 & 0xFF) / 255f
            };
        }

        public static int rgba(final int r,
                               final int g,
                               final int b,
                               final int a) {
            return a << 24 | r << 16 | g << 8 | b;
        }

        public static int getRed(final int hex) {
            return hex >> 16 & 255;
        }

        public static int getGreen(final int hex) {
            return hex >> 8 & 255;
        }

        public static int getBlue(final int hex) {
            return hex & 255;
        }

        public static int getAlpha(final int hex) {
            return hex >> 24 & 255;
        }
    }
    public static void drawGlow(float x, float y, float width, float height, int radius, int color) {

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL_GREATER, 0.01f);
        GlStateManager.disableAlphaTest();

        x -= radius;
        y -= radius;
        width = width + radius * 2;
        height = height + radius * 2;
        x -= 0.25f;
        y += 0.25f;

        int identifier = Objects.hash(width, height, radius);
        int textureId;

        if (shadowCache.containsKey(identifier)) {
            textureId = shadowCache.get(identifier);
            GlStateManager.bindTexture(textureId);
        } else {
            if (width <= 0) {
                width = 1;
            }

            if (height <= 0) {
                height = 1;
            }

            BufferedImage originalImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graphics = originalImage.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(radius, radius, (int) (width - radius * 2), (int) (height - radius * 2));
            graphics.dispose();

            GaussianFilter filter = new GaussianFilter(radius);
            BufferedImage blurredImage = filter.filter(originalImage, null);
            DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
            texture.setBlurMipmap(true, true);
            try {
                textureId = texture.getGlTextureId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            shadowCache.put(identifier, textureId);
        }

        float[] startColorComponents = rgb(color);

        buffer.begin(GL_QUADS, POSITION_COLOR_TEX);
        buffer.pos(x, y, 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                .tex(0.0f, 0.0f)
                .endVertex();

        buffer.pos(x, y + (float) ((int) height), 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                .tex(0.0f, 1.0f)
                .endVertex();

        buffer.pos(x + (float) ((int) width), y + (float) ((int) height), 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                .tex(1.0f, 1.0f)
                .endVertex();

        buffer.pos(x + (float) ((int) width), y, 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                .tex(1.0f, 0.0f)
                .endVertex();

        tessellator.draw();
        GlStateManager.enableAlphaTest();
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }

    public static void drawItemStack(ItemStack itemStack, int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableDepthTest();
        RenderHelper.enableStandardItemLighting();
        mc.getItemRenderer().renderItemAndEffectIntoGUI(itemStack, x, y);
        mc.getItemRenderer().renderItemOverlays(mc.fontRenderer, itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color((int) 1.0F, (int) 1.0F, (int) 1.0F, (int) 1.0F);
        GlStateManager.disableDepthTest();
    }
    public static void drawShadow(float x, float y, float width, float height, int radius, int startColor, int endColor) {


        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL_GREATER, 0.01f);

        x -= radius;
        y -= radius;
        width = width + radius * 2;
        height = height + radius * 2;
        x -= 0.25f;
        y += 0.25f;

        int identifier = Objects.hash(width, height, radius);
        int textureId;

        if (shadowCache.containsKey(identifier)) {
            textureId = shadowCache.get(identifier);
            GlStateManager.bindTexture(textureId);
        } else {
            if (width <= 0) {
                width = 1;
            }

            if (height <= 0) {
                height = 1;
            }

            BufferedImage originalImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graphics = originalImage.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(radius, radius, (int) (width - radius * 2), (int) (height - radius * 2));
            graphics.dispose();

            GaussianFilter filter = new GaussianFilter(radius);
            BufferedImage blurredImage = filter.filter(originalImage, null);
            DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
            texture.setBlurMipmap(true, true);
            try {
                textureId = texture.getGlTextureId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            shadowCache.put(identifier, textureId);
        }

        float[] startColorComponents = rgb(startColor);
        float[] endColorComponents = rgb(endColor);

        buffer.begin(GL_QUADS, POSITION_COLOR_TEX);
        buffer.pos(x, y, 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                .tex(0.0f, 0.0f)
                .endVertex();

        buffer.pos(x, y + (float) ((int) height), 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                .tex(0.0f, 1.0f)
                .endVertex();

        buffer.pos(x + (float) ((int) width), y + (float) ((int) height), 0.0f)
                .color(endColorComponents[0], endColorComponents[1], endColorComponents[2], endColorComponents[3])
                .tex(1.0f, 1.0f)
                .endVertex();

        buffer.pos(x + (float) ((int) width), y, 0.0f)
                .color(endColorComponents[0], endColorComponents[1], endColorComponents[2], endColorComponents[3])
                .tex(1.0f, 0.0f)
                .endVertex();

        tessellator.draw();

        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }
    public static void drawShadow(float x, float y, float width, float height, int radius, Color startColor, Color endColor) {


        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL_GREATER, 0.01f);

        x -= radius;
        y -= radius;
        width = width + radius * 2;
        height = height + radius * 2;
        x -= 0.25f;
        y += 0.25f;

        int identifier = Objects.hash(width, height, radius);
        int textureId;

        if (shadowCache.containsKey(identifier)) {
            textureId = shadowCache.get(identifier);
            GlStateManager.bindTexture(textureId);
        } else {
            if (width <= 0) {
                width = 1;
            }

            if (height <= 0) {
                height = 1;
            }

            BufferedImage originalImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graphics = originalImage.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(radius, radius, (int) (width - radius * 2), (int) (height - radius * 2));
            graphics.dispose();

            GaussianFilter filter = new GaussianFilter(radius);
            BufferedImage blurredImage = filter.filter(originalImage, null);
            DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
            texture.setBlurMipmap(true, true);
            try {
                textureId = texture.getGlTextureId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            shadowCache.put(identifier, textureId);
        }

        float[] startColorComponents = rgb(startColor.getRGB());
        float[] endColorComponents = rgb(endColor.getRGB());

        buffer.begin(GL_QUADS, POSITION_COLOR_TEX);
        buffer.pos(x, y, 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                .tex(0.0f, 0.0f)
                .endVertex();

        buffer.pos(x, y + (float) ((int) height), 0.0f)
                .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                .tex(0.0f, 1.0f)
                .endVertex();

        buffer.pos(x + (float) ((int) width), y + (float) ((int) height), 0.0f)
                .color(endColorComponents[0], endColorComponents[1], endColorComponents[2], endColorComponents[3])
                .tex(1.0f, 1.0f)
                .endVertex();

        buffer.pos(x + (float) ((int) width), y, 0.0f)
                .color(endColorComponents[0], endColorComponents[1], endColorComponents[2], endColorComponents[3])
                .tex(1.0f, 0.0f)
                .endVertex();

        tessellator.draw();

        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }

    public static void drawGradientGlow(float x, float y, float width, float height, int radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {


        GlStateManager.blendFuncSeparate(SourceFactor.SRC_ALPHA.param,
                DestFactor.ONE_MINUS_SRC_ALPHA.param, SourceFactor.ONE.param,
                DestFactor.ZERO.param);
        GlStateManager.shadeModel(7425);

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.alphaFunc(GL_GREATER, 0.01f);

        x -= radius;
        y -= radius;
        width = width + radius * 2;
        height = height + radius * 2;
        x -= 0.25f;
        y += 0.25f;

        int identifier = Objects.hash(width, height, radius);
        int textureId;

        if (shadowCache.containsKey(identifier)) {
            textureId = shadowCache.get(identifier);
            GlStateManager.bindTexture(textureId);
        } else {
            if (width <= 0) {
                width = 1;
            }

            if (height <= 0) {
                height = 1;
            }

            BufferedImage originalImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
            Graphics2D graphics = originalImage.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(radius, radius, (int) (width - radius * 2), (int) (height - radius * 2));
            graphics.dispose();

            GaussianFilter filter = new GaussianFilter(radius);
            BufferedImage blurredImage = filter.filter(originalImage, null);
            DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
            texture.setBlurMipmap(true, true);
            try {
                textureId = texture.getGlTextureId();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            shadowCache.put(identifier, textureId);
        }

        float[] bottomLefts = rgb(bottomLeft);
        float[] topLefts = rgb(topLeft);
        float[] bottomRights = rgb(bottomRight);
        float[] topRights = rgb(topRight);

        buffer.begin(GL_QUADS, POSITION_COLOR_TEX);
        buffer.pos(x, y, 0.0f)
                .color(bottomLefts[0], bottomLefts[1], bottomLefts[2], bottomLefts[3])
                .tex(0.0f, 0.0f)
                .endVertex();

        buffer.pos(x, y + (float) ((int) height), 0.0f)
                .color(topLefts[0], topLefts[1], topLefts[2], topLefts[3])
                .tex(0.0f, 1.0f)
                .endVertex();

        buffer.pos(x + (float) ((int) width), y + (float) ((int) height), 0.0f)
                .color(topRights[0], topRights[1], topRights[2], topRights[3])
                .tex(1.0f, 1.0f)
                .endVertex();

        buffer.pos(x + (float) ((int) width), y, 0.0f)
                .color(bottomRights[0], bottomRights[1], bottomRights[2], bottomRights[3])
                .tex(1.0f, 0.0f)
                .endVertex();

        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.bindTexture(0);
        GlStateManager.disableBlend();
    }
    public static void drawRoundedGradientRect(float x, float y, float x2, float y2, float round1, float round2, float round3, float round4, float value, int color1, int color2, int color3, int color4) {
        float[] c1 = ColorUtil.getRGBAf(color1);
        float[] c2 = ColorUtil.getRGBAf(color2);
        float[] c3 = ColorUtil.getRGBAf(color3);
        float[] c4 = ColorUtil.getRGBAf(color4);
        GlStateManager.color((int) 0.0F, (int) 0.0F, (int) 0.0F, (int) 0.0F);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ROUNDED_GRADIENT.useProgram();
        ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2.0F, y2 * 2.0F);
        ROUNDED_GRADIENT.setupUniform4f("round", round1, round2, round3, round4);
        ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.0F, 1.5F);
        ROUNDED_GRADIENT.setupUniform1f("value", value);
        ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
        ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
        ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
        ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
        allocTextureRectangle(x, y, x2, y2);
        ROUNDED_GRADIENT.unloadProgram();
        GlStateManager.disableBlend();
    }
    public static void allocTextureRectangle(float x, float y, float width, float height) {
        BUILDER.begin(7, DefaultVertexFormats.POSITION_TEX);
        BUILDER.pos((double)x, (double)y, 0.0).tex(0.0F, 0.0F).endVertex();
        BUILDER.pos((double)x, (double)(y + height), 0.0).tex((float) 0.0F, 1.0F).endVertex();
        BUILDER.pos((double)(x + width), (double)(y + height), 0.0).tex(1.0F, 1.0F).endVertex();
        BUILDER.pos((double)(x + width), (double)y, 0.0).tex((float) 1.0F, 0.0F).endVertex();
        TESSELLATOR.draw();
    }
    public static void drawRoundedRect(float x, float y, float x2, float y2, float round, int color) {
        drawRoundedGradientRect(x, y, x2, y2, round, 1.0F, color, color, color, color);
    }
    public static void drawRoundedRect(double x, double y, double x2, double y2, float round, Color color) {
        drawRoundedGradientRect((float) x, (float) y, (float) x2, (float) y2, round * 2, 1.0F, color.getRGB(), color.getRGB(), color.getRGB(), color.getRGB());
    }
    public static void drawRoundedGradientRect(float x, float y, float x2, float y2, float round, float value, int color1, int color2, int color3, int color4) {
        float[] c1 = ColorUtil.getRGBAf(color1);
        float[] c2 = ColorUtil.getRGBAf(color2);
        float[] c3 = ColorUtil.getRGBAf(color3);
        float[] c4 = ColorUtil.getRGBAf(color4);
        GlStateManager.color((int) 0.0F, (int) 0.0F, (int) 0.0F, (int) 0.0F);
        GlStateManager.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ROUNDED_GRADIENT.useProgram();
        ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2.0F, y2 * 2.0F);
        ROUNDED_GRADIENT.setupUniform4f("round", round, round, round, round);
        ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.0F, 1.5F);
        ROUNDED_GRADIENT.setupUniform1f("value", value);
        ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
        ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
        ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
        ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
        allocTextureRectangle(x, y, x2, y2);
        ROUNDED_GRADIENT.unloadProgram();
        GlStateManager.disableBlend();
    }
    public static void drawRoundedRect(float x, float y, float x2, float y2, float round, float swapX, float swapY, int firstColor, int secondColor) {
        float[] c = ColorUtil.getRGBAf(firstColor);
        float[] c1 = ColorUtil.getRGBAf(secondColor);
        GlStateManager.color((int) 0.0F, (int) 0.0F, (int) 0.0F, (int) 0.0F);
        GlStateManager.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        ROUNDED.useProgram();
        ROUNDED.setupUniform2f("size", (x2 - round) * 2.0F, (y2 - round) * 2.0F);
        ROUNDED.setupUniform1f("round", round);
        ROUNDED.setupUniform2f("smoothness", 0.0F, 1.5F);
        ROUNDED.setupUniform2f("swap", swapX, swapY);
        ROUNDED.setupUniform4f("firstColor", c[0], c[1], c[2], c[3]);
        ROUNDED.setupUniform4f("secondColor", c1[0], c1[1], c1[2], c[3]);
        allocTextureRectangle(x, y, x2, y2);
        ROUNDED.unloadProgram();
        GlStateManager.disableBlend();
    }
    public static class Render2D {
        public static void drawRectNoWH(double left, double top, double right, double bottom, int color) {
            double j;
            if (left < right) {
                j = left;
                left = right;
                right = j;
            }

            if (top < bottom) {
                j = top;
                top = bottom;
                bottom = j;
            }

            float f3 = (float)(color >> 24 & 255) / 255.0F;
            float f = (float)(color >> 16 & 255) / 255.0F;
            float f1 = (float)(color >> 8 & 255) / 255.0F;
            float f2 = (float)(color & 255) / 255.0F;
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            GlStateManager.enableBlend();
            GlStateManager.disableTexture();
            //      GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
            GlStateManager.color((int) f, (int) f1, (int) f2, (int) f3);
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
            bufferbuilder.pos(left, bottom, 0.0).endVertex();
            bufferbuilder.pos(right, bottom, 0.0).endVertex();
            bufferbuilder.pos(right, top, 0.0).endVertex();
            bufferbuilder.pos(left, top, 0.0).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture();
            GlStateManager.disableBlend();
        }
        public static void drawRoundedGradientRect(float x, float y, float x2, float y2, float round1, float round2, float round3, float round4, float value, int color1, int color2, int color3, int color4) {
            float[] c1 = ColorUtil.getRGBAf(color1);
            float[] c2 = ColorUtil.getRGBAf(color2);
            float[] c3 = ColorUtil.getRGBAf(color3);
            float[] c4 = ColorUtil.getRGBAf(color4);
            GlStateManager.color((int) 0.0F, (int) 0.0F, (int) 0.0F, (int) 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            ROUNDED_GRADIENT.useProgram();
            ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2.0F, y2 * 2.0F);
            ROUNDED_GRADIENT.setupUniform4f("round", round1, round2, round3, round4);
            ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.0F, 1.5F);
            ROUNDED_GRADIENT.setupUniform1f("value", value);
            ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
            ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
            ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
            ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
            allocTextureRectangle(x, y, x2, y2);
            ROUNDED_GRADIENT.unloadProgram();
            GlStateManager.disableBlend();
        }
        public static void drawCRoundedRect(float x, float y, float x2, float y2, float round1, float round2, float round3, float round4, int color) {
            drawRoundedGradientRect(x, y, x2, y2, round1, round2, round3, round4, 1.0F, color, color, color, color);
        }
        public static double[] worldToScreen(double x, double y, double z) {
            if (Minecraft.getInstance().currentScreen != null && !(Minecraft.getInstance().currentScreen instanceof ChatScreen) && (Minecraft.getInstance().currentScreen == null)) {
                return null;
            } else {
                try {
                    Vector3d camera_pos = Minecraft.getInstance().getRenderManager().info.getProjectedView();
                    Quaternion camera_rotation_conj = Minecraft.getInstance().getRenderManager().getCameraOrientation().copy();
                    camera_rotation_conj.conjugate();
                    Vector3f result3f = new Vector3f((float) (camera_pos.x - x), (float) (camera_pos.y - y), (float) (camera_pos.z - z));
                    result3f.transform(camera_rotation_conj);
                    GameRenderer gameRenderer = Minecraft.getInstance().gameRenderer;
                    Method method = null;
                    Method[] var11 = GameRenderer.class.getDeclaredMethods();
                    int var12 = var11.length;

                    for (int var13 = 0; var13 < var12; ++var13) {
                        Method m = var11[var13];
                        if (m.getParameterCount() == 3 && m.getParameterTypes()[2] == Boolean.TYPE && m.getParameterTypes()[1] == Float.TYPE && m.getParameterTypes()[0] == ActiveRenderInfo.class && m.getReturnType() == Double.TYPE) {
                            method = m;
                        }
                    }

                    method.setAccessible(true);
                    float fov = ((Double) method.invoke(gameRenderer, Minecraft.getInstance().getRenderManager().info, 1, true)).floatValue();
                    float half_height = (float) Minecraft.getInstance().getMainWindow().getScaledHeight() / 2.0F;
                    float scale_factor = half_height / (result3f.getZ() * (float) Math.tan(Math.toRadians((double) (fov / 2.0F))));
                    if (result3f.getZ() < 0.0F) {
                        return new double[]{(double) (-result3f.getX() * scale_factor + (float) (Minecraft.getInstance().getMainWindow().getScaledWidth() / 2)), (double) ((float) (Minecraft.getInstance().getMainWindow().getScaledHeight() / 2) - result3f.getY() * scale_factor)};
                    }
                } catch (Exception var15) {
                    var15.printStackTrace();
                }

                return null;
            }
        }

        public static final HashMap<Integer, Integer> shadowCache = new HashMap<>();

        public static void drawTriangle(float x, float y, float width, float height, Color color) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            enableSmoothLine(1);
            GL11.glRotatef(180 + 90, 0F, 0F, 1.0F);

            // fill.
            GL11.glBegin(9);
            ColorUtil.setColor(color.getRGB());
            GL11.glVertex2f(x, y - 2);
            GL11.glVertex2f(x + width, y + height);
            GL11.glVertex2f(x + width, y);
            GL11.glVertex2f(x, y - 2);
            GL11.glEnd();

            GL11.glBegin(9);
            ColorUtil.setColor(color.brighter().getRGB());
            GL11.glVertex2f(x + width, y);
            GL11.glVertex2f(x + width, y + height);
            GL11.glVertex2f(x + width * 2, y - 2);
            GL11.glVertex2f(x + width, y);
            GL11.glEnd();

            // line.
            GL11.glBegin(3);
            ColorUtil.setColor(color.getRGB());
            GL11.glVertex2f(x, y - 2);
            GL11.glVertex2f(x + width, y + height);
            GL11.glVertex2f(x + width, y);
            GL11.glVertex2f(x, y - 2);
            GL11.glEnd();

            GL11.glBegin(3);
            ColorUtil.setColor(color.brighter().getRGB());
            GL11.glVertex2f(x + width, y);
            GL11.glVertex2f(x + width, y + height);
            GL11.glVertex2f(x + width * 2, y - 2);
            GL11.glVertex2f(x + width, y);
            GL11.glEnd();

            disableSmoothLine();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glRotatef(-180 - 90, 0F, 0F, 1.0F);
            GL11.glPopMatrix();
        }

        public static void drawTriangle(float x, float y, float width, float height, float vector, int color) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            enableSmoothLine(1);
            GL11.glRotatef(180 + vector, 0F, 0F, 1.0F);

            // fill.
            GL11.glBegin(9);
            ColorUtil.setColor(color);
            GL11.glVertex2f(x, y - 2);
            GL11.glVertex2f(x + width, y + height);
            GL11.glVertex2f(x + width, y);
            GL11.glVertex2f(x, y - 2);
            GL11.glEnd();

            disableSmoothLine();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glRotatef(-180 - vector, 0F, 0F, 1.0F);
            GL11.glPopMatrix();
        }

        public static void enableSmoothLine(float width) {
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(false);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            GL11.glLineWidth(width);
        }

        public static void disableSmoothLine() {
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glDepthMask(true);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        }

        public static int downloadImage(String url) {
            int texId = -1;
            int identifier = Objects.hash(url);
            if (shadowCache2.containsKey(identifier)) {
                texId = shadowCache2.get(identifier);
            } else {
                URL stringURL = null;
                try {
                    stringURL = new URL(url);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                BufferedImage img = null;
                try {
                    img = ImageIO.read(stringURL);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                try {
                    texId = loadTexture(img);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                shadowCache2.put(identifier, texId);
            }
            return texId;
        }


        private static HashMap<Integer, Integer> shadowCache2 = new HashMap<Integer, Integer>();
        public static void wexrender(float x, float y, float width, float height, float radius, float razdelenie, int color, int color2, int color3, int color4, int color5) {
            float[] colorComponents = ColorUtil.penis2(color);
            /*float adjustedThickness = thickness;
            drawGradientRound(x1, y1, x2 + adjustedThickness, y2 + adjustedThickness, depth, style, alpha, texture, blendMode);
            if (colorComponents[3] != 0.0F) {
                drawRoundedRect(x1, y1, x2, y2, depth, color);
            }*/
            float var13 = razdelenie - 1.0F;
            drawRoundedGradientRect(x - var13, y - var13, width + var13, height + var13, radius * 2, razdelenie, color2, color3, color4, color5);
            if (colorComponents[3] != 0.0F) {
                drawRoundedRect(x, y, width, height, radius * 2, color);
            }
        }

        public static void drawShadow(float x, float y, float width, float height, int radius, int color) {

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(GL_GREATER, 0.01f);
            GlStateManager.disableAlphaTest();

            x -= radius;
            y -= radius;
            width = width + radius * 2;
            height = height + radius * 2;
            x -= 0.25f;
            y += 0.25f;

            int identifier = Objects.hash(width, height, radius);
            int textureId;

            if (shadowCache.containsKey(identifier)) {
                textureId = shadowCache.get(identifier);
                GlStateManager.bindTexture(textureId);
            } else {
                if (width <= 0) {
                    width = 1;
                }

                if (height <= 0) {
                    height = 1;
                }

                BufferedImage originalImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D graphics = originalImage.createGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(radius, radius, (int) (width - radius * 2), (int) (height - radius * 2));
                graphics.dispose();

                GaussianFilter filter = new GaussianFilter(radius);
                BufferedImage blurredImage = filter.filter(originalImage, null);
                DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
                texture.setBlurMipmap(true, true);
                try {
                    textureId = texture.getGlTextureId();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                shadowCache.put(identifier, textureId);
            }

            float[] startColorComponents = rgb(color);

            buffer.begin(GL_QUADS, POSITION_COLOR_TEX);
            buffer.pos(x, y, 0.0f)
                    .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                    .tex(0.0f, 0.0f)
                    .endVertex();

            buffer.pos(x, y + (float) ((int) height), 0.0f)
                    .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                    .tex(0.0f, 1.0f)
                    .endVertex();

            buffer.pos(x + (float) ((int) width), y + (float) ((int) height), 0.0f)
                    .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                    .tex(1.0f, 1.0f)
                    .endVertex();

            buffer.pos(x + (float) ((int) width), y, 0.0f)
                    .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                    .tex(1.0f, 0.0f)
                    .endVertex();

            tessellator.draw();
            GlStateManager.enableAlphaTest();
            GlStateManager.bindTexture(0);
            GlStateManager.disableBlend();
        }


        public static void drawShadow(float x, float y, float width, float height, int radius, int startColor, int endColor) {


            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(GL_GREATER, 0.01f);

            x -= radius;
            y -= radius;
            width = width + radius * 2;
            height = height + radius * 2;
            x -= 0.25f;
            y += 0.25f;

            int identifier = Objects.hash(width, height, radius);
            int textureId;

            if (shadowCache.containsKey(identifier)) {
                textureId = shadowCache.get(identifier);
                GlStateManager.bindTexture(textureId);
            } else {
                if (width <= 0) {
                    width = 1;
                }

                if (height <= 0) {
                    height = 1;
                }

                BufferedImage originalImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D graphics = originalImage.createGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(radius, radius, (int) (width - radius * 2), (int) (height - radius * 2));
                graphics.dispose();

                GaussianFilter filter = new GaussianFilter(radius);
                BufferedImage blurredImage = filter.filter(originalImage, null);
                DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
                texture.setBlurMipmap(true, true);
                try {
                    textureId = texture.getGlTextureId();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                shadowCache.put(identifier, textureId);
            }

            float[] startColorComponents = rgb(startColor);
            float[] endColorComponents = rgb(endColor);

            buffer.begin(GL_QUADS, POSITION_COLOR_TEX);
            buffer.pos(x, y, 0.0f)
                    .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                    .tex(0.0f, 0.0f)
                    .endVertex();

            buffer.pos(x, y + (float) ((int) height), 0.0f)
                    .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                    .tex(0.0f, 1.0f)
                    .endVertex();

            buffer.pos(x + (float) ((int) width), y + (float) ((int) height), 0.0f)
                    .color(endColorComponents[0], endColorComponents[1], endColorComponents[2], endColorComponents[3])
                    .tex(1.0f, 1.0f)
                    .endVertex();

            buffer.pos(x + (float) ((int) width), y, 0.0f)
                    .color(endColorComponents[0], endColorComponents[1], endColorComponents[2], endColorComponents[3])
                    .tex(1.0f, 0.0f)
                    .endVertex();

            tessellator.draw();

            GlStateManager.bindTexture(0);
            GlStateManager.disableBlend();
        }
        public static void drawShadow(float x, float y, float width, float height, int radius, Color startColor, Color endColor) {


            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(GL_GREATER, 0.01f);

            x -= radius;
            y -= radius;
            width = width + radius * 2;
            height = height + radius * 2;
            x -= 0.25f;
            y += 0.25f;

            int identifier = Objects.hash(width, height, radius);
            int textureId;

            if (shadowCache.containsKey(identifier)) {
                textureId = shadowCache.get(identifier);
                GlStateManager.bindTexture(textureId);
            } else {
                if (width <= 0) {
                    width = 1;
                }

                if (height <= 0) {
                    height = 1;
                }

                BufferedImage originalImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D graphics = originalImage.createGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(radius, radius, (int) (width - radius * 2), (int) (height - radius * 2));
                graphics.dispose();

                GaussianFilter filter = new GaussianFilter(radius);
                BufferedImage blurredImage = filter.filter(originalImage, null);
                DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
                texture.setBlurMipmap(true, true);
                try {
                    textureId = texture.getGlTextureId();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                shadowCache.put(identifier, textureId);
            }

            float[] startColorComponents = rgb(startColor.getRGB());
            float[] endColorComponents = rgb(endColor.getRGB());

            buffer.begin(GL_QUADS, POSITION_COLOR_TEX);
            buffer.pos(x, y, 0.0f)
                    .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                    .tex(0.0f, 0.0f)
                    .endVertex();

            buffer.pos(x, y + (float) ((int) height), 0.0f)
                    .color(startColorComponents[0], startColorComponents[1], startColorComponents[2], startColorComponents[3])
                    .tex(0.0f, 1.0f)
                    .endVertex();

            buffer.pos(x + (float) ((int) width), y + (float) ((int) height), 0.0f)
                    .color(endColorComponents[0], endColorComponents[1], endColorComponents[2], endColorComponents[3])
                    .tex(1.0f, 1.0f)
                    .endVertex();

            buffer.pos(x + (float) ((int) width), y, 0.0f)
                    .color(endColorComponents[0], endColorComponents[1], endColorComponents[2], endColorComponents[3])
                    .tex(1.0f, 0.0f)
                    .endVertex();

            tessellator.draw();

            GlStateManager.bindTexture(0);
            GlStateManager.disableBlend();
        }

        public static void drawShadow(float x, float y, float width, float height, int radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {


            GlStateManager.blendFuncSeparate(SourceFactor.SRC_ALPHA.param,
                    DestFactor.ONE_MINUS_SRC_ALPHA.param, SourceFactor.ONE.param,
                    DestFactor.ZERO.param);
            GlStateManager.shadeModel(7425);

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            GlStateManager.alphaFunc(GL_GREATER, 0.01f);

            x -= radius;
            y -= radius;
            width = width + radius * 2;
            height = height + radius * 2;
            x -= 0.25f;
            y += 0.25f;

            int identifier = Objects.hash(width, height, radius);
            int textureId;

            if (shadowCache.containsKey(identifier)) {
                textureId = shadowCache.get(identifier);
                GlStateManager.bindTexture(textureId);
            } else {
                if (width <= 0) {
                    width = 1;
                }

                if (height <= 0) {
                    height = 1;
                }

                BufferedImage originalImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_ARGB_PRE);
                Graphics2D graphics = originalImage.createGraphics();
                graphics.setColor(Color.WHITE);
                graphics.fillRect(radius, radius, (int) (width - radius * 2), (int) (height - radius * 2));
                graphics.dispose();

                GaussianFilter filter = new GaussianFilter(radius);
                BufferedImage blurredImage = filter.filter(originalImage, null);
                DynamicTexture texture = new DynamicTexture(TextureUtils.toNativeImage(blurredImage));
                texture.setBlurMipmap(true, true);
                try {
                    textureId = texture.getGlTextureId();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                shadowCache.put(identifier, textureId);
            }

            float[] bottomLefts = rgb(bottomLeft);
            float[] topLefts = rgb(topLeft);
            float[] bottomRights = rgb(bottomRight);
            float[] topRights = rgb(topRight);

            buffer.begin(GL_QUADS, POSITION_COLOR_TEX);
            buffer.pos(x, y, 0.0f)
                    .color(bottomLefts[0], bottomLefts[1], bottomLefts[2], bottomLefts[3])
                    .tex(0.0f, 0.0f)
                    .endVertex();

            buffer.pos(x, y + (float) ((int) height), 0.0f)
                    .color(topLefts[0], topLefts[1], topLefts[2], topLefts[3])
                    .tex(0.0f, 1.0f)
                    .endVertex();

            buffer.pos(x + (float) ((int) width), y + (float) ((int) height), 0.0f)
                    .color(topRights[0], topRights[1], topRights[2], topRights[3])
                    .tex(1.0f, 1.0f)
                    .endVertex();

            buffer.pos(x + (float) ((int) width), y, 0.0f)
                    .color(bottomRights[0], bottomRights[1], bottomRights[2], bottomRights[3])
                    .tex(1.0f, 0.0f)
                    .endVertex();

            tessellator.draw();
            GlStateManager.shadeModel(7424);
            GlStateManager.bindTexture(0);
            GlStateManager.disableBlend();
        }



        public static void drawCircle(float x, float y, float start, float end, float radius, float width, boolean filled, int color) {

            float i;
            float endOffset;
            if (start > end) {
                endOffset = end;
                end = start;
                start = endOffset;
            }

            GlStateManager.enableBlend();
            GL11.glDisable(GL_TEXTURE_2D);
            RenderSystem.blendFuncSeparate(770, 771, 1, 0);

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glLineWidth(width);
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (i = end; i >= start; i--) {
                ColorUtil.setColor(color);
                float cos = (float) (MathHelper.cos((float) (i * Math.PI / 180)) * radius);
                float sin = (float) (MathHelper.sin((float) (i * Math.PI / 180)) * radius);
                GL11.glVertex2f(x + cos, y + sin);
            }
            GL11.glEnd();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);

            if (filled) {
                GL11.glBegin(GL11.GL_TRIANGLE_FAN);
                for (i = end; i >= start; i--) {
                    ColorUtil.setColor(color);
                    float cos = (float) MathHelper.cos((float) (i * Math.PI / 180)) * radius;
                    float sin = (float) MathHelper.sin((float) (i * Math.PI / 180)) * radius;
                    GL11.glVertex2f(x + cos, y + sin);
                }
                GL11.glEnd();
            }

            GL11.glEnable(GL_TEXTURE_2D);
            GlStateManager.disableBlend();
        }

        public static int loadTexture(BufferedImage image) throws Exception {
            int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
            ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);

            for (int pixel : pixels) {
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF));
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
            buffer.flip();

            int textureID = GlStateManager.genTexture();
            GlStateManager.bindTexture(textureID);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_CLAMP_TO_EDGE);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_CLAMP_TO_EDGE);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
            GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
            GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
            GlStateManager.bindTexture(0);
            return textureID;
        }

        public static void drawFace(float d,
                                    float y,
                                    float u,
                                    float v,
                                    float uWidth,
                                    float vHeight,
                                    float width,
                                    float height,
                                    float tileWidth,
                                    float tileHeight,
                                    AbstractClientPlayerEntity target) {
            try {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                ResourceLocation skin = target.getLocationSkin();
                mc.getTextureManager().bindTexture(skin);
                float hurtPercent = getHurtPercent(target);
                GL11.glColor4f(1, 1 - hurtPercent, 1 - hurtPercent, 1);
                AbstractGui.drawScaledCustomSizeModalRect(d, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
                GL11.glColor4f(1, 1, 1, 1);
                GL11.glPopMatrix();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void drawFace(float x, float y, float width, float height) {
            try {
                AbstractGui.drawScaledCustomSizeModalRect(x, y, 8, 8, 8, 8, width, height, 64, 64);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static float getRenderHurtTime(LivingEntity hurt) {
            return (float) hurt.hurtTime - (hurt.hurtTime != 0 ? mc.timer.renderPartialTicks : 0.0f);
        }

        public static float getHurtPercent(LivingEntity hurt) {
            return getRenderHurtTime(hurt) / (float) 10;
        }

        public static void drawRect(float x,
                                    float y,
                                    float width,
                                    float height,
                                    int color) {

            drawMcRect(x, y, x + width, y + height, color);
        }

        public static void drawRoundCircle(float x,
                                           float y,
                                           float radius,
                                           int color) {
        }

        public static void drawRoundCircle(float x,
                                           float y,
                                           float radius,
                                           int bottomLeft, int topLeft, int bottomRight, int topRight) {
            drawGradientRound(x - (radius / 2), y - (radius / 2), radius, radius, (radius / 2), bottomLeft, topLeft, bottomRight, topRight);
        }

        public static void drawMcRect(double left,
                                      double top,
                                      double right,
                                      double bottom,
                                      int color) {
            if (left < right) {
                double i = left;
                left = right;
                right = i;
            }

            if (top < bottom) {
                double j = top;
                top = bottom;
                bottom = j;
            }

            float f3 = (float) (color >> 24 & 255) / 255.0F;
            float f = (float) (color >> 16 & 255) / 255.0F;
            float f1 = (float) (color >> 8 & 255) / 255.0F;
            float f2 = (float) (color & 255) / 255.0F;
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
            RenderSystem.enableBlend();
            RenderSystem.disableTexture();
            RenderSystem.defaultBlendFunc();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(left, bottom, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.pos(right, bottom, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.pos(right, top, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.pos(left, top, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
        }

        public static void drawLine(double x,
                                    double y,
                                    double z,
                                    double w,
                                    int color) {


            float f3 = (float) (color >> 24 & 255) / 255.0F;
            float f = (float) (color >> 16 & 255) / 255.0F;
            float f1 = (float) (color >> 8 & 255) / 255.0F;
            float f2 = (float) (color & 255) / 255.0F;
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
            RenderSystem.enableBlend();
            RenderSystem.disableTexture();
            RenderSystem.defaultBlendFunc();
            GL11.glEnable(GL_LINE_SMOOTH);
            RenderSystem.lineWidth(1.5f);
            bufferbuilder.begin(GL_LINES, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(x, y, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.pos(z, w, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.finishDrawing();
            WorldVertexBufferUploader.draw(bufferbuilder);
            GL11.glDisable(GL_LINE_SMOOTH);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
        }


        public static void drawMcRectBuilding(double left,
                                              double top,
                                              double right,
                                              double bottom,
                                              int color) {
            if (left < right) {
                double i = left;
                left = right;
                right = i;
            }

            if (top < bottom) {
                double j = top;
                top = bottom;
                bottom = j;
            }

            float f3 = (float) (color >> 24 & 255) / 255.0F;
            float f = (float) (color >> 16 & 255) / 255.0F;
            float f1 = (float) (color >> 8 & 255) / 255.0F;
            float f2 = (float) (color & 255) / 255.0F;
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
            //bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(left, bottom, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.pos(right, bottom, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.pos(right, top, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.pos(left, top, 0.0F).color(f, f1, f2, f3).endVertex();
            //bufferbuilder.finishDrawing();
            //WorldVertexBufferUploader.draw(bufferbuilder);
        }

        public static void drawRectBuilding(double left,
                                            double top,
                                            double right,
                                            double bottom,
                                            int color) {
            right += left;
            bottom += top;

            if (left < right) {
                double i = left;
                left = right;
                right = i;
            }

            if (top < bottom) {
                double j = top;
                top = bottom;
                bottom = j;
            }

            float f3 = (float) (color >> 24 & 255) / 255.0F;
            float f = (float) (color >> 16 & 255) / 255.0F;
            float f1 = (float) (color >> 8 & 255) / 255.0F;
            float f2 = (float) (color & 255) / 255.0F;
            BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
            //bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(left, bottom, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.pos(right, bottom, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.pos(right, top, 0.0F).color(f, f1, f2, f3).endVertex();
            bufferbuilder.pos(left, top, 0.0F).color(f, f1, f2, f3).endVertex();
            //bufferbuilder.finishDrawing();
            //WorldVertexBufferUploader.draw(bufferbuilder);
        }


        public static void drawRectOutlineBuilding(double x, double y, double width, double height, double size, int color) {
            drawMcRectBuilding(x + size, y, width - size, y + size, color);
            drawMcRectBuilding(x, y, x + size, height, color);
            drawMcRectBuilding(width - size, y, width, height, color);
            drawMcRectBuilding(x + size, height - size, width - size, height, color);
        }



        public static void drawMCHorizontal(double x,
                                            double y,
                                            double width,
                                            double height,
                                            int start,
                                            int end) {


            float f = (float) (start >> 24 & 255) / 255.0F;
            float f1 = (float) (start >> 16 & 255) / 255.0F;
            float f2 = (float) (start >> 8 & 255) / 255.0F;
            float f3 = (float) (start & 255) / 255.0F;
            float f4 = (float) (end >> 24 & 255) / 255.0F;
            float f5 = (float) (end >> 16 & 255) / 255.0F;
            float f6 = (float) (end >> 8 & 255) / 255.0F;
            float f7 = (float) (end & 255) / 255.0F;

            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.disableAlphaTest();
            RenderSystem.defaultBlendFunc();
            RenderSystem.shadeModel(7425);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

            bufferbuilder.pos(x, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, height, 0f).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(width, y, 0f).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(x, y, 0f).color(f1, f2, f3, f).endVertex();

            tessellator.draw();
            RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableTexture();
        }

        public static void drawMCHorizontalBuilding(double x,
                                                    double y,
                                                    double width,
                                                    double height,
                                                    int start,
                                                    int end) {


            float f = (float) (start >> 24 & 255) / 255.0F;
            float f1 = (float) (start >> 16 & 255) / 255.0F;
            float f2 = (float) (start >> 8 & 255) / 255.0F;
            float f3 = (float) (start & 255) / 255.0F;
            float f4 = (float) (end >> 24 & 255) / 255.0F;
            float f5 = (float) (end >> 16 & 255) / 255.0F;
            float f6 = (float) (end >> 8 & 255) / 255.0F;
            float f7 = (float) (end & 255) / 255.0F;


            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            //bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

            bufferbuilder.pos(x, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, height, 0f).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(width, y, 0f).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(x, y, 0f).color(f1, f2, f3, f).endVertex();

            //tessellator.draw();

        }


        public static void drawHorizontal(float x,
                                          float y,
                                          float width,
                                          float height,
                                          int start,
                                          int end) {

            width += x;
            height += y;

            float f = (float) (start >> 24 & 255) / 255.0F;
            float f1 = (float) (start >> 16 & 255) / 255.0F;
            float f2 = (float) (start >> 8 & 255) / 255.0F;
            float f3 = (float) (start & 255) / 255.0F;
            float f4 = (float) (end >> 24 & 255) / 255.0F;
            float f5 = (float) (end >> 16 & 255) / 255.0F;
            float f6 = (float) (end >> 8 & 255) / 255.0F;
            float f7 = (float) (end & 255) / 255.0F;

            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.disableAlphaTest();
            RenderSystem.defaultBlendFunc();
            RenderSystem.shadeModel(7425);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

            bufferbuilder.pos(x, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, height, 0f).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(width, y, 0f).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(x, y, 0f).color(f1, f2, f3, f).endVertex();

            tessellator.draw();
            RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableTexture();
        }

        public static void applyGradient(float x, float y, float width, float height, int bottomLeft, int topLeft, int bottomRight, int topRight, Runnable content) {
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            GRADIENT_MASK_SHADER.attach();
            GRADIENT_MASK_SHADER.setUniform("location", (float) (x * 2), (float) ((sr.getHeight() - (height * 2)) - (y * 2)));
            GRADIENT_MASK_SHADER.setUniform("rectSize", (float) (width * 2), (float) (height * 2));
            GRADIENT_MASK_SHADER.setUniform("tex", 2);
            GRADIENT_MASK_SHADER.setUniform("alpha", 1f);

            float[] bottoml = IntColor.rgb(bottomLeft);
            float[] topl = IntColor.rgb(topLeft);
            float[] bottomr = IntColor.rgb(bottomRight);
            float[] topr = IntColor.rgb(topRight);

            // Bottom Left
            GRADIENT_MASK_SHADER.setUniform("color1", bottoml[0], bottoml[1], bottoml[2]);
            //Top left
            GRADIENT_MASK_SHADER.setUniform("color2", topl[0], topl[1], topl[2]);
            //Bottom Right
            GRADIENT_MASK_SHADER.setUniform("color3", bottomr[0], bottomr[1], bottomr[2]);
            //Top Right
            GRADIENT_MASK_SHADER.setUniform("color4", topr[0], topr[1], topr[2]);

            //Apply the gradient to whatever is put here
            content.run();

            GRADIENT_MASK_SHADER.detach();
            GlStateManager.disableBlend();
        }

        public static void drawVertical(float x,
                                        float y,
                                        float width,
                                        float height,
                                        int start,
                                        int end) {
            width += x;
            height += y;

            float f = (float) (start >> 24 & 255) / 255.0F;
            float f1 = (float) (start >> 16 & 255) / 255.0F;
            float f2 = (float) (start >> 8 & 255) / 255.0F;
            float f3 = (float) (start & 255) / 255.0F;
            float f4 = (float) (end >> 24 & 255) / 255.0F;
            float f5 = (float) (end >> 16 & 255) / 255.0F;
            float f6 = (float) (end >> 8 & 255) / 255.0F;
            float f7 = (float) (end & 255) / 255.0F;

            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.disableAlphaTest();
            RenderSystem.defaultBlendFunc();
            RenderSystem.shadeModel(7425);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

            bufferbuilder.pos(x, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, y, 0f).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(x, y, 0f).color(f5, f6, f7, f4).endVertex();

            tessellator.draw();
            RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableTexture();

        }

        public static void drawMCVertical(double x,
                                          double y,
                                          double width,
                                          double height,
                                          int start,
                                          int end) {

            float f = (float) (start >> 24 & 255) / 255.0F;
            float f1 = (float) (start >> 16 & 255) / 255.0F;
            float f2 = (float) (start >> 8 & 255) / 255.0F;
            float f3 = (float) (start & 255) / 255.0F;
            float f4 = (float) (end >> 24 & 255) / 255.0F;
            float f5 = (float) (end >> 16 & 255) / 255.0F;
            float f6 = (float) (end >> 8 & 255) / 255.0F;
            float f7 = (float) (end & 255) / 255.0F;

            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.disableAlphaTest();
            RenderSystem.defaultBlendFunc();
            RenderSystem.shadeModel(7425);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

            bufferbuilder.pos(x, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, y, 0f).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(x, y, 0f).color(f5, f6, f7, f4).endVertex();

            tessellator.draw();
            RenderSystem.shadeModel(7424);
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableTexture();

        }

        public static void drawMCVerticalBuilding(double x,
                                                  double y,
                                                  double width,
                                                  double height,
                                                  int start,
                                                  int end) {

            float f = (float) (start >> 24 & 255) / 255.0F;
            float f1 = (float) (start >> 16 & 255) / 255.0F;
            float f2 = (float) (start >> 8 & 255) / 255.0F;
            float f3 = (float) (start & 255) / 255.0F;
            float f4 = (float) (end >> 24 & 255) / 255.0F;
            float f5 = (float) (end >> 16 & 255) / 255.0F;
            float f6 = (float) (end >> 8 & 255) / 255.0F;
            float f7 = (float) (end & 255) / 255.0F;

//            RenderSystem.disableTexture();
//            RenderSystem.enableBlend();
//            RenderSystem.disableAlphaTest();
//            RenderSystem.defaultBlendFunc();
//            RenderSystem.shadeModel(7425);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            //bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

            bufferbuilder.pos(x, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, y, 0f).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(x, y, 0f).color(f5, f6, f7, f4).endVertex();

            //tessellator.draw();
//            RenderSystem.shadeModel(7424);
//            RenderSystem.disableBlend();
//            RenderSystem.enableAlphaTest();
//            RenderSystem.enableTexture();

        }

        public static void drawVerticalBuilding(double x,
                                                double y,
                                                double width,
                                                double height,
                                                int start,
                                                int end) {

            width += x;
            height += y;

            float f = (float) (start >> 24 & 255) / 255.0F;
            float f1 = (float) (start >> 16 & 255) / 255.0F;
            float f2 = (float) (start >> 8 & 255) / 255.0F;
            float f3 = (float) (start & 255) / 255.0F;
            float f4 = (float) (end >> 24 & 255) / 255.0F;
            float f5 = (float) (end >> 16 & 255) / 255.0F;
            float f6 = (float) (end >> 8 & 255) / 255.0F;
            float f7 = (float) (end & 255) / 255.0F;

//            RenderSystem.disableTexture();
//            RenderSystem.enableBlend();
//            RenderSystem.disableAlphaTest();
//            RenderSystem.defaultBlendFunc();
//            RenderSystem.shadeModel(7425);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            //bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);

            bufferbuilder.pos(x, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, height, 0f).color(f1, f2, f3, f).endVertex();
            bufferbuilder.pos(width, y, 0f).color(f5, f6, f7, f4).endVertex();
            bufferbuilder.pos(x, y, 0f).color(f5, f6, f7, f4).endVertex();

            //tessellator.draw();
//            RenderSystem.shadeModel(7424);
//            RenderSystem.disableBlend();
//            RenderSystem.enableAlphaTest();
//            RenderSystem.enableTexture();

        }

        public static void drawTexture(final float x,
                                       final float y,
                                       final float width,
                                       final float height,
                                       final float radius,
                                       final float alpha) {
            pushMatrix();
            enableBlend();
            blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            ShaderUtil.TEXTURE_ROUND_SHADER.attach();

            ShaderUtil.TEXTURE_ROUND_SHADER.setUniform("rectSize", (float) (width * 2), (float) (height * 2));
            ShaderUtil.TEXTURE_ROUND_SHADER.setUniform("radius", radius * 2);
            ShaderUtil.TEXTURE_ROUND_SHADER.setUniform("alpha", alpha);

            quadsBegin(x, y, width, height, 7);


            ShaderUtil.TEXTURE_ROUND_SHADER.detach();
            popMatrix();
        }

        private static Framebuffer framebuffer = new Framebuffer(1, 1, true, false);

        public static void drawTextureTest(float x, float y, double width, double height,
                                           final float radius,
                                           final float alpha) {
            //setupFram(framebuffer)
            pushMatrix();
            enableBlend();
            blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            ShaderUtil.TEXTURE_ROUND_SHADER.attach();

            ShaderUtil.TEXTURE_ROUND_SHADER.setUniform("rectSize", (float) (width * 2), (float) (height * 2));
            ShaderUtil.TEXTURE_ROUND_SHADER.setUniform("radius", radius * 2);
            ShaderUtil.TEXTURE_ROUND_SHADER.setUniform("alpha", alpha);

            AbstractGui.drawScaledCustomSizeModalRect(x, y, 8F, 8F, 8F, 8F, (float) width, (float) height, 64, 64);
            //quadsBegin(x,y, (float) width, (float) height,7);

            ShaderUtil.TEXTURE_ROUND_SHADER.detach();
            popMatrix();
        }


        public static void quadsBegin(float x, float y, float width, float height, int glQuads) {
            buffer.begin(glQuads, POSITION_TEX);
            {
                buffer.pos(x, y, 0).tex(0, 0).endVertex();
                buffer.pos(x, y + height, 0).tex(0, 1).endVertex();
                buffer.pos(x + width, y + height, 0).tex(1, 1).endVertex();
                buffer.pos(x + width, y, 0).tex(1, 0).endVertex();
            }
            tessellator.draw();
        }




        static ShaderUtil out = new ShaderUtil("out");
        public static void drawRoundedRect(float x, float y, float x2, float y2, float round, int color) {
            drawRoundedGradientRect(x, y, x2, y2, round, 1.0F, color, color, color, color);
        }
        public static void drawRoundedRect(double x, double y, double x2, double y2, float round, Color color) {
            drawRoundedGradientRect((float) x, (float) y, (float) x2, (float) y2, round * 2, 1.0F, color.getRGB(), color.getRGB(), color.getRGB(), color.getRGB());
        }
        public static void drawRoundedGradientRect(float x, float y, float x2, float y2, float round, float value, int color1, int color2, int color3, int color4) {
            float[] c1 = ColorUtil.getRGBAf(color1);
            float[] c2 = ColorUtil.getRGBAf(color2);
            float[] c3 = ColorUtil.getRGBAf(color3);
            float[] c4 = ColorUtil.getRGBAf(color4);
            GlStateManager.color((int) 0.0F, (int) 0.0F, (int) 0.0F, (int) 0.0F);
            GlStateManager.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            ROUNDED_GRADIENT.useProgram();
            ROUNDED_GRADIENT.setupUniform2f("size", x2 * 2.0F, y2 * 2.0F);
            ROUNDED_GRADIENT.setupUniform4f("round", round, round, round, round);
            ROUNDED_GRADIENT.setupUniform2f("smoothness", 0.0F, 1.5F);
            ROUNDED_GRADIENT.setupUniform1f("value", value);
            ROUNDED_GRADIENT.setupUniform4f("color1", c1[0], c1[1], c1[2], c1[3]);
            ROUNDED_GRADIENT.setupUniform4f("color2", c2[0], c2[1], c2[2], c2[3]);
            ROUNDED_GRADIENT.setupUniform4f("color3", c3[0], c3[1], c3[2], c3[3]);
            ROUNDED_GRADIENT.setupUniform4f("color4", c4[0], c4[1], c4[2], c4[3]);
            allocTextureRectangle(x, y, x2, y2);
            ROUNDED_GRADIENT.unloadProgram();
            GlStateManager.disableBlend();
        }
        public static void drawRoundedRect(float x, float y, float x2, float y2, float round, float swapX, float swapY, int firstColor, int secondColor) {
            float[] c = ColorUtil.getRGBAf(firstColor);
            float[] c1 = ColorUtil.getRGBAf(secondColor);
            GlStateManager.color((int) 0.0F, (int) 0.0F, (int) 0.0F, (int) 0.0F);
            GlStateManager.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            ROUNDED.useProgram();
            ROUNDED.setupUniform2f("size", (x2 - round) * 2.0F, (y2 - round) * 2.0F);
            ROUNDED.setupUniform1f("round", round);
            ROUNDED.setupUniform2f("smoothness", 0.0F, 1.5F);
            ROUNDED.setupUniform2f("swap", swapX, swapY);
            ROUNDED.setupUniform4f("firstColor", c[0], c[1], c[2], c[3]);
            ROUNDED.setupUniform4f("secondColor", c1[0], c1[1], c1[2], c[3]);
            allocTextureRectangle(x, y, x2, y2);
            ROUNDED.unloadProgram();
            GlStateManager.disableBlend();
        }

        public static void drawHud(float x, float y, float width, float height, float radius, float softness, float blur) {
           /* int clr1 = injectAlpha(ColorUtil.getColor(270), 75);
            int clr2 = injectAlpha(ColorUtil.getColor(0), 75);
            int clr3 = injectAlpha(ColorUtil.getColor(180), 75);
            int clr4 = injectAlpha(ColorUtil.getColor(90), 75);*/
            int clr1 = ColorUtil.getColor(270);
            int clr2 = ColorUtil.getColor(0);
            int clr3 = ColorUtil.getColor(180);
            int clr4 = ColorUtil.getColor(90);

            // drawShadow(x, y, width, height, (int) radius, ColorUtil.getColor(270), ColorUtil.getColor(0), ColorUtil.getColor(180), ColorUtil.getColor(90));
            //     drawRoundedGradientRect(x, y, width, height, radius,1, clr1, clr2, clr3, clr4);
            //   drawRoundedBlur(new MatrixStack(), x, y, width, height, radius, Color.BLACK, blur, 0.5F);
        }
        public static void drawBlur(float x, float y, float x2, float y2, float round, int color, float blurStrenth, float blurOpacity) {
            // Получение цвета в формате RGBA

            // Установка начального цвета

            // Включение блендинга
            GlStateManager.enableBlend();
            float i = (float)mc.getMainWindow().getGuiScaleFactor();
            // Использование шейдера
            BLUR.useProgram();

            // Установка юниформов для шейдера
            BLUR.setupUniform2f("uSize", x2 * i, y2 * i);  // Размер области
            BLUR.setupUniform2f("uLocation", x * i, -y * i + mc.getMainWindow().getScaledHeight() * i - y2 * i);  // Позиция
            BLUR.setupUniform1f("radius", round);  // Радиус размытия
            BLUR.setupUniform1f("brightness", blurStrenth);  // Яркость (настроить при необходимости)
            BLUR.setupUniform1f("quality", blurOpacity);  // Качество размытия (может быть увеличено для сильного размытия)
            BLUR.setupUniform1f("color1", color);  // Основной цвет

            // Отрисовка прямоугольника с размытой областью

            // Отключение программы шейдера
            BLUR.unloadProgram();

            // Отключение блендинга
            GlStateManager.disableBlend();
        }
        public static void drawRoundedBlur(MatrixStack matrices, float x, float y, float width, float height, float radius, Color c1, float blurStrength, float blurOpacity) {
            setupRender();
            BufferBuilder buffer = Tessellator.getInstance().getBuffer();
            Matrix4f matrix = matrices.getLast().getMatrix();
            buffer.begin(GL11.GL_QUADS, POSITION);

            buffer.pos(matrix, x, y, 0.0F).color(c1.getRed(), c1.getGreen(), c1.getBlue(), (int)(blurOpacity * 255)).endVertex();
            buffer.pos(matrix, x, y + height, 0.0F).color(c1.getRed(), c1.getGreen(), c1.getBlue(), (int)(blurOpacity * 255)).endVertex();
            buffer.pos(matrix, x + width, y + height, 0.0F).color(c1.getRed(), c1.getGreen(), c1.getBlue(), (int)(blurOpacity * 255)).endVertex();
            buffer.pos(matrix, x + width, y, 0.0F).color(c1.getRed(), c1.getGreen(), c1.getBlue(), (int)(blurOpacity * 255)).endVertex();

            BLUR.setupUniform2f("InputResolution", mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight()); // Разрешение экрана
            BLUR.setupUniform2f("uSize", (width), (height)); // Размер области
            BLUR.setupUniform2f("uLocation", x, y); // Позиция
            BLUR.setupUniform1f("radius", radius); // Радиус размытия
            BLUR.setupUniform1f("Brightness", 1.0F); // Яркость (настроить при необходимости)
            BLUR.setupUniform1f("Quality", blurStrength); // Качество размытия (может быть увеличено для сильного размытия)
            BLUR.setupUniform4f("color1", c1.getRed() / 255.0F, c1.getGreen() / 255.0F, c1.getBlue() / 255.0F, 1.0F); // Основной цвет

            Tessellator.getInstance().draw();
            endRender();
        }
        public static void setupRender() {
            RenderSystem.enableBlend();
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        }

        public static void endRender() {
            RenderSystem.disableBlend();
        }
        public static void allocTextureRectangle(float x, float y, float width, float height) {
            BUILDER.begin(7, DefaultVertexFormats.POSITION_TEX);
            BUILDER.pos((double)x, (double)y, 0.0).tex(0.0F, 0.0F).endVertex();
            BUILDER.pos((double)x, (double)(y + height), 0.0).tex((float) 0.0F, 1.0F).endVertex();
            BUILDER.pos((double)(x + width), (double)(y + height), 0.0).tex(1.0F, 1.0F).endVertex();
            BUILDER.pos((double)(x + width), (double)y, 0.0).tex((float) 1.0F, 0.0F).endVertex();
            TESSELLATOR.draw();
        }
        public static void drawRoundedOutline(float x, float y, float width, float height, float radius, float outlineThickness, int color, int outlineColor) {
            pushMatrix();
            enableBlend();
            out.attach();

            out.setUniform("size", (float) (width * 2), (float) (height * 2));
            out.setUniform("round", radius * 2, radius * 2, radius * 2, radius * 2);

            out.setUniform("smoothness", 0.f, 1.5f);
            out.setUniform("outlineSize", outlineThickness);
            out.setUniform("color",
                    getRed(color) / 255f,
                    getGreen(color) / 255f,
                    getBlue(color) / 255f,
                    IntColor.getAlpha(color) / 255f);

            ShaderUtil.CORNER_ROUND_SHADER.setUniform("outlineColor",
                    getRed(outlineColor) / 255f,
                    getGreen(outlineColor) / 255f,
                    getBlue(outlineColor) / 255f,
                    IntColor.getAlpha(outlineColor) / 255f);

            ShaderUtil.CORNER_ROUND_SHADER.drawQuads(x, y, width, height);

            ShaderUtil.CORNER_ROUND_SHADER.detach();
            disableBlend();
            popMatrix();
        }


        public static void drawRoundedCorner(float x,
                                             float y,
                                             float width,
                                             float height,
                                             float radius,
                                             int color,
                                             final Corner corner) {
            pushMatrix();
            enableBlend();
            ShaderUtil.CORNER_ROUND_SHADER.attach();

            ShaderUtil.CORNER_ROUND_SHADER.setUniform("size", (float) (width * 2), (float) (height * 2));
            switch (corner) {
                case ALL ->
                        ShaderUtil.CORNER_ROUND_SHADER.setUniform("round", radius * 2, radius * 2, radius * 2, radius * 2);
                case RIGHT -> ShaderUtil.CORNER_ROUND_SHADER.setUniform("round", 0, 0, radius * 2, radius * 2);
                case LEFT -> ShaderUtil.CORNER_ROUND_SHADER.setUniform("round", radius * 2, radius * 2, 0, 0);
                case TOP_RIGHT -> ShaderUtil.CORNER_ROUND_SHADER.setUniform("round", 0, 0, radius * 2, 0);
                case TOP -> ShaderUtil.CORNER_ROUND_SHADER.setUniform("round", radius * 2, 0, radius * 2, 0);
                case DOWN -> ShaderUtil.CORNER_ROUND_SHADER.setUniform("round", 0, radius * 2, 0, radius * 2);
            }

            ShaderUtil.CORNER_ROUND_SHADER.setUniform("smoothness", 0.f, 1.5f);
            ShaderUtil.CORNER_ROUND_SHADER.setUniform("color",
                    getRed(color) / 255f,
                    getGreen(color) / 255f,
                    getBlue(color) / 255f,
                    IntColor.getAlpha(color) / 255f);

            ShaderUtil.CORNER_ROUND_SHADER.drawQuads(x, y, width, height);

            ShaderUtil.CORNER_ROUND_SHADER.detach();
            disableBlend();
            popMatrix();
        }

        public static void drawRoundedCorner(float x,
                                             float y,
                                             float width,
                                             float height,
                                             Vector4f vector4f) {
            pushMatrix();
            enableBlend();
            CORNER_ROUND_SHADER_TEXTURE.attach();

            CORNER_ROUND_SHADER_TEXTURE.setUniform("size", (float) (width * 2), (float) (height * 2));
            CORNER_ROUND_SHADER_TEXTURE.setUniform("round", vector4f.x * 2, vector4f.y * 2, vector4f.z * 2, vector4f.w * 2);

            CORNER_ROUND_SHADER_TEXTURE.setUniform("smoothness", 0.f, 1.5f);
            CORNER_ROUND_SHADER_TEXTURE.setUniformf("alpha", 1);

            CORNER_ROUND_SHADER_TEXTURE.drawQuads(x, y, width, height);

            CORNER_ROUND_SHADER_TEXTURE.detach();
            disableBlend();
            popMatrix();
        }


        public static void drawRoundedCorner(float x,
                                             float y,
                                             float width,
                                             float height,
                                             float radius,
                                             int color) {
            pushMatrix();
            enableBlend();
            ShaderUtil.CORNER_ROUND_SHADER.attach();

            ShaderUtil.CORNER_ROUND_SHADER.setUniform("size", (float) (width * 2), (float) (height * 2));
            ShaderUtil.CORNER_ROUND_SHADER.setUniform("round", radius * 2, radius * 2, radius * 2, radius * 2);

            ShaderUtil.CORNER_ROUND_SHADER.setUniform("smoothness", 0.f, 1.5f);
            ShaderUtil.CORNER_ROUND_SHADER.setUniform("color",
                    getRed(color) / 255f,
                    getGreen(color) / 255f,
                    getBlue(color) / 255f,
                    IntColor.getAlpha(color) / 255f);

            ShaderUtil.CORNER_ROUND_SHADER.drawQuads(x, y, width, height);

            ShaderUtil.CORNER_ROUND_SHADER.detach();
            disableBlend();
            popMatrix();
        }

        public static void drawRoundedCorner(float x,
                                             float y,
                                             float width,
                                             float height,
                                             Vector4f vector4f,
                                             int color) {
            pushMatrix();
            enableBlend();
            ShaderUtil.CORNER_ROUND_SHADER.attach();

            ShaderUtil.CORNER_ROUND_SHADER.setUniform("size", (float) (width * 2), (float) (height * 2));
            ShaderUtil.CORNER_ROUND_SHADER.setUniform("round", vector4f.x * 2, vector4f.y * 2, vector4f.z * 2, vector4f.w * 2);

            ShaderUtil.CORNER_ROUND_SHADER.setUniform("smoothness", 0.f, 1.5f);
            ShaderUtil.CORNER_ROUND_SHADER.setUniform("color",
                    getRed(color) / 255f,
                    getGreen(color) / 255f,
                    getBlue(color) / 255f,
                    IntColor.getAlpha(color) / 255f);

            ShaderUtil.CORNER_ROUND_SHADER.drawQuads(x, y, width, height);

            ShaderUtil.CORNER_ROUND_SHADER.detach();
            disableBlend();
            popMatrix();
        }

        private static ShaderUtil rounded = new ShaderUtil("cornerGradient");



        public static void drawGradientRound(float x, float y, float width, float height, float radius, int bottomLeft, int topLeft, int bottomRight, int topRight) {
            RenderSystem.color4f(1, 1, 1, 1);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            ShaderUtil.GRADIENT_ROUND_SHADER.attach();
            ShaderUtil.setupRoundedRectUniforms(x, y, width, height, radius, ShaderUtil.GRADIENT_ROUND_SHADER);

            ShaderUtil.GRADIENT_ROUND_SHADER.setUniform("color1", rgb(bottomLeft));
            ShaderUtil.GRADIENT_ROUND_SHADER.setUniform("color2", rgb(topLeft));
            ShaderUtil.GRADIENT_ROUND_SHADER.setUniform("color3", rgb(bottomRight));
            ShaderUtil.GRADIENT_ROUND_SHADER.setUniform("color4", rgb(topRight));

            ShaderUtil.GRADIENT_ROUND_SHADER.drawQuads(x - 1, y - 1, width + 2, height + 2);
            ShaderUtil.GRADIENT_ROUND_SHADER.detach();
            RenderSystem.disableBlend();
        }



        public static void drawImage(ResourceLocation resourceLocation, float x, float y, float width, float height, int color) {
            RenderSystem.pushMatrix();
            RenderSystem.enableBlend();
            setColor(color);

            mc.getTextureManager().bindTexture(resourceLocation);
            AbstractGui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
            RenderSystem.color4f(1, 1, 1, 1);
            RenderSystem.popMatrix();

        }



        public static void setColor(int color) {
            setColor(color, (float) (color >> 24 & 255) / 255.0F);
        }

        public static void setColor(int color, float alpha) {
            float r = (float) (color >> 16 & 255) / 255.0F;
            float g = (float) (color >> 8 & 255) / 255.0F;
            float b = (float) (color & 255) / 255.0F;
            RenderSystem.color4f(r, g, b, alpha);
        }

        public static void scaleStart(float x, float y, float scale) {
            GL11.glPushMatrix();
            GL11.glTranslatef(x, y, 0.0F);
            GL11.glScalef(scale, scale, 1.0F);
            GL11.glTranslatef(-x, -y, 0.0F);
        }

        public enum Corner {
            RIGHT,
            LEFT,
            TOP_RIGHT,
            TOP,
            ALL,
            DOWN
        }
    }

    public static class Render3D {

        public static void drawBlockBox(BlockPos blockPos, int color) {
            drawBox(new AxisAlignedBB(blockPos).offset(-mc.getRenderManager().info.getProjectedView().x, -mc.getRenderManager().info.getProjectedView().y, -mc.getRenderManager().info.getProjectedView().z), color);
        }

        public static void drawBox(AxisAlignedBB bb, int color) {
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL_DEPTH_TEST);
            GL11.glEnable(GL_LINE_SMOOTH);
            GL11.glLineWidth(1);
            float[] rgb = IntColor.rgb(color);
            GlStateManager.color4f(rgb[0], rgb[1], rgb[2], rgb[3]);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder vertexbuffer = tessellator.getBuffer();
            vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
            vertexbuffer.pos(bb.minX, bb.minY, bb.minZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.maxX, bb.minY, bb.minZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.maxX, bb.minY, bb.maxZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.minX, bb.minY, bb.maxZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.minX, bb.minY, bb.minZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            tessellator.draw();
            vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
            vertexbuffer.pos(bb.minX, bb.maxY, bb.minZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.maxX, bb.maxY, bb.minZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.minX, bb.maxY, bb.maxZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.minX, bb.maxY, bb.minZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            tessellator.draw();
            vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
            vertexbuffer.pos(bb.minX, bb.minY, bb.minZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.minX, bb.maxY, bb.minZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.maxX, bb.minY, bb.minZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.maxX, bb.maxY, bb.minZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.maxX, bb.minY, bb.maxZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.maxX, bb.maxY, bb.maxZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.minX, bb.minY, bb.maxZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            vertexbuffer.pos(bb.minX, bb.maxY, bb.maxZ).color(rgb[0], rgb[1], rgb[2], rgb[3]).endVertex();
            tessellator.draw();
            GlStateManager.color4f(rgb[0], rgb[1], rgb[2], rgb[3]);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL_DEPTH_TEST);
            GL11.glDisable(GL_LINE_SMOOTH);
            GL11.glPopMatrix();

        }
    }

    public static class SmartScissor {
        private static class State implements Cloneable {
            public boolean enabled;
            public int transX;
            public int transY;
            public int x;
            public int y;
            public int width;
            public int height;

            @Override
            public State clone() {
                try {
                    return (State) super.clone();
                } catch (CloneNotSupportedException e) {
                    throw new AssertionError(e);
                }
            }
        }

        private static State state = new State();

        private static final List<State> stateStack = Lists.newArrayList();

        public static void push() {
            stateStack.add(state.clone());
            GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
        }

        public static void pop() {
            state = stateStack.remove(stateStack.size() - 1);
            GL11.glPopAttrib();
        }

        public static void unset() {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            state.enabled = false;
        }

        public static void setFromComponentCoordinates(int x, int y, int width, int height) {
            int scaleFactor = 2;

            int screenX = x * scaleFactor;
            int screenY = y * scaleFactor;
            int screenWidth = width * scaleFactor;
            int screenHeight = height * scaleFactor;
            screenY = mc.getMainWindow().getHeight() - screenY - screenHeight;
            set(screenX, screenY, screenWidth, screenHeight);
        }

        public static void setFromComponentCoordinates(double x, double y, double width, double height) {
            int scaleFactor = 2;

            int screenX = (int) (x * scaleFactor);
            int screenY = (int) (y * scaleFactor);
            int screenWidth = (int) (width * scaleFactor);
            int screenHeight = (int) (height * scaleFactor);
            screenY = mc.getMainWindow().getHeight() - screenY - screenHeight;
            set(screenX, screenY, screenWidth, screenHeight);
        }

        public static void setFromComponentCoordinates(double x, double y, double width, double height, float scale) {
            float scaleFactor = 2;

            int screenX = (int) (x * scaleFactor);
            int screenY = (int) (y * scaleFactor);
            int screenWidth = (int) (width * scaleFactor);
            int screenHeight = (int) (height * scaleFactor);
            screenY = mc.getMainWindow().getHeight() - screenY - screenHeight;
            set(screenX, screenY, screenWidth, screenHeight);
        }

        public static void set(int x, int y, int width, int height) {
            Rectangle screen = new Rectangle(0, 0, mc.getMainWindow().getWidth(), mc.getMainWindow().getHeight());
            Rectangle current;
            if (state.enabled) {
                current = new Rectangle(state.x, state.y, state.width, state.height);
            } else {
                current = screen;
            }
            Rectangle target = new Rectangle(x + state.transX, y + state.transY, width, height);
            Rectangle result = current.intersection(target);
            result = result.intersection(screen);
            if (result.width < 0) result.width = 0;
            if (result.height < 0) result.height = 0;
            state.enabled = true;
            state.x = result.x;
            state.y = result.y;
            state.width = result.width;
            state.height = result.height;
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(result.x, result.y, result.width, result.height);
        }

        public static void translate(int x, int y) {
            state.transX = x;
            state.transY = y;
        }

        public static void translateFromComponentCoordinates(int x, int y) {
            int totalHeight = mc.getMainWindow().getScaledHeight();
            int scaleFactor = (int) mc.getMainWindow().getGuiScaleFactor();

            int screenX = x * scaleFactor;
            int screenY = y * scaleFactor;
            screenY = (totalHeight * scaleFactor) - screenY;
            translate(screenX, screenY);
        }

        private SmartScissor() {
        }
    }

    public static class Stencil {

        // Инициализация буфера кадра
        public static void init() {
            mc.getFramebuffer().bindFramebuffer(false); // Привязка буфера кадра

            if (mc.getFramebuffer().depthBuffer > -1) { // Если есть буфер глубины
                EXTFramebufferObject.glDeleteRenderbuffersEXT(mc.getFramebuffer().depthBuffer); // Удаление буфера глубины
                final int stencilDepthBufferID = EXTFramebufferObject.glGenRenderbuffersEXT(); // Создание нового буфера трафарета
                EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID); // Привязка буфера трафарета
                EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH24_STENCIL8_EXT, mc.getMainWindow().getScaledWidth(), mc.getMainWindow().getScaledHeight()); // Установка параметров буфера трафарета
                EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID); // Привязка буфера трафарета к фреймбуферу
                EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencilDepthBufferID); // Привязка буфера трафарета к фреймбуферу
                mc.getFramebuffer().depthBuffer = -1; // Отключение буфера глубины
            }

            glClear(GL_STENCIL_BUFFER_BIT); // Очистка буфера трафарета
            glEnable(GL_STENCIL_TEST); // Включение теста трафарета

            glStencilFunc(GL_ALWAYS, 1, 0xFF); // Установка функции теста трафарета
            glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE); // Установка операций трафарета
            glColorMask(false, false, false, false); // Отключение записи в буфер цвета
        }

        // Чтение значения трафарета
        public static void read(int ref) {
            glColorMask(true, true, true, true); // Включение записи в буфер цвета
            glStencilFunc(GL_EQUAL, ref, 0xFF); // Установка функции теста трафарета
            glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP); // Установка операций трафарета
        }

        // Выключение теста трафарета
        public static void unload() {
            glDisable(GL_STENCIL_TEST);
        }
    }

    static {
        BUILDER = TESSELLATOR.getBuffer();
        ROUNDED = new Shader("rounded.fsh", true);
        BLUR = new Shader("blur.fsh", true);
        GRADIENT_MASK = new Shader("gradient_mask.fsh", true);

        ROUNDED_GRADIENT = new Shader("rounded_gradient.fsh", true);

    }
}
