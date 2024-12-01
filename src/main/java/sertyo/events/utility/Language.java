package sertyo.events.utility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@Getter
@RequiredArgsConstructor
public enum Language {
    UKR("uk_UK"),
    RUS("ru_RU");

    private final String file;
    private final HashMap<String, String> strings = new HashMap<>();
}