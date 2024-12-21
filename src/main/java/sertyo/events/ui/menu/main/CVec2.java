package sertyo.events.ui.menu.main;

import lombok.Getter;
import lombok.Setter;
import sertyo.events.ui.menu.main.CVec4;

@Getter
@Setter
public class CVec2 {
    public float x;
    public float y;

    public CVec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public CVec2() {
        this.x = 0;
        this.y = 0;
    }

    public CVec2(float q) {
        this.x = q;
        this.y = q;
    }

    /** get x and y from VEC4*/
    public CVec2(CVec4 q) {
        this.x = q.x;
        this.y = q.y;
    }
    public Float x() {
        return x;
    }

    public Float y() {
        return y;
    }
    public Float z() {
        return y;
    }


    public Float w() {
        return x;
    }

    public Float h() {
        return y;
    }

    public CVec2 mul(float x,float y) {
        return new CVec2(this.x * x, this.y * y);
    }

    public CVec2 add(float x,float y) {
        return new CVec2(this.x + x, this.y + y);
    }
    public CVec2 add(float f) {
        return new CVec2(this.x + f, this.y + f);
    }
    public CVec2 add(CVec2 f) {
        return new CVec2(this.x + f.x, this.y + f.y);
    }

    public CVec2 divide(float x, float y) {
        return new CVec2(this.x / cm(x), this.y / cm(y));
    }

    public CVec2 divide(float factor) {
        return new CVec2(this.x / cm(factor), this.y / cm(factor));
    }

    public float cm(float value) {
        return Math.max(0.001f, value);
    }

    public void update(CVec2 xy) {
        this.x = xy.x;
        this.y = xy.y;
    }

    public CVec2 seperate(CVec2 vector) {
        return new CVec2(this.x - vector.x, this.y - vector.y);
    }

    public void update(float x, float y) {
        update(new CVec2(x, y));
    }
    public boolean isShit() {
        return x == -1;
    }
}