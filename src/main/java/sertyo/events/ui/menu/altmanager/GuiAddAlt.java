package sertyo.events.ui.menu.altmanager;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MainWindow;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import sertyo.events.ui.menu.altmanager.alt.Alt;
import sertyo.events.ui.menu.altmanager.alt.AltManager;
import sertyo.events.ui.menu.widgets.CustomButton;
import sertyo.events.utility.render.RenderUtility;
import sertyo.events.utility.render.fonts.Fonts;


import java.awt.*;
import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;

import static sertyo.events.utility.Utility.mc;

public class GuiAddAlt extends Screen {
   private TextFieldWidget nameField;
   private String status;
   private final GuiAltManager guiAltManager;

   protected GuiAddAlt() {
       super(new StringTextComponent("svozvvz"));
       this.status = TextFormatting.GRAY + "Add Alt";
      this.guiAltManager = new GuiAltManager();
   }

   public void init() {
      MainWindow scaledResolution = mc.getMainWindow();
      int scaledWidth = scaledResolution.getScaledWidth();
      int scaledHeight = scaledResolution.getScaledHeight();

      this.nameField = new TextFieldWidget(mc.fontRenderer, scaledWidth / 2 - 100, scaledHeight / 2 - 40, 200, 20, new StringTextComponent(""));
      this.addButton(new CustomButton(scaledWidth / 2 - 102, scaledHeight / 2 + 40, 100, 22, new StringTextComponent("Добавить"), p_onPress_1_ -> {
         AddAltThread login = new AddAltThread(this.nameField.getText());
         login.start();
      }));
      this.addButton(new CustomButton( scaledWidth / 2 + 2, scaledHeight / 2 + 40, 100, 22, new StringTextComponent("Вернуться"), p_onPress_1_ -> {
         mc.displayGuiScreen(this.guiAltManager);
      }));
      super.init();
   }

   public void displayGuiScreen(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
      MainWindow scaledResolution = mc.getMainWindow();
      int scaledWidth = scaledResolution.getScaledWidth();
      int scaledHeight = scaledResolution.getScaledHeight();
      GlStateManager.pushMatrix();
      RenderUtility.drawRect(0.0F, 0.0F, (float)scaledWidth, (float)scaledHeight, (new Color(20, 20, 20)).getRGB());
      this.nameField.setText("Никнейм");
      Fonts.mntsb17.drawCenteredString(this.status, (float)scaledWidth / 2.0F, 10.0F, -1);
      super.render(ms, mouseX, mouseY, partialTicks);
      GlStateManager.popMatrix();
   }

   public boolean mouseClicked(double mouseX, double mouseY, int button) {
      this.nameField.mouseClicked(mouseX, mouseY, button);
      return super.mouseClicked(mouseX, mouseY, button);
   }

   public boolean keyPressed(int keycode, int skancode, int modif) {
      if (keycode == 1) {
         mc.displayGuiScreen(new GuiAltManager());
      } else {
         this.nameField.keyPressed(keycode, skancode, modif);
         if (skancode == '\t' && (this.nameField.isFocused())) {
            this.nameField.setFocused(!this.nameField.isFocused());
         }

      }
      return this.keyPressed(keycode, skancode, modif);
   }


   private static void setStatus(GuiAddAlt guiAddAlt, String status) {
      guiAddAlt.status = status;
   }

   private class AddAltThread extends Thread {
      private final String username;

      AddAltThread(String username) {
         this.username = username;
         GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GRAY + "Add Alt");
      }

      private void checkAndAddAlt(String username, String password) {
         try {
            YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);

            try {
               auth.logIn();
               AltManager.addAccount(new Alt(username, password, auth.getSelectedProfile().getName(), Alt.Status.Working, Alt.getCurrentDate()));
               GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Добавлен аккаунт - " + TextFormatting.RED + this.username + TextFormatting.WHITE + " (Mojang)");
            } catch (AuthenticationException var6) {
               GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.RED + "Ошибка подключения!");
               var6.printStackTrace();
            }
         } catch (Throwable var7) {
            GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.RED + "Ошибка!");
            var7.printStackTrace();
         }

      }

      public void run() {
            AltManager.addAccount(new Alt(this.username, "", Alt.getCurrentDate()));
            GuiAddAlt.setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Добавлен аккаунт - " + TextFormatting.RED + this.username + TextFormatting.WHITE + " (без лицензии)");
      }
   }
}
