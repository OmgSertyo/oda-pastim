package sertyo.events.ui.ab.font.api;

import com.google.common.base.Preconditions;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import it.unimi.dsi.fastutil.chars.Char2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import sertyo.events.ui.ab.font.main.IFont;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static sertyo.events.utility.Utility.mc;

public class FontRenderer  implements Closeable {
    private static final Random RND = new Random();
    private static final Char2IntArrayMap colorCodes = new Char2IntArrayMap() {{
        put('0', 0x000000);
        put('1', 0x0000AA);
        put('2', 0x00AA00);
        put('3', 0x00AAAA);
        put('4', 0xAA0000);
        put('5', 0xAA00AA);
        put('6', 0xFFAA00);
        put('7', 0xAAAAAA);
        put('8', 0x555555);
        put('9', 0x5555FF);
        put('A', 0x55FF55);
        put('B', 0x55FFFF);
        put('C', 0xFF5555);
        put('D', 0xFF55FF);
        put('E', 0xFFFF55);
        put('F', 0xFFFFFF);
    }};
    private static final Object2ObjectArrayMap<ResourceLocation, ObjectList<DrawEntry>> GLYPH_PAGE_CACHE = new Object2ObjectArrayMap<>();
    private final float originalSize;
    public final ObjectList<GlyphMap> maps = new ObjectArrayList<>();
    private final Char2ObjectArrayMap<Glyph> allGlyphs = new Char2ObjectArrayMap<>();
    private int scaleMul = 0;
    private Font[] fonts;
    private int previousGameScale = -1;
    private final int charsPerPage;
    private final int padding;
    private static boolean color;
    public static boolean replace = true;

    public static void color(boolean set) {
        color = set;
    }

    public FontRenderer(Font[] fonts, float sizePx, int charactersPerPage, int paddingBetweenCharacters) {
        Preconditions.checkArgument(fonts.length > 0, "fonts.length == 0");
        Preconditions.checkArgument(charactersPerPage > 4, "Unreasonable charactersPerPage count");
        Preconditions.checkArgument(paddingBetweenCharacters > 0, "paddingBetweenCharacters > 0");
        this.originalSize = sizePx;
        this.charsPerPage = charactersPerPage;
        this.padding = paddingBetweenCharacters;
        init(fonts, sizePx);
    }

    public FontRenderer(Font[] fonts, float sizePx) {
        this(fonts, sizePx, 256, 5);
    }

    private static int floorNearestMulN(int x, int n) {
        return n * (int) Math.floor((double) x / (double) n);
    }

