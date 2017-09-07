package de.sscholz.math;

import de.sscholz.util.GlUtil;

public class Vector2 extends VectorSpace<Vector2> {

    public static Vector2 x() {
        return new Vector2(1, 0);
    }

    public static Vector2 y() {
        return new Vector2(0, 1);
    }

    public static Vector2 xy() {
        return one();
    }

    public static Vector2 nx() {
        return new Vector2(-1, 0);
    }

    public static Vector2 ny() {
        return new Vector2(0, -1);
    }

    public static Vector2 one() {
        return new Vector2(1, 1);
    }

    public static Vector2 zero() {
        return new Vector2(0, 0);
    }

    public double x;
    public double y;

    public Vector2() {

    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 set(Vector2 that) {
        this.x = that.x;
        this.y = that.y;
        return this;
    }

    public Vector2 set(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2 clone() {
        return new Vector2(x, y);
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double length2() {
        return x * x + y * y;
    }

    public double distanceTo(Vector2 that) {
        double dx = that.x - this.x;
        double dy = that.y - this.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public double distanceTo2(Vector2 that) {
        double dx = that.x - this.x;
        double dy = that.y - this.y;
        return dx * dx + dy * dy;
    }

    public Vector2 add(Vector2 that) {
        this.x += that.x;
        this.y += that.y;
        return this;
    }

    public Vector2 sub(Vector2 that) {
        this.x -= that.x;
        this.y -= that.y;
        return this;
    }

    public Vector2 mul(double s) {
        this.x *= s;
        this.y *= s;
        return this;
    }

    @Override
    public boolean almostEqual(Vector2 b) {
        return MathUtil.almostEqual(x, b.x) && MathUtil.almostEqual(y, b.y);
    }

    public Vector2 negate() {
        x = -x;
        y = -y;
        return this;
    }

    public Vector2 normalize() {
        double lsq = length2();
        if (MathUtil.almostEqual(lsq, 1.0))
            return this;
        double linv = 1.0 / Math.sqrt(lsq);
        x *= linv;
        y *= linv;
        return this;
    }

    public Vector2 normalize(Vector2 that) {
        set(that);
        normalize();
        return this;
    }

    public void gl() {
        GlUtil.getGl().glVertex2d(x, y);
    }

    public void glTex() {
        GlUtil.getGl().glTexCoord2d(x, y);
    }

    public double[] data() {
        return new double[]{x, y};
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }


}
