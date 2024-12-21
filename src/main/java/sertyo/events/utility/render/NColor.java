package sertyo.events.utility.render;


import java.awt.*;

public class NColor extends Color {


    public NColor(int r, int g, int b) {
        super(r, g, b);
    }
    public NColor(int r, int g, int b,int a) {
        super(r, g, b,a);
    }

    public NColor(float r, float g, float b) {
        super(
                r,
                g,
                b);
    }
    public static float clamp(float num, float min, float max)
    {
        return Math.min(Math.max(num, min), max);
    }
    public NColor(float r, float g, float b,float a) {
        super(clamp(r,0,1)
                ,
                clamp(g,0,1),
                clamp(b,0,1),
                clamp(a,0,1));
    }

    public NColor(int color) {
        super(color);
    }

    public NColor darker(float factor) {
        return new NColor(Math.max((int)(getRed()  *factor), 0),
                Math.max((int)(getGreen() * factor), 0),
                Math.max((int)(getBlue() * factor), 0),
                getAlpha());
    }
    public NColor withCustomAlpha(int alpha) {
        return new NColor(this.getRed(),this.getGreen(),this.getBlue(),alpha);
    }

    public NColor withCustomAlpha(float alpha) {
        return new NColor(this.getRed() / 255f,this.getGreen() / 255f,this.getBlue() / 255f,alpha);
    }

    public NColor withCustomAnimatedAlpha(float alpha) {
        return new NColor(this.getRed() / 255f,this.getGreen() / 255f,this.getBlue() / 255f,alpha * (this.getAlpha() / 255f));
    }

    public NColor brighter(float factor) {
        int r = getRed();
        int g = getGreen();
        int b = getBlue();
        int alpha = getAlpha();

        int i = (int) (1.0 / (1.0 - factor));
        if (r == 0 && g == 0 && b == 0) {
            return new NColor(i, i, i, alpha);
        }
        if (r > 0 && r < i) r = i;
        if (g > 0 && g < i) g = i;
        if (b > 0 && b < i) b = i;

        return new NColor(Math.min((int) (r / factor), 255),
                Math.min((int) (g / factor), 255),
                Math.min((int) (b / factor), 255),
                alpha);
    }

    public static NColor fromColor(Color color) {
        return new NColor(color.getRed(),color.getGreen(),color.getBlue(), color.getAlpha());
    }

    public static NColor create(Color color) {
        return new NColor(color.getRed(),color.getGreen(),color.getBlue(), color.getAlpha());
    }
}
