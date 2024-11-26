package sertyo.events.utility.render.fonts;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class CFont {
   private final float imgSize = 512.0F;
   protected CharData[] charData = new CharData[1105];
   protected Font font;
   protected boolean antiAlias;
   protected boolean fractionalMetrics;
   protected int fontHeight = -1;
   protected int charOffset = 0;
   protected DynamicTexture tex;

   public CFont(Font font, boolean antiAlias, boolean fractionalMetrics) {
      this.font = font;
      this.antiAlias = antiAlias;
      this.fractionalMetrics = fractionalMetrics;
      this.tex = this.setupTexture(font, antiAlias, fractionalMetrics, this.charData);
   }
   protected DynamicTexture setupTexture(Font font, boolean antiAlias, boolean fractionalMetrics, CFont.CharData[] chars) {
      BufferedImage img = this.generateFontImage(font, antiAlias, fractionalMetrics, chars);

      try {
         // Создание NativeImage на основе BufferedImage
         NativeImage nativeImage = new NativeImage(img.getWidth(), img.getHeight(), true);
         for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
               int rgba = img.getRGB(x, y); // Получаем RGBA пиксель из BufferedImage
               nativeImage.setPixelRGBA(x, y, rgba); // Устанавливаем пиксель в NativeImage
            }
         }

         return new DynamicTexture(nativeImage);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }
   public static int loadTexture(BufferedImage image) {
      int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
      ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);

      try {
         for (int pixel : pixels) {
            buffer.put((byte)((pixel >> 16) & 0xFF));
            buffer.put((byte)((pixel >> 8) & 0xFF));
            buffer.put((byte)(pixel & 0xFF));
            buffer.put((byte)((pixel >> 24) & 0xFF));
         }
         buffer.flip();
      } catch (BufferOverflowException | ReadOnlyBufferException ex) {return -1;}

      int textureID = GlStateManager.genTexture();
      GlStateManager.bindTexture(textureID);
      GlStateManager.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
      GlStateManager.texParameter(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
      GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, buffer);
      GlStateManager.bindTexture(0);

      return textureID;
   }
   protected BufferedImage generateFontImage(Font font, boolean antiAlias, boolean fractionalMetrics, CharData[] chars) {
      this.getClass();
      int imgSize = 512;
      BufferedImage bufferedImage = new BufferedImage(imgSize, imgSize, 2);
      Graphics2D g = (Graphics2D)bufferedImage.getGraphics();
      g.setFont(font);
      g.setColor(new Color(255, 255, 255, 0));
      g.fillRect(0, 0, imgSize, imgSize);
      g.setColor(Color.WHITE);
      g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, fractionalMetrics ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
      g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, antiAlias ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
      FontMetrics fontMetrics = g.getFontMetrics();
      int charHeight = 0;
      int positionX = 0;
      int positionY = 1;

      for(int i = 0; i <= chars.length; ++i) {
         char ch = (char)i;
         if (ch > 1039 && ch < 1104 || ch < 256) {
            CharData charData = new CharData();
            Rectangle2D dimensions = fontMetrics.getStringBounds(String.valueOf(ch), g);
            charData.width = dimensions.getBounds().width + 8;
            charData.height = dimensions.getBounds().height;
            if (positionX + charData.width >= imgSize) {
               positionX = 0;
               positionY += charHeight;
               charHeight = 0;
            }

            if (charData.height > charHeight) {
               charHeight = charData.height;
            }

            charData.storedX = positionX;
            charData.storedY = positionY;
            if (charData.height > this.fontHeight) {
               this.fontHeight = charData.height;
            }

            chars[i] = charData;
            g.drawString(String.valueOf(ch), positionX + 2, positionY + fontMetrics.getAscent());
            positionX += charData.width;
         }
      }

      return bufferedImage;
   }

   public void drawChar(CharData[] chars, char c, float x, float y) throws ArrayIndexOutOfBoundsException {
      try {
         this.drawQuad(x, y, (float)chars[c].width, (float)chars[c].height, (float)chars[c].storedX, (float)chars[c].storedY, (float)chars[c].width, (float)chars[c].height);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }

   protected void drawQuad(float x, float y, float width, float height, float srcX, float srcY, float srcWidth, float srcHeight) {
      try {
         float renderSRCX = srcX / 512.0F;
         float renderSRCY = srcY / 512.0F;
         float renderSRCWidth = srcWidth / 512.0F;
         float renderSRCHeight = srcHeight / 512.0F;
         GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
         GL11.glVertex2d((double)(x + width), (double)y);
         GL11.glTexCoord2f(renderSRCX, renderSRCY);
         GL11.glVertex2d((double)x, (double)y);
         GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
         GL11.glVertex2d((double)x, (double)(y + height));
         GL11.glTexCoord2f(renderSRCX, renderSRCY + renderSRCHeight);
         GL11.glVertex2d((double)x, (double)(y + height));
         GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY + renderSRCHeight);
         GL11.glVertex2d((double)(x + width), (double)(y + height));
         GL11.glTexCoord2f(renderSRCX + renderSRCWidth, renderSRCY);
         GL11.glVertex2d((double)(x + width), (double)y);
      } catch (Exception var13) {
         var13.printStackTrace();
      }

   }

   public int getStringHeight(String text) {
      return this.getFontHeight();
   }

   public int getFontHeight() {
      return (this.fontHeight - 8) / 2;
   }

   public int getStringWidth(String text) {
      int width = 0;
      char[] var3 = text.toCharArray();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         char c = var3[var5];
         if (c < this.charData.length) {
            width += this.charData[c].width - 8 + this.charOffset;
         }
      }

      return width / 2;
   }

   public boolean isAntiAlias() {
      return this.antiAlias;
   }

   public void setAntiAlias(boolean antiAlias) {
      if (this.antiAlias != antiAlias) {
         this.antiAlias = antiAlias;
         this.tex = this.setupTexture(this.font, antiAlias, this.fractionalMetrics, this.charData);
      }

   }

   public boolean isFractionalMetrics() {
      return this.fractionalMetrics;
   }

   public void setFractionalMetrics(boolean fractionalMetrics) {
      if (this.fractionalMetrics != fractionalMetrics) {
         this.fractionalMetrics = fractionalMetrics;
         this.tex = this.setupTexture(this.font, this.antiAlias, fractionalMetrics, this.charData);
      }

   }

   public Font getFont() {
      return this.font;
   }

   public void setFont(Font font) {
      this.font = font;
      this.tex = this.setupTexture(font, this.antiAlias, this.fractionalMetrics, this.charData);
   }

   protected static class CharData {
      public int width;
      public int height;
      public int storedX;
      public int storedY;
   }
}