    public static String stripControlCodes(String text) {
        char[] chars = text.toCharArray();
        StringBuilder f = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '§') {
                i++;
                continue;
            }
            f.append(c);
        }
        return f.toString();
    }

    private void sizeCheck() {
        int gs = (int) Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
        if (gs != this.previousGameScale) {
            close();
            init(this.fonts, this.originalSize);
        }
    }

    private void init(Font[] fonts, float sizePx) {
        this.previousGameScale = (int) Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
        this.scaleMul = this.previousGameScale;
        this.fonts = new Font[fonts.length];
        for (int i = 0; i < fonts.length; i++) {
            this.fonts[i] = fonts[i].deriveFont(sizePx * this.scaleMul);
        }
    }

    private GlyphMap generateMap(char from, char to) {
        GlyphMap gm = new GlyphMap(from, to, this.fonts, randomIdentifier(), padding);
        maps.add(gm);
        return gm;
    }

    private static String randomString() {
        return String.valueOf(RND.nextInt(10000));
    }

    public static ResourceLocation randomIdentifier() {
        return new ResourceLocation("temp", randomString());
    }

    private Glyph locateGlyph0(char glyph) {
        for (GlyphMap map : maps) {
            if (map.contains(glyph)) {
                return map.getGlyph(glyph);
            }
        }
        int base = floorNearestMulN(glyph, charsPerPage);
        GlyphMap glyphMap = generateMap((char) base, (char) (base + charsPerPage));
        return glyphMap.getGlyph(glyph);
    }

    private Glyph locateGlyph1(char glyph) {
        return allGlyphs.computeIfAbsent(glyph, this::locateGlyph0);
    }

    public void drawString(MatrixStack stack, String s, float x, float y, float r, float g, float b, float a) {
        drawString(stack, s, x, y, r, g, b, a, false);
    }

    public static int[] RGBIntToRGB(int in) {
        int red = in >> 8 * 2 & 0xFF;
        int green = in >> 8 & 0xFF;
        int blue = in & 0xFF;
        return new int[]{red, green, blue};
    }

    public void drawString(MatrixStack stack, String s, float x, float y, Color color, boolean shadow) {
        drawString(stack, s, x, y, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F, shadow);
    }

    public void drawString(MatrixStack stack, String s, float x, float y, Color color) {
        drawString(stack, s, x, y, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, color.getAlpha() / 255F, false);
    }

    public static double roundToDecimal(double n, int point) {
        if (point == 0) {
            return Math.floor(n);
        }
        double factor = Math.pow(10, point);
        return Math.round(n * factor) / factor;
    }


    public void drawString(MatrixStack stack, String s, float x, float y, float r, float g, float b, float a, boolean shadow) {
        sizeCheck();
        float r2 = r, g2 = g, b2 = b;
        stack.push();
        stack.scale(1f, 1f, 1f);
        stack.translate(roundToDecimal(x, 1), roundToDecimal(y, 1), 0);
        stack.scale(1f / this.scaleMul, 1f / this.scaleMul, 1f);

        RenderSystem.enableBlend();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);

      //  GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
      //  GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//
        char[] chars = s.toCharArray();
        float xOffset = 0;
        float yOffset = 0;
        boolean inSel = false;
        boolean hasStyle = false;
        int lineStart = 0;
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '█') continue;
            if (inSel) {
                inSel = false;
                char c1 = Character.toUpperCase(c);
                if (colorCodes.containsKey(c1)) {
                    int ii = colorCodes.get(c1);
                    int[] col = RGBIntToRGB(ii);
                    r2 = col[0] / 255f;
                    g2 = col[1] / 255f;
                    b2 = col[2] / 255f;
                    hasStyle = true;
                } else if (c1 == 'R') {
                    r2 = r;
                    g2 = g;
                    b2 = b;
                    hasStyle = false;
                }
                continue;
            }
            if (c == '§') {
                inSel = true;
                continue;
            } else if (c == '\n') {
                yOffset += getStringHeight(s.substring(lineStart, i)) * scaleMul;
                xOffset = 0;
                lineStart = i + 1;
                continue;
            }
            Glyph glyph = locateGlyph1(c);
            if (glyph.value() != ' ' && glyph.value() != '█') {
                ResourceLocation i1 = glyph.owner().bindToTexture;

                DrawEntry entry = new DrawEntry(xOffset, yOffset, r2, g2, b2, glyph);
                entry.hasStyle = hasStyle;
                GLYPH_PAGE_CACHE.computeIfAbsent(i1, integer -> new ObjectArrayList<>()).add(entry);
            }
            xOffset += glyph.width();
        }

        for (ResourceLocation identifier : GLYPH_PAGE_CACHE.keySet()) {
            mc.getTextureManager().bindTexture(identifier);

            List<DrawEntry> objects = GLYPH_PAGE_CACHE.get(identifier);

         //   bb.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            for (DrawEntry object : objects) {
                float xo = object.atX;
                float yo = object.atY;

                Color shadowC = IFont.getShadowColor(new Color(object.r, object.g, object.b, a));

                float cr = object.r;
                float cg = object.g;
                float cb = object.b;

                if (shadow && !object.hasStyle) {
                    cr = shadowC.getRed() / 255F;
                    cg = shadowC.getGreen() / 255F;
                    cb = shadowC.getBlue() / 255F;
                    a = shadowC.getAlpha() / 255F;
                }

                Glyph glyph = object.toDraw;
                GlyphMap owner = glyph.owner();

                float w = glyph.width();
                float h = glyph.height();

                float u1 = (float) glyph.u() / owner.width;
                float v1 = (float) glyph.v() / owner.height;
                float u2 = (float) (glyph.u() + glyph.width()) / owner.width;
                float v2 = (float) (glyph.v() + glyph.height()) / owner.height;
                stack.push();
                stack.translate(xo, yo, 0);
                RenderSystem.color4f(cr, cg, cb, a);
                drawTexturedQuad(stack.getLast().getMatrix(), 0, w, 0, h, 0, u1, u2, v1, v2);
                RenderSystem.color4f(1f, 1f, 1f, 1f);
                stack.pop();
               //bb.vertex(mat, xo + 0, yo + h, 0).texture(u1, v2).color(cr, cg, cb, a).next();
               //bb.vertex(mat, xo + w, yo + h, 0).texture(u2, v2).color(cr, cg, cb, a).next();
               //bb.vertex(mat, xo + w, yo + 0, 0).texture(u2, v1).color(cr, cg, cb, a).next();
               //bb.vertex(mat, xo + 0, yo + 0, 0).texture(u1, v1).color(cr, cg, cb, a).next();
            }

          //  Tessellator.getInstance().draw();
        }

        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        stack.pop();
        GLYPH_PAGE_CACHE.clear();
    }

    private static void drawTexturedQuad(Matrix4f matrices, float x0, float x1, float y0, float y1, int z, float u0, float u1, float v0, float v1) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferBuilder.pos(matrices, x0, y1, (float)z).tex(u0, v1).endVertex();
        bufferBuilder.pos(matrices, x1, y1, (float)z).tex(u1, v1).endVertex();
        bufferBuilder.pos(matrices, x1, y0, (float)z).tex(u1, v0).endVertex();
        bufferBuilder.pos(matrices, x0, y0, (float)z).tex(u0, v0).endVertex();
        Tessellator.getInstance().draw();
    }

    public float getStringWidth(String text) {
        char[] c = stripControlCodes(text).toCharArray();
        float currentLine = 0;
        float maxPreviousLines = 0;
        for (char c1 : c) {
            if (c1 == '█') continue;
            if (c1 == '\n') {
                maxPreviousLines = Math.max(currentLine, maxPreviousLines);
                currentLine = 0;
                continue;
            }
            Glyph glyph = locateGlyph1(c1);
            currentLine += glyph.width() / (float) this.scaleMul;
        }
        return Math.max(currentLine, maxPreviousLines);
    }

    public float getStringHeight(String text) {
        char[] c = stripControlCodes(text).toCharArray();
        if (c.length == 0) {
            c = new char[]{' '};
        }
        float currentLine = 0;
        float previous = 0;
        for (char c1 : c) {
            if (c1 == '█') continue;
            if (c1 == '\n') {
                if (currentLine == 0) {
                    // empty line, assume space
                    currentLine = locateGlyph1(' ').height() / (float) this.scaleMul;
                }
                previous += currentLine;
                currentLine = 0;
                continue;
            }
            Glyph glyph = locateGlyph1(c1);
            currentLine = Math.max(glyph.height() / (float) this.scaleMul, currentLine);
        }
        return currentLine + previous;
    }

    @Override
    public void close() {
        for (GlyphMap map : maps) {
            map.destroy();
        }
        maps.clear();
        allGlyphs.clear();
    }

    public class DrawEntry {
        float atX;
        float atY;
        float r;
        float g;
        float b;
        Glyph toDraw;
        boolean hasStyle;

        public DrawEntry(float atX, float atY, float r, float g, float b, Glyph toDraw) {
            this.atX = atX;
            this.atY = atY;
            this.r = r;
            this.g = g;
            this.b = b;
            this.toDraw = toDraw;
        }

        public float atX() {
            return atX;
        }

        public float atY() {
            return atY;
        }

        public float b() {
            return b;
        }

        public float g() {
            return g;
        }

        public float r() {
            return r;
        }
    }

    public class Glyph {
        int u;
        int v;
        int width;
        int height;
        char value;
        GlyphMap owner;

        public Glyph(int u, int v, int width, int height, char value, GlyphMap owner) {
            this.u = u;
            this.v = v;
            this.width = width;
            this.height = height;
            this.value = value;
            this.owner = owner;
        }

        public char value() {
            return value;
        }

        public GlyphMap owner() {
            return owner;
        }

        public int height() {
            return height;
        }

        public int u() {
            return u;
        }

        public int v() {
            return v;
        }

        public int width() {
            return width;
        }
    }


    public class GlyphMap {
        char fromIncl, toExcl;
        Font[] font;
        ResourceLocation bindToTexture;
        int pixelPadding;
        private final Char2ObjectArrayMap<Glyph> glyphs = new Char2ObjectArrayMap<>();
        int width, height;

        public GlyphMap(char fromIncl, char toExcl, Font[] font, ResourceLocation bindToTexture, int pixelPadding) {
            this.fromIncl = fromIncl;
            this.toExcl = toExcl;
            this.font = font;
            this.bindToTexture = bindToTexture;
            this.pixelPadding = pixelPadding;
        }

        boolean generated = false;

        public Glyph getGlyph(char c) {
            if (!generated) {
                generate();
            }
            return glyphs.get(c);
        }

        public void destroy() {
            Minecraft.getInstance().getTextureManager().deleteTexture(this.bindToTexture);
            this.glyphs.clear();
            this.width = -1;
            this.height = -1;
            generated = false;
        }

        public boolean contains(char c) {
            return c >= fromIncl && c < toExcl;
        }

        private Font getFontForGlyph(char c) {
            for (Font font1 : this.font) {
                if (font1.canDisplay(c)) {
                    return font1;
                }
            }
            return this.font[0];
        }

        public void generate() {
            if (generated) {
                return;
            }
            int range = toExcl - fromIncl - 1;
            int charsVert = (int) (Math.ceil(Math.sqrt(range)) * 1.5);
            glyphs.clear();
            int generatedChars = 0;
            int charNX = 0;
            int maxX = 0, maxY = 0;
            int currentX = 0, currentY = 0;
            int currentRowMaxY = 0;
            List<Glyph> glyphs1 = new ArrayList<>();
            AffineTransform af = new AffineTransform();
            FontRenderContext frc = new FontRenderContext(af, true, false);
            while (generatedChars <= range) {
                char currentChar = (char) (fromIncl + generatedChars);
                Font font = getFontForGlyph(currentChar);
                Rectangle2D stringBounds = font.getStringBounds(String.valueOf(currentChar), frc);

                int width = (int) Math.ceil(stringBounds.getWidth());
                int height = (int) Math.ceil(stringBounds.getHeight());
                generatedChars++;
                maxX = Math.max(maxX, currentX + width);
                maxY = Math.max(maxY, currentY + height);
                if (charNX >= charsVert) {
                    currentX = 0;
                    currentY += currentRowMaxY + pixelPadding;
                    charNX = 0;
                    currentRowMaxY = 0;
                }
                currentRowMaxY = Math.max(currentRowMaxY, height);
                glyphs1.add(new Glyph(currentX, currentY, width, height, currentChar, this));
                currentX += width + pixelPadding;
                charNX++;
            }
            BufferedImage bi = new BufferedImage(Math.max(maxX + pixelPadding, 1), Math.max(maxY + pixelPadding, 1),
                    BufferedImage.TYPE_INT_ARGB);
            width = bi.getWidth();
            height = bi.getHeight();
            Graphics2D g2d = bi.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2d.setColor(new Color(255, 255, 255, 0));
            g2d.fillRect(0, 0, width, height);
            g2d.setColor(Color.WHITE);

            for (Glyph glyph : glyphs1) {
                g2d.setFont(getFontForGlyph(glyph.value()));
                FontMetrics fontMetrics = g2d.getFontMetrics();
                g2d.drawString(String.valueOf(glyph.value()), glyph.u(), glyph.v() + fontMetrics.getAscent());
                glyphs.put(glyph.value(), glyph);
            }

            registerBufferedImageTexture(bindToTexture, bi);
            generated = true;
        }

        private static void registerBufferedImageTexture(ResourceLocation i, BufferedImage bi) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ImageIO.write(bi, "png", out);
                byte[] bytes = out.toByteArray();

                ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
                data.flip();
                DynamicTexture tex = new DynamicTexture(NativeImage.read(data));
                mc.execute(() -> mc.getTextureManager().loadTexture(i, tex));
            } catch (Exception e) {
                e.fillInStackTrace();
            }
        }
    }
}
