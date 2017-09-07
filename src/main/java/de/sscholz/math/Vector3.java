package de.sscholz.math;


import de.sscholz.util.GlUtil;

public class Vector3 extends VectorSpace<Vector3> {

    public static Vector3 x() {
        return new Vector3(1, 0, 0);
    }

    public static Vector3 y() {
        return new Vector3(0, 1, 0);
    }

    public static Vector3 z() {
        return new Vector3(0, 0, 1);
    }

    public static Vector3 xy() {
        return new Vector3(1, 1, 0);
    }

    public static Vector3 xyz() {
        return one();
    }

    public static Vector3 xz() {
        return new Vector3(1, 0, 1);
    }

    public static Vector3 yz() {
        return new Vector3(0, 1, 1);
    }

    public static Vector3 nx() {
        return new Vector3(-1, 0, 0);
    }

    public static Vector3 ny() {
        return new Vector3(0, -1, 0);
    }

    public static Vector3 nz() {
        return new Vector3(0, 0, -1);
    }

    public static Vector3 one() {
        return new Vector3(1, 1, 1);
    }

    public static Vector3 zero() {
        return new Vector3(0, 0, 0);
    }

    public double x;
    public double y;
    public double z;

    public Vector3() {

    }

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 set(Vector3 that) {
        this.x = that.x;
        this.y = that.y;
        this.z = that.z;
        return this;
    }

    public Vector3 set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Vector3 clone() {
        return new Vector3(x, y, z);
    }

    public Vector2 toXy() {
        return new Vector2(x, y);
    }

    public Vector2 toXz() {
        return new Vector2(x, z);
    }

    public Vector2 toYz() {
        return new Vector2(y, z);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double length2() {
        return x * x + y * y + z * z;
    }

    public double dot(Vector3 that) {
        double dx = that.x - this.x;
        double dy = that.y - this.y;
        double dz = that.z - this.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public double distanceTo(Vector3 that) {
        return Math.sqrt(dot(that));
    }

    public double distanceTo2(Vector3 that) {
        return dot(that);
    }

    public Vector3 add(Vector3 that) {
        this.x += that.x;
        this.y += that.y;
        this.z += that.z;
        return this;
    }

    public Vector3 sub(Vector3 that) {
        this.x -= that.x;
        this.y -= that.y;
        this.z -= that.z;
        return this;
    }

    public Vector3 mul(double s) {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        return this;
    }

    @Override
    public boolean almostEqual(Vector3 b) {
        return MathUtil.almostEqual(x, b.x) && MathUtil.almostEqual(y, b.y) && MathUtil.almostEqual(z, b.z);
    }

    public Vector3 negate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Vector3 normalize() {
        double lsq = length2();
        if (MathUtil.almostEqual(lsq, 1.0))
            return this;
        double linv = 1.0 / Math.sqrt(lsq);
        x *= linv;
        y *= linv;
        z *= linv;
        return this;
    }

    public Vector3 normalize(Vector3 that) {
        set(that);
        normalize();
        return this;
    }

    public double angle(Vector3 that) {
        return Math.acos(dot(that) /
                Math.sqrt(this.length2() * that.length2()));
    }

    public Vector3 cross(Vector3 a, Vector3 b) {
        x = a.y * b.z - a.z * b.y;
        y = a.z * b.x - a.x * b.z;
        z = a.x * b.y - a.y * b.x;
        return this;
    }

    public void gl() {
        GlUtil.getGl().glVertex3d(x, y, z);
    }

    public void glNormal() {
        GlUtil.getGl().glNormal3d(x, y, z);
    }

    public double[] data() {
        return new double[]{x, y, z};
    }

    public float[] floatData() {
        return new float[]{(float) x, (float) y, (float) z};
    }

    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }

}
