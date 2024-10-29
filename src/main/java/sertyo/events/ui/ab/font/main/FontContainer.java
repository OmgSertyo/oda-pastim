package sertyo.events.ui.ab.font.main;



import sertyo.events.ui.ab.font.api.FontRenderer;
import sertyo.events.ui.ab.font.utils.InstallUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FontContainer {
    private final HashMap<Integer, FontRenderer> MAP = new HashMap<>();
    public ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final String link;

    public FontContainer(String link) {
        this.link = link;
        for (int i = 0; i < 30; i++) {
            MAP.put(i, new FontRenderer(new Font[]{InstallUtils.installFont(link, i)}, i));
        }
    }

    public FontRenderer get(int size) {
        try {
            return MAP.get(size);
        } catch (Exception e) {
            return MAP.get(0);
        }
    }
}