package sertyo.events.ui.csgui.spotify;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.StringTextComponent;
import me.sertyo.j2c.J2c;
import sertyo.events.ui.csgui.CsGui;
import sertyo.events.utility.font.Fonts;
import sertyo.events.utility.render.ColorUtil;
import sertyo.events.utility.render.GaussianBlur;
import sertyo.events.utility.render.RenderUtil;
import sertyo.events.utility.render.animation.Animation;
import sertyo.events.utility.render.animation.impl.DecelerateAnimation;


import java.awt.*;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static sertyo.events.ui.csgui.spotify.SpotifyLocalAPI.getCurrentTrack;
import static sertyo.events.utility.Utility.mc;

@J2c
public class Spotify extends Screen {
    private Vector2f position = new Vector2f(0, 0);
    public static Vector2f size = new Vector2f(500, 400);
    public static boolean isConnected;
    public static String[] trackInfo = new String[]{"Сейчас ничего не играет", "N/A"};
    private int textureId = -1;
    public static void startTrackUpdate() throws IOException {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            try {
                trackInfo = getCurrentTrack(SpotifyLocalAPI.accessToken);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.SECONDS);
    }
    public Spotify() {
        super(new StringTextComponent("SpotifyConnect"));
    }

    @Override
    protected void init() {
        super.init();
        size = new Vector2f(450, 350);
        position = new Vector2f(mc.getMainWindow().scaledWidth / 2f - size.x / 2f, mc.getMainWindow().scaledHeight / 2f - size.y / 2f);
            try {
                startTrackUpdate();
            } catch (IOException e) {
                throw new RuntimeException(e);
        }
        if (isConnected) {
            try {
                // ????????? ?????????? ? ?????
                String[] trackInfo = getCurrentTrack(SpotifyLocalAPI.accessToken);
                String coverUrl = trackInfo[2]; // URL ???????

                // ???????? ????????
                textureId = RenderUtil.Render2D.downloadImage(coverUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private final Animation animation = new DecelerateAnimation(300, 1.0F);

    private void render0(int n, MatrixStack matrixStack, int x, int y, int width, int height) {
        //???? ????
        GaussianBlur.startBlur();
        RenderUtil.Render2D.drawRoundedRect(x, y, width, height, 6.5f, ColorUtil.rgba(20, 20, 20, 200));
        GaussianBlur.endBlur(20, 2);
        RenderUtil.Render2D.drawRoundedRect(x, y, width, height, 8.5f, n);
    }
    int n3 = ColorUtil.rgba(11, 11, 11, 130);
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (!isConnected) {
            RenderUtil.scale(position.x - 20, position.y + 20, this.animation.getOutput(), () -> this.render0(n3, matrixStack, (int) position.x - 20, (int) position.y + 20, (int) size.x + 13, (int) size.y - 100));
            RenderUtil.Render2D.drawRoundedRect(position.x - 20 + 50, position.y + 215, size.x - 100, size.y - 300, 4, ColorUtil.rgba(11, 11, 11, 255));
            Fonts.gilroy[16].drawString(matrixStack, "Привет если ты зашёл сюда случайно нажми кнопку " + "ESC а если нет то следуй инструкциям 1 нажми на", position.x - 20 + 4, position.y + 24, -1);
            Fonts.gilroy[16].drawString(matrixStack, " кнопку внизу дальше у тебя откроется сайт там подтверди авторизацию в приложение PS это безопастно", position.x - 20 + 2, position.y + 35, -1);
            Fonts.gilroy[16].drawString(matrixStack, " ибо без этого не будет работать апи если у тебя открылся не основной браузер выполни вход в аккаунт", position.x - 20 + 2, position.y + 45, -1);
            Fonts.gilroy[16].drawString(matrixStack, " спотифая в браузере который открылся далее тебя ????????? ?? ???????? ? ??? ????? ???????? Authori-", position.x - 20 + 2, position.y + 55, -1);
            Fonts.gilroy[16].drawString(matrixStack, " zation code received. You can close this window. закрываешь браузер и перезаходишь в эту гуи и всё", position.x - 20 + 2, position.y + 66, -1);
            Fonts.msSemiBold[30].drawString(matrixStack, "Начать подключение spotify", position.x - 20 + 107, position.y + 77 + 158, -1);
        } else {
                int n2 = ColorUtil.rgba(11, 11, 11, 130);

                RenderUtil.scale(position.x - 20, position.y + 20, this.animation.getOutput(), () -> this.render0(n3, matrixStack, (int) position.x - 20, (int) position.y + 20, (int) size.x + 13, (int) size.y - 100));
                Fonts.gilroy[16].drawString(matrixStack, "???????? ??????? ????????? ??? ????????? ?????? ?????? ?????? ????????? ??????? ?????????? ???????", position.x - 20 + 4, position.y + 24, -1);
                RenderUtil.Render2D.drawRoundedCorner(position.x - 21, position.y + 214, size.x + 13, 55, 10.0f, n2, RenderUtil.Render2D.Corner.TOP);
                Fonts.gilroy[18].drawString(matrixStack, "Название песни: " + trackInfo[0], position.x + 28, position.y + 230, -1);
                Fonts.gilroy[18].drawString(matrixStack, "Автор(ы): " + trackInfo[1], position.x + 28, position.y + 240, -1);
                Fonts.gilroy[18].drawString(matrixStack, "Дата выхода: " + trackInfo[5], position.x + 28, position.y + 250, -1);
                Fonts.gilroy[16].drawString(matrixStack, trackInfo[3], position.x + 28, position.y + 260, -1);
                Fonts.gilroy[16].drawString(matrixStack, trackInfo[4], position.x + 32 + Fonts.gilroy[16].getWidth(trackInfo[3]) + size.x - 250, position.y + 260, -1);
                RenderUtil.Render2D.drawRoundedRect(position.x + 30 + Fonts.gilroy[16].getWidth(trackInfo[3]), position.y + 260, size.x - 250, 4, 2, new Color(241, 235, 235).getRGB());
           // Fonts.gilroy[20].drawString(matrixStack, SpotifyLocalAPI.getPlayPauseIcon(), position.x + 30 + Fonts.gilroy[16].getWidth(trackInfo[3]) + size.x - 250 + 30, position.y + 258, -1);

            GlStateManager.color4f(0, 0, 0, 0);
                GlStateManager.bindTexture(textureId);
                RenderUtil.Render2D.drawTexture(position.x - 14, position.y + 225, 40 - 2, 40, 4f, 1);
        }
    }
    private String trackProgress = "0:00";
    private String trackEndTime = "0:00";
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!isConnected) {
            if (RenderUtil.isInRegion(mouseX, mouseY, position.x - 20 + 50, position.y + 215, size.x - 100, size.y - 300)) {
                try {
                    SpotifyLocalAPI.main(new String[]{});
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("IOException occurred: " + e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Exception occurred: " + e.getMessage());
                }
            }
        } else {
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    @Override
    public void onClose() {
        super.onClose();
        CsGui.spotifymenu = false;
    }
}
