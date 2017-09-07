package de.sscholz.gfx.texture;

import de.sscholz.math.Color;
import de.sscholz.math.MathUtil;

public class Pixel {

    public byte r, g, b, a = (byte) 255;

    public Pixel(byte r, byte g, byte b, byte a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Pixel(byte r, byte g, byte b) {
        this(r, g, b, (byte) 255);
    }

    public Pixel clone() {
        return new Pixel(r, g, b, a);
    }

    @Override
    public String toString() {
        return "(" + MathUtil.unsignByte(r) + "," + MathUtil.unsignByte(g) + "," + MathUtil.unsignByte(b) + "," + MathUtil.unsignByte(a) + ")";
    }

    public Color toColor() {
        return new Color(MathUtil.byteToDouble(r), MathUtil.byteToDouble(g), MathUtil.byteToDouble(b), MathUtil.byteToDouble(a));
    }

    public void set(Pixel pixel) {
        set(pixel.r, pixel.g, pixel.b, pixel.a);
    }

    public void set(byte r, byte g, byte b, byte a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

}
