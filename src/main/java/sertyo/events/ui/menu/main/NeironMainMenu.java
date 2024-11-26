//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.menu.main;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.MainWindow;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.WorldSelectionScreen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import sertyo.events.Main;
import sertyo.events.ui.menu.altmanager.AltManagerScreen;
import sertyo.events.ui.menu.widgets.CustomButton;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.RenderUtility;
import sertyo.events.utility.render.Vec2i;
import sertyo.events.utility.render.fonts.Fonts;


import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;



import static sertyo.events.utility.Utility.mc;

public class NeironMainMenu extends Screen {
   public NeironMainMenu() {
       super(ITextComponent.getTextComponentOrEmpty("NIGGA"));
   }

   public void init() {
      MainWindow scaledResolution = mc.getMainWindow();
      this.width = sertyo.events.Main.getInstance().getScaleMath().calc(scaledResolution.getScaledWidth());
      this.height = sertyo.events.Main.getInstance().getScaleMath().calc(scaledResolution.getScaledHeight());
      this.addButton(new CustomButton(this.width / 2 - 102, this.height / 2 - 35, 100, 22, new StringTextComponent("SinglePlayer"), p_onPress_1_ -> {
         mc.displayGuiScreen(new WorldSelectionScreen(this));
      }));
      this.addButton(new CustomButton(this.width / 2 + 2, this.height / 2 - 35, 100, 22, new StringTextComponent("MultiPlayer"), p_onPress_1_ -> {
         mc.displayGuiScreen(new MultiplayerScreen(this));
      }));
            this.addButton(new CustomButton(this.width / 2 - 102, this.height / 2 - 9, 100, 22, new StringTextComponent("AltManager"), p_onPress_1_ -> {
               mc.displayGuiScreen(new AltManagerScreen());
            }));
         this.addButton(new CustomButton(this.width / 2 + 2, this.height / 2 - 9, 100, 22, new StringTextComponent("Setting"), p_onPress_1_ -> {
            mc.displayGuiScreen(new OptionsScreen(this, this.minecraft.gameSettings));
         }));
         this.addButton(new CustomButton(this.width / 2 - 102, this.height / 2 + 17, 204, 22, new StringTextComponent("Exit"), p_onPress_1_ -> {
            mc.shutdownMinecraftApplet();

         }));

      }

   public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
      MainWindow scaledResolution = mc.getMainWindow();
      int scaledWidth = sertyo.events.Main.getInstance().getScaleMath().calc(scaledResolution.getScaledWidth());
      int scaledHeight = Main.getInstance().getScaleMath().calc(scaledResolution.getScaledHeight());
      Color color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0];
      Color color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1];
      Vec2i mouse = Main.getInstance().getScaleMath().getMouse(mouseX, mouseY, scaledResolution);
      Main.getInstance().getScaleMath().pushScale();
      RenderUtil.Render2D.drawRect(0.0F, 0.0F, (float)scaledWidth, (float)scaledHeight, (new Color(20, 20, 20)).getRGB());
      RenderUtility.applyRound(35.0F, 35.0F, 17.0F, 1.0F, () -> {
         RenderUtility.drawProfile((float)(scaledWidth - 45), 10.0F, 35.0F, 35.0F);
      });

      sertyo.events.utility.font.Fonts.msBold[17].drawString(Main.cheatProfile.getName(), (float)(scaledWidth - 50 - sertyo.events.utility.font.Fonts.msBold[17].getWidth(Main.cheatProfile.getName())), 20.0F, -1);
      sertyo.events.utility.font.Fonts.msBold[16].drawString("UID: " + Main.cheatProfile.getId(), (float)(scaledWidth - 50 - sertyo.events.utility.font.Fonts.msBold[16].getWidth("UID: " + Main.cheatProfile.getId())), 30.0F, (new Color(120, 120, 120)).getRGB());
      Fonts.tenacityBold35.drawGradientString(Main.name, (double)((float)scaledWidth / 2.0F - 14.0F), (double)((float)scaledHeight / 2.0F - 65.0F), color, color2);
      super.render(ms, mouse.getX(), mouse.getY(), partialTicks);
      Main.getInstance().getScaleMath().popScale();
   }
   public static void openLink(String url) throws IOException, URISyntaxException {
      if (Desktop.isDesktopSupported()) {
         Desktop desktop = Desktop.getDesktop();
         if (desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(new URI(url));
         } else {
            System.out.println("�������� �������� �� ��������������.");
         }
      } else {
         System.out.println("������� �� ��������������.");
      }
   }
}
