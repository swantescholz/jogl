package de.sscholz.math;

public class Plane extends VectorSpace<Plane> {

    public static Plane x() {
        return new Plane(1, 0, 0, 0);
    }

    public static Plane y() {
        return new Plane(0, 1, 0, 0);
    }

    public static Plane z() {
        return new Plane(0, 0, 1, 0);
    }

    public static Plane xy() {
        return new Plane(1, 1, 0, 0);
    }

    public static Plane xz() {
        return new Plane(1, 0, 1, 0);
    }

    public static Plane yz() {
        return new Plane(0, 1, 1, 0);
    }

    public static Plane xyz() {
        return new Plane(1, 1, 1, 0);
    }


    public Vector3 n = new Vector3(0.0, 1.0, 0.0);
    public double d = 0.0;


    public Plane() {
    }

    public Plane(double x, double y, double z, double distance) {
        set(x, y, z, distance);
    }

    public Plane(Vector3 normal, double distance) {
        set(normal, distance);
    }

    public Plane(Plane that) {
        set(that);
    }


    @Override
    public Plane set(Plane b) {
        return set(b.n, b.d);
    }

    public Plane set(double x, double y, double z, double distance) {
        return set(new Vector3(x, y, z), distance);
    }

    public Plane set(Vector3 normal, double distance) {
        n.set(normal);
        d = distance;
        return this;
    }

    @Override
    public Plane clone() {
        return new Plane(this);
    }

    @Override
    public Plane negate() {
        n.negate();
        d = -d;
        return this;
    }

    @Override
    public Plane add(Plane b) {
        n.add(b.n);
        d += b.d;
        return this;
    }

    @Override
    public Plane sub(Plane b) {
        n.sub(b.n);
        d -= b.d;
        return this;
    }

    @Override
    public Plane mul(double b) {
        n.mul(b);
        d *= b;
        return this;
    }

    @Override
    public boolean almostEqual(Plane b) {
        return n.almostEqual(b.n) && MathUtil.almostEqual(d, b.d);
    }

    public Plane normalize() {
        double lsq = n.length2();
        if (MathUtil.almostEqual(lsq, 1.0))
            return this;
        double linv = 1.0 / Math.sqrt(lsq);
        n.mul(linv);
        d *= linv;
        return this;
    }

    public Plane normalize(Plane b) {
        return set(b).normalize();
    }

    public double dot(Vector3 b) {
        return n.dot(b) + d;
    }

    public double dotNormal(Vector3 b) {
        return n.dot(b);
    }

    public double distance(Vector3 point) {
        return dot(point);
    }

    public Vector3 nearestPoint(Vector3 result, Vector3 point) {
        return result.mul(n, dot(point)).negate().add(point);
    }

    public static Plane fromPoints(Vector3 a, Vector3 b, Vector3 c) {
        Plane plane = new Plane();
        plane.n.cross(b.clone().sub(a), c.clone().sub(a)).normalize();
        plane.d = plane.distance(a);
        return plane;
    }
}
