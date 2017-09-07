package de.sscholz.math;

public class Quaternion extends VectorSpace<Quaternion> {

    public static Quaternion zero() {
        return new Quaternion(0, 0, 0, 0);
    }

    public static Quaternion one() {
        return new Quaternion(1, 0, 0, 0);
    }

    public double w;
    public double x;
    public double y;
    public double z;

    public Quaternion() {

    }

    public Quaternion(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Quaternion(double w, Vector3 v) {
        set(w, v);
    }

    public Quaternion(Quaternion q) {
        set(q);
    }

    public Quaternion set(double w, double x, double y, double z) {
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
        return this;
    }

    public Quaternion set(double w, Vector3 v) {
        return set(w, v.x, v.y, v.z);
    }

    public Quaternion set(Quaternion q) {
        return set(q.w, q.x, q.y, q.z);
    }

    public Quaternion clone() {
        return new Quaternion(this);
    }

    @Override
    public Quaternion negate() {
        w = -w;
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Quaternion add(Quaternion that) {
        w += that.w;
        x += that.x;
        y += that.y;
        z += that.z;
        return this;
    }

    public Quaternion sub(Quaternion that) {
        w -= that.w;
        x -= that.x;
        y -= that.y;
        z -= that.z;
        return this;
    }

    public Quaternion mul(double s) {
        w *= s;
        x *= s;
        y *= s;
        z *= s;
        return this;
    }

    @Override
    public boolean almostEqual(Quaternion b) {
        return MathUtil.almostEqual(w, b.w) && MathUtil.almostEqual(x, b.x) &&
                MathUtil.almostEqual(y, b.y) && MathUtil.almostEqual(z, b.z);
    }

    public Quaternion mul(Quaternion q) {
        mul(clone(), q);
        return this;
    }

    public Quaternion mul(Quaternion a, Quaternion b) {
        w = a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z;
        x = a.x * b.w + a.w * b.x + a.y * b.z - a.z * b.y;
        y = a.y * b.w + a.w * b.y + a.z * b.x - a.x * b.z;
        z = a.z * b.w + a.w * b.z + a.x * b.y - a.y * b.x;
        return this;
    }

    public Quaternion conjugate() {
        x = -x;
        y = -y;
        z = -z;
        return this;
    }

    public Quaternion conjugate(Quaternion q) {
        w = q.w;
        x = -q.x;
        y = -q.y;
        z = -q.z;
        return this;
    }

    public Quaternion normalize() {
        div(length());
        return this;
    }

    public Quaternion normalize(Quaternion q) {
        div(q, length());
        return this;
    }

    public double length() {
        return Math.sqrt(w * w + x * x + y * y + z * z);
    }

    public double length2() {
        return w * w + x * x + y * y + z * z;
    }

    public Quaternion invert() {
        conjugate();
        div(length2());
        return this;
    }

    public Quaternion invert(Quaternion q) {
        conjugate(q);
        div(length2());
        return this;
    }

    public double dot(Quaternion q) {
        return w * q.w + x * q.x + y * q.y + z * q.z;
    }

    public double getAngle() {
        return 2.0 * Math.acos(w);
    }

    public Vector3 getAxis() {
        return new Vector3(x, y, z).normalize();
    }

    public Vector3 getVector() {
        return new Vector3(x, y, z);
    }

    public static Quaternion axisAngle(Vector3 axis, double angle) {
        return new Quaternion(Math.cos(-angle * 0.5),
                axis.clone().normalize().mul(Math.sin(-angle * 0.5)));
    }

    public Vector3 transform(Vector3 v) {
        Quaternion vpure = new Quaternion(0, v);
        Quaternion inv = new Quaternion().invert(this);
        Quaternion result = new Quaternion().mul(this, vpure).mul(inv);
        return result.getVector();
    }

    public Quaternion lerp(Quaternion q, double t) {
        lerp(clone(), q, t);
        return this;
    }

    public Quaternion lerp(Quaternion a, Quaternion b, double t) {
        this.mul(a, 1.0 - t).add(new Quaternion().mul(b, t));
        return this;
    }

    public Quaternion nlerp(Quaternion q, double t) {
        nlerp(clone(), q, t);
        return this;
    }

    public Quaternion nlerp(Quaternion a, Quaternion b, double t) {
        lerp(a, b, t);
        normalize();
        return this;
    }

    public Quaternion slerp(Quaternion q, double t) {
        slerp(clone(), q, t);
        return this;
    }

    public Quaternion slerp(Quaternion a, Quaternion b, double t) {
        final double alpha = Math.acos(a.dot(b));
        Quaternion tmp = new Quaternion().mul(b, Math.sin(t * alpha));
        this.mul(a, Math.sin((1.0 - t) * alpha)).add(tmp).div(Math.sin(alpha));
        return this;
    }

    public double[] data() {
        return new double[]{x, y, z};
    }

    public String toString() {
        return "(" + w + ", " + x + ", " + y + ", " + z + ")";
    }

    public Matrix toMatrix() {
        double xx2 = 2 * x * x, yy2 = 2 * y * y, zz2 = 2 * z * z;
        double wx2 = 2 * w * x, wy2 = 2 * w * y, wz2 = 2 * w * z;
        double xy2 = 2 * x * y, xz2 = 2 * x * z, yz2 = 2 * y * z;
        Matrix m = new Matrix();
        m.m[15] = 1;
        m.m[3] = m.m[7] = m.m[11] = m.m[12] = m.m[13] = m.m[14] = 0;
        m.m[0] = 1 - yy2 - zz2;
        m.m[5] = 1 - xx2 - zz2;
        m.m[10] = 1 - xx2 - yy2;
        m.m[1] = xy2 - wz2;
        m.m[4] = xy2 + wz2;
        m.m[8] = xz2 - wy2;
        m.m[2] = xz2 + wy2;
        m.m[6] = yz2 - wx2;
        m.m[9] = yz2 + wx2;
        return m;
    }


/*
 0  1  2  3
 4  5  6  7
 8  9 10 11
12 13 14 15
*/
}
