package sertyo.events.utility.render;

import com.mojang.blaze3d.systems.IRenderCall;
import com.mojang.blaze3d.systems.RenderSystem;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.Shader;
import net.optifine.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class KawaseBlur {

    public static KawaseBlur blur = new KawaseBlur();

    public final CustomFramebuffer BLURRED;
    public final CustomFramebuffer ADDITIONAL;
    CustomFramebuffer blurTarget = new CustomFramebuffer(false).setLinear();

    public KawaseBlur() {
        BLURRED = new CustomFramebuffer(false).setLinear();
        ADDITIONAL = new CustomFramebuffer(false).setLinear();
       
    }
    public void render(Runnable run) {

        StencilUtil.initStencilToWrite();
        run.run();
        StencilUtil.readStencilBuffer(1);
         BLURRED.draw();
        StencilUtil.uninitStencilBuffer();
    }
    private static final ShaderUtil kawaseDown = new ShaderUtil("kawaseDown");
    private static final ShaderUtil kawaseUp = new ShaderUtil("kawaseUp");

    public void updateBlur(float offset, int steps) {

        Minecraft mc = Minecraft.getInstance();
        Framebuffer mcFramebuffer = mc.getFramebuffer();
        ADDITIONAL.setup();
        mcFramebuffer.bindFramebufferTexture();
        kawaseDown.attach();
        kawaseDown.setUniform("offset", offset);
        kawaseDown.setUniformf("resolution", 1f / mc.getMainWindow().getWidth(),
                1f / mc.getMainWindow().getHeight());
        CustomFramebuffer.drawTexture();
        CustomFramebuffer[] buffers = {this.ADDITIONAL, this.BLURRED };
        for (int i = 1; i < steps; ++i) {
            int step = i % 2;
            buffers[step].setup();
            buffers[(step + 1) % 2].draw();
        }
        kawaseUp.attach();
        kawaseUp.setUniform("offset", offset);
        kawaseUp.setUniformf("resolution", 1f / mc.getMainWindow().getWidth(),
                1f / mc.getMainWindow().getHeight());
        for (int i = 0; i < steps; ++i) {
            int step = i % 2;
            buffers[(step + 1) % 2].setup();
            buffers[step].draw();
        }
        kawaseUp.detach();
        mcFramebuffer.bindFramebuffer(false);
    }

}
