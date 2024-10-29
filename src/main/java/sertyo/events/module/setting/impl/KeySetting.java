package sertyo.events.module.setting.impl;

import sertyo.events.module.setting.Setting;

import java.util.function.Supplier;

public class KeySetting extends Setting {
    private int key;
    public boolean state;

    public KeySetting(String name, boolean state, int defaultKey) {
        super(name, state);
        this.state = state;
        key = defaultKey;
        this.setVisible(() -> {
            return true;
        });
    }

    public KeySetting(String name, boolean state, Supplier<Boolean> visible, int defaultKey) {
        super(name, state);
        this.state = state;
        key = defaultKey;
        this.setVisible(visible);
    }

    public float get() {
        return this.key;
    }
}
