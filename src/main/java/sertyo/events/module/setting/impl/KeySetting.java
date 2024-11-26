package sertyo.events.module.setting.impl;

import lombok.Setter;
import sertyo.events.module.setting.Setting;

import java.util.function.Supplier;

public class KeySetting extends Setting {
    @Setter
    private int key;
    public boolean state;

    public KeySetting(String name, int defaultKey) {
        super(name, true);
        this.state = true;
        key = defaultKey;
        this.setVisible(() -> {
            return true;
        });
    }

    public KeySetting(String name, int defaultKey, Supplier<Boolean> visible) {
        super(name, true);
        this.state = true;
        key = defaultKey;
        this.setVisible(visible);
    }

    public int get() {
        return this.key;
    }
}
