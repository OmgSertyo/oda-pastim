package sertyo.events.ui.menu.altmanager;


import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.lang3.RandomStringUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWScrollCallback;
import sertyo.events.Main;
import sertyo.events.ui.menu.altmanager.alt.Alt;
import sertyo.events.ui.menu.altmanager.alt.AltLoginThread;
import sertyo.events.ui.menu.altmanager.alt.AltManager;
import sertyo.events.ui.menu.main.NeironMainMenu;
import sertyo.events.ui.menu.widgets.CustomButton;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.RenderUtility;
import sertyo.events.utility.render.StencilUtility;
import sertyo.events.utility.render.fonts.Fonts;

import static sertyo.events.utility.Utility.mc;

public class GuiAltManager extends Screen {
   public Alt selectedAlt = null;
   private AltLoginThread loginThread;
   public int scrollY = 0;

   public GuiAltManager() {
      super(new StringTextComponent("sosal? sovri nevri"));
   }

   public void init() {
      MainWindow scaledResolution = mc.getMainWindow();
      int scaledWidth = scaledResolution.getScaledWidth();
      int scaledHeight = scaledResolution.getScaledHeight();
      this.addButton(new CustomButton(scaledWidth / 2 - 121, scaledHeight - 48, 57, 20, new StringTextComponent("Добавить"), p_onPress_1_ -> {
         mc.displayGuiScreen(new GuiAddAlt());

      }));
      this.addButton(new CustomButton(scaledWidth / 2 - 59, scaledHeight - 48, 57,20, new StringTextComponent("Случайный"), p_onPress_1_ -> {
         String randomName = RandomStringUtils.randomAlphabetic(3) + RandomStringUtils.randomNumeric(3) + "_nrn";
         AltManager.addAccount(new Alt(randomName, "", Alt.getCurrentDate()));
         mc.session = new Session(randomName, "", "", "mojang");
      }));
      this.addButton(new CustomButton(scaledWidth / 2 + 3, scaledHeight - 48, 57, 20, new StringTextComponent("Удалить"), p_onPress_1_ -> {
         if (this.loginThread != null) {
            this.loginThread = null;
         }

         AltManager.removeAccount(this.selectedAlt);
         this.selectedAlt = null;
      }));
      this.addButton(new CustomButton(scaledWidth / 2 + 65, scaledHeight - 48, 57, 20, new StringTextComponent("Очистить"), p_onPress_1_ -> {
         AltManager.clearAccounts();
      }));
      this.addButton(new CustomButton(scaledWidth / 2 - 121, scaledHeight - 24, 243, 20, new StringTextComponent("Вернуться"), p_onPress_1_ -> {
         mc.displayGuiScreen(new NeironMainMenu());
      }));
      super.init();
   }


   public void render(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
      MainWindow scaledResolution = mc.getMainWindow();
      int scaledWidth = scaledResolution.getScaledWidth();
      int scaledHeight = scaledResolution.getScaledHeight();
      int color = ColorUtil.getColor(0);
      int color2 = ColorUtil.getColor(500);
      GlStateManager.pushMatrix();
      RenderUtility.drawRect(0.0F, 0.0F, (float)scaledWidth, (float)scaledHeight, (new Color(20, 20, 20)).getRGB());
      GLFW.glfwSetScrollCallback(GLFW.glfwGetCurrentContext(), new GLFWScrollCallback() {
         @Override
         public void invoke(long window, double xoffset, double yoffset) {
            // Обновление scrollY на основе значения прокрутки
            scrollY = (int)((float)scrollY + (float)yoffset / 10.0F);
         }
      });
      int offset = this.scrollY;
      StencilUtility.initStencilToWrite();
      RenderUtility.drawRect((float)scaledWidth / 2.0F - 100.5F, 0.0F, 201.0F, (float)(scaledHeight - 50), 3);
      StencilUtility.readStencilBuffer(1);

      for(Iterator var19 = AltManager.registry.iterator(); var19.hasNext(); offset += 40) {
         Alt alt = (Alt)var19.next();
         if (alt.equals(this.selectedAlt)) {
            RenderUtility.drawRoundedGradientRect((float)scaledWidth / 2.0F - 100.5F, (float)(20 + offset) - 0.5F, 201.0F, 36.0F, 7.0F, 1.0F, color, color, color2, color2);
         }

         RenderUtility.drawRoundedRect((float)scaledWidth / 2.0F - 100.0F, (float)(20 + offset), 200.0F, 35.0F, 6.0F, (new Color(30, 30, 30)).getRGB());
         GlStateManager.pushMatrix();
         GlStateManager.color((int) 1.0F, (int) 1.0F, (int) 1.0F, (int) 1.0F);
         if (alt.getSkin() == null) {
            alt.setSkin(AbstractClientPlayerEntity.getLocationSkin(alt.getUsername()));
           // this.getDownloadImageSkin(alt, alt.getUsername());
         } else {
            mc.getTextureManager().bindTexture(alt.getSkin());
            GlStateManager.enableTexture();
            AbstractGui.drawScaledCustomSizeModalRect(scaledWidth / 2 - 95, 25 + offset, 8.0F, 8.0F, 8, 8, 25, 25, 64.0F, 64.0F);
         }

         GlStateManager.popMatrix();
         String name = alt.getUsername();
         if (name.equalsIgnoreCase(mc.session.getUsername())) {
            name = TextFormatting.GREEN + name;
         }

         Fonts.mntsb16.drawString(name, (float)scaledWidth / 2.0F - 65.0F, (float)(30 + offset), -1);
         Fonts.mntsb13.drawString(alt.getDate(), (float)scaledWidth / 2.0F - 65.0F, 42.5F + (float)offset, -1);
      }

      StencilUtility.uninitStencilBuffer();
      super.render(stack, mouseX, mouseY, partialTicks);
      GlStateManager.popMatrix();
   }

   public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
      MainWindow scaledResolution = mc.getMainWindow();
      int scaledWidth = scaledResolution.getScaledWidth();

      GLFW.glfwSetScrollCallback(GLFW.glfwGetCurrentContext(), new GLFWScrollCallback() {
         @Override
         public void invoke(long window, double xoffset, double yoffset) {
            scrollY = (int)((float)scrollY + (float)yoffset / 10.0F);
         }
      });
            int offset = this.scrollY;

      for(Iterator var16 = AltManager.registry.iterator(); var16.hasNext(); offset += 40) {
         Alt alt = (Alt)var16.next();
         if (RenderUtility.isHovered(mouseX, mouseY, (float)scaledWidth / 2.0F - 100.0F, (float)(20 + offset), 200.0F, 35.0F)) {
            if (this.selectedAlt == alt) {
               (this.loginThread = new AltLoginThread(this.selectedAlt)).start();
             }

            this.selectedAlt = alt;
         }
      }

      return super.mouseClicked(mouseX, mouseY, mouseButton);
   }
}
