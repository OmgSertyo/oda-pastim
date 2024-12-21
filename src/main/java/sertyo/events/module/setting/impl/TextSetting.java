package sertyo.events.module.setting.impl;


import sertyo.events.module.setting.Setting;

import java.util.function.Supplier;

public class TextSetting extends Setting {
    public String text;
    public boolean state;


    public TextSetting(String name, String text) {
        super(name, true);
        this.state = true;
        this.text = text;
        this.setVisible(() -> {
            return true;
        });
    }

    public TextSetting(String name, String text, Supplier<Boolean> visible) {
        super(name, true);
        this.state = true;
        this.text = text;
        this.setVisible(visible);
    }

    public String get() {
        return this.text;
    }
}
