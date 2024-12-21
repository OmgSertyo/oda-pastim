package sertyo.events.utility.misc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.SneakyThrows;
import net.minecraft.util.ResourceLocation;
import sertyo.events.Main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static sertyo.events.utility.Utility.mc;

public class Localization {
    static Gson gson = new Gson();
    private static final Map<Language, Map<String, String>> cache = new ConcurrentHashMap<>();

    public static String get(String key) {
        Language currentLanguage = Main.getInstance().getLanguage();

        Map<String, String> translations = cache.computeIfAbsent(currentLanguage, Localization::loadTranslations);
        return translations.getOrDefault(key, key);
    }

    @SneakyThrows
    private static Map<String, String> loadTranslations(Language language) {
        ResourceLocation identifier = new ResourceLocation("neiron/translations/" + language.getFile() + ".json");

        InputStream stream = mc.getResourceManager()
                .getResource(identifier)
                .getInputStream();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, StandardCharsets.UTF_8)
        );

        return gson.fromJson(reader, new TypeToken<Map<String, String>>() {}.getType());
    }
}
