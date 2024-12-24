//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sertyo.events.ui.menu.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import sertyo.events.Main;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.render.ColorUtility;
import sertyo.events.utility.render.RenderUtility;
import sertyo.events.utility.render.animation.Direction;
import sertyo.events.utility.render.animation.impl.DecelerateAnimation;

import java.awt.Color;

public class CustomButton extends Button {
   public DecelerateAnimation animation;

   public CustomButton(int x, int y, int width, int height, ITextComponent title, net.minecraft.client.gui.widget.button.Button.IPressable pressedAction) {
      super(x, y, width, height, title, pressedAction, field_238486_s_);
      this.animation = new DecelerateAnimation(300, 1.0F, Direction.BACKWARDS);
   }

   public CustomButton(int x, int y, int width, int height, ITextComponent title, net.minecraft.client.gui.widget.button.Button.IPressable pressedAction, net.minecraft.client.gui.widget.button.Button.ITooltip onTooltip) {
      super(x, y, width, height, title, pressedAction);
      this.onPress = pressedAction;
      this.onTooltip = onTooltip;
      this.animation = new DecelerateAnimation(300, 1.0F, Direction.BACKWARDS);
   }
   @Override
   public void renderButton(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
      if (this.visible) {
         int color = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[0].getRGB();
         int color2 = Main.getInstance().getThemeManager().getCurrentStyleTheme().getColors()[1].getRGB();
         this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
         this.animation.setDirection(this.hovered ? Direction.FORWARDS : Direction.BACKWARDS);
         //GlStateManager.enableBlend();
        // RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
         RenderUtility.Render2D.drawRoundedRect((float)this.x, (float)this.y, (float)this.width, (float)this.height, 5.0F, (new Color(30, 30, 30)).getRGB());
         if (this.hovered) {
            RenderUtility.Render2D.drawRoundedGradientRect((float)this.x, (float)this.y, (float)this.width, (float)this.height, 5.0F, 1.0F, ColorUtility.setAlpha(color, this.animation.getOutput()), ColorUtility.setAlpha(color, this.animation.getOutput()), ColorUtility.setAlpha(color2, this.animation.getOutput()), ColorUtility.setAlpha(color2, this.animation.getOutput()));
            RenderUtility.Render2D.drawRoundedRect((float)(this.x + 1), (float)(this.y + 1), (float)(this.width - 2), (float)(this.height - 2), 4.0F, (new Color(30, 30, 30)).getRGB());
            Fonts.msBold[16].drawCenteredString(ms, ColorUtility.gradient(this.getMessage().getString(), color, color2), (float)this.x + (float)this.width / 2.0F, (float)this.y + (float)(this.height - 8) / 2.0F + 1.0F, Color.white.getRGB());
         } else {
            Fonts.msBold[16].drawCenteredString(this.getMessage().getString(), (float)this.x + (float)this.width / 2.0F, (float)this.y + (float)(this.height - 8) / 2.0F + 1.0F, Color.white.getRGB());
         }
      }

   }
}
