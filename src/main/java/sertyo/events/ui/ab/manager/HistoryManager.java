package sertyo.events.ui.ab.manager;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager {
    @Getter
    private static final List<HistoryItem> historyItem = new ArrayList<>();

    public static void add(HistoryItem item) {
        historyItem.add(item);
    }
}