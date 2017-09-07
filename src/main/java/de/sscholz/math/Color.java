package de.sscholz.math;

import de.sscholz.gfx.texture.Pixel;
import de.sscholz.util.GlUtil;

public class Color extends VectorSpace<Color> {


    public double r;
    public double g;
    public double b;
    public double a;

    public Color() {
        this(0, 0, 0, 1);
    }

    public Color(double r, double g, double b) {
        this(r, g, b, 1);
    }

    public Color(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static Color one() {
        return new Color(1.0, 1.0, 1.0, 1.0);
    }

    public static Color zero() {
        return new Color(0.0, 0.0, 0.0, 0.0);
    }

    public static Color black() {
        return new Color(0.0, 0.0, 0.0);
    }

    public static Color white() {
        return new Color(1.0, 1.0, 1.0);
    }

    public static Color red() {
        return new Color(1.0, 0.0, 0.0);
    }

    public static Color green() {
        return new Color(0.0, 1.0, 0.0);
    }

    public static Color blue() {
        return new Color(0.0, 0.0, 1.0);
    }

    public static Color yellow() {
        return new Color(1.0, 1.0, 0.0);
    }

    public static Color purple() {
        return new Color(1.0, 0.0, 1.0);
    }

    public static Color cyan() {
        return new Color(0.0, 1.0, 1.0);
    }

    public static Color orange() {
        return new Color(1, 0.5, 0, 1);
    }

    public static Color pink() {
        return new Color(0, 0.2667, 0.7333);
    }

    public static Color sky() {
        return new Color(0.5, 0.7, 0.9);
    }

    public static Color mellow() {
        return new Color(0.9, 0.7, 0.5);
    }

    public static Color forest() {
        return new Color(0.247, 0.498, 0.373);
    }

    public static Color silver() {
        return new Color(0.784314, 0.784314, 0.784314);
    }

    public static Color gold() {
        return new Color(0.862745, 0.745098, 0.0);
    }

    public static int rgbIntFromBytes(byte r, byte g, byte b) {
        return rgbIntFromInts(unsignByte(r), unsignByte(g), unsignByte(b));
    }

    public static int rgbIntFromInts(int r, int g, int b) {
        int rgb = 255;
        rgb = (rgb << 8) + r;
        rgb = (rgb << 8) + g;
        rgb = (rgb << 8) + b;
        return rgb;
    }

    public static int unsignByte(byte b) {
        return b & 0xFF;
    }

    public void gl() {
        GlUtil.getGl().glColor4d(r, g, b, a);
    }

    public Color set(Color that) {
        this.r = that.r;
        this.g = that.g;
        this.b = that.b;
        this.a = that.a;
        return this;
    }

    public Color set(double r, double g, double b, double a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    public Color clone() {
        return new Color(r, g, b, a);
    }

    public Color add(Color that) {
        this.r += that.r;
        this.g += that.g;
        this.b += that.b;
        this.a += that.a;
        return this;
    }

    public Color sub(Color that) {
        this.r -= that.r;
        this.g -= that.g;
        this.b -= that.b;
        this.a -= that.a;
        return this;
    }

    public Color mul(double s) {
        this.r *= s;
        this.g *= s;
        this.b *= s;
        this.a *= s;
        return this;
    }

    @Override
    public boolean almostEqual(Color b) {
        return MathUtil.almostEqual(this.r, b.r) && MathUtil.almostEqual(this.g, b.g) &&
                MathUtil.almostEqual(this.b, b.b) && MathUtil.almostEqual(this.a, b.a);
    }

    public Color negate() {
        r = -r;
        g = -g;
        b = -b;
        a = -a;
        return this;
    }

    public void clamp() {
        clamp(0.0, 1.0);
    }

    public Color clamp(double min, double max) {
        r = MathUtil.clamp(r, min, max);
        g = MathUtil.clamp(g, min, max);
        b = MathUtil.clamp(b, min, max);
        a = MathUtil.clamp(a, min, max);
        return this;
    }

    public Pixel toPixel() {
        return new Pixel(toByte(r), toByte(g), toByte(b), toByte(a));
    }

    public double[] data() {
        return new double[]{r, g, b, a};
    }

    public float[] floatData() {
        return new float[]{(float) r, (float) g, (float) b, (float) a};
    }

    public String toString() {
        return "(" + r + ", " + g + ", " + b + ", " + a + ")";
    }

    public static byte toByte(double d) {
        return (byte) MathUtil.clamp((int) (256 * d), 0, 255);
    }

    public static Color random() {
        return random(1.0);
    }

    public static Color random(double alpha) {
        final double min = 0.0;
        final double max = 1.0;
        return new Color(
                MathUtil.randomDouble(min, max),
                MathUtil.randomDouble(min, max),
                MathUtil.randomDouble(min, max),
                alpha);
    }

    public int toRgbInt() {
        return rgbIntFromInts(toInt(r), toInt(g), toInt(b));
    }

    private int toInt(double d) {
        return MathUtil.clamp((int) (d * 256.0), 0, 255);
    }
}
