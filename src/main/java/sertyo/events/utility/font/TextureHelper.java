package sertyo.events.utility.font;

import sertyo.events.utility.render.fonts.CFont;

import java.awt.image.BufferedImage;
public final class TextureHelper {

	public static int loadTexture(BufferedImage image) {
        return CFont.loadTexture(image);
	}
	
}
