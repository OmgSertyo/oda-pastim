package sertyo.events.ui.menu.main;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CVec4 {
    public float x;
    public float y;
    public float width;
    public float height;

    public CVec4(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public CVec4(CVec2 xy, CVec2 wh) {
        this.x = xy.x;
        this.y = xy.y;
        this.width = wh.w();
        this.height = wh.h();
    }

    public CVec4(float x, float y, CVec2 wh) {
        this.x = x;
        this.y = y;
        this.width = wh.w();
        this.height = wh.h();
    }


    public CVec4() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public float x() {
        return x;
    }

    public float y() {
        return y;
    }

    public float w() {
        return width;
    }

    public float h() {
        return height;
    }

    public float r() {
        return x;
    }

    public float g() {
        return y;
    }

    public float b() {
        return width;
    }

    public float a() {
        return height;
    }

}