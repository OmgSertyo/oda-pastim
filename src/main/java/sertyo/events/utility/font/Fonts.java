package sertyo.events.utility.font;

import lombok.SneakyThrows;
import sertyo.events.Main;
import sertyo.events.utility.font.common.Lang;
import sertyo.events.utility.font.styled.StyledFont;


public class Fonts {
    public static final String FONT_DIR = "/assets/minecraft/neiron/font/";

    public static volatile StyledFont[] minecraft = new StyledFont[24];
    public static volatile StyledFont[] icomoon = new StyledFont[30];
    public static volatile StyledFont[] verdana = new StyledFont[24];
    public static volatile StyledFont[] gilroyBold = new StyledFont[24];
    public static volatile StyledFont[] msBold = new StyledFont[29];
    public static volatile StyledFont[] nlicon = new StyledFont[24];
    public static volatile StyledFont[] msMedium = new StyledFont[24];
    public static volatile StyledFont[] msLight = new StyledFont[24];
    public static volatile StyledFont[] msRegular = new StyledFont[24];
    public static volatile StyledFont[] msSemiBold = new StyledFont[31];

    public static volatile StyledFont[] gilroy = new StyledFont[24];
    public static volatile StyledFont[] sora = new StyledFont[24];
    public static volatile StyledFont[] woveline = new StyledFont[24];
    public static volatile StyledFont[] icons = new StyledFont[24];
    public static volatile StyledFont[] configIcon = new StyledFont[24];

    public static volatile StyledFont[] icons1 = new StyledFont[131];

    @SneakyThrows
    public static void init() {
        long time = System.currentTimeMillis();

        minecraft[8] = new StyledFont("mc.ttf", 8, 0.0f, 0.0f, 0.0f, false, Lang.ENG_RU);
        woveline[19] = new StyledFont("woveline.otf", 19, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        icons1[130] = new StyledFont("guiicon.ttf", 130, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        for (int i = 8; i < 24;i++) {
            icons1[i] = new StyledFont("guiicon.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 24;i++) {
            icons[i] = new StyledFont("icons.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 24;i++) {
            nlicon[i] = new StyledFont("icons.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 16;i++) {
            verdana[i] = new StyledFont("verdana.ttf", i, 0.0f, 0.0f, 0.0f, false, Lang.ENG_RU);
        }
        for (int i = 10; i < 23;i++) {
            sora[i] = new StyledFont("sora.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 10; i < 30;i++) {
            icomoon[i] = new StyledFont("icomoon.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 10; i < 23;i++) {
            configIcon[i] = new StyledFont("Glyphter.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 10; i < 23;i++) {
            gilroyBold[i] = new StyledFont("gilroy-bold.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 10; i < 24;i++) {
            gilroy[i] = new StyledFont("gilroy.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }

        for (int i = 8; i < 29;i++) {
            msBold[i] = new StyledFont("Montserrat-Bold.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 24;i++) {
            msLight[i] = new StyledFont("Montserrat-Light.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 24;i++) {
            msMedium[i] = new StyledFont("Montserrat-Medium.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 24;i++) {
            msRegular[i] = new StyledFont("Montserrat-Regular.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        for (int i = 8; i < 31;i++) {
            msSemiBold[i] = new StyledFont("Montserrat-SemiBold.ttf", i, 0.0f, 0.0f, 0.0f, true, Lang.ENG_RU);
        }
        System.out.println("Шрифты загрузились за: " + (System.currentTimeMillis() - time) + " миллисекунды!");

        //fontThread.shutdown();
    }
}