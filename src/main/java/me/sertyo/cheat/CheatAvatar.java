package me.sertyo.cheat;

import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import net.coobird.thumbnailator.Thumbnails;
import net.minecraft.util.ResourceLocation;
import sertyo.events.Main;


public class CheatAvatar {
    private final File avatarFile;
    @Getter
    private ResourceLocation namespaced;
    private final int imageSize;
    @Getter
    @Setter
    public static CheatAvatar cheatAvatar;

    public CheatAvatar(File avatarFile, int imageSize) {
        this.imageSize = imageSize;
        this.avatarFile = avatarFile;
    }

    public void create() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(Files.readAllBytes(Paths.get(avatarFile.getAbsolutePath())));
        BufferedImage bufferedImage = Thumbnails.of(inputStream).size(imageSize, imageSize).asBufferedImage();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();

        namespaced = new ResourceLocation(() -> bytes);
    }

    public static ResourceLocation getAvatarResource() {
        if(cheatAvatar == null) {
            cheatAvatar = new CheatAvatar(Main.cheatProfile.getAvatarFile(), 60);
            try {
                cheatAvatar.create();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cheatAvatar.getNamespaced() == null
                ? new ResourceLocation("goyda/logo.png") // Если не удалось ставим другую какуюто
                : cheatAvatar.getNamespaced();
    }
}
