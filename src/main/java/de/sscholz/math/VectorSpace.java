package de.sscholz.math;

public abstract class VectorSpace<T extends VectorSpace<T>> {

    public abstract T set(T b);

    public abstract T clone();

    public abstract T negate();

    public abstract T add(T b);

    public abstract T sub(T b);

    public abstract T mul(double b);

    public abstract boolean almostEqual(T b);

    public T add(T a, T b) {
        set(a);
        return add(b);
    }

    public T negate(T b) {
        set(b);
        return negate();
    }

    public T sub(T a, T b) {
        set(a);
        return sub(b);
    }

    public T mul(T a, double b) {
        set(a);
        return mul(b);
    }

    public T div(double b) {
        return mul(1.0 / b);
    }

    public T div(T a, double b) {
        return mul(a, 1.0 / b);
    }

    public T interpolateLinear(T b, double t) {
        mul(1.0 - t);
        return add(b.clone().mul(t));
    }

    public T interpolateLinear(T a, T b, double t) {
        set(a);
        return interpolateLinear(b, t);
    }

    public T interpolateHermite(T valueA, T tangentA, T valueB, T tangentB, double t) {
        T a = valueA.clone().sub(valueB).mul(2.0).add(tangentA).add(tangentB).mul(t * t * t);
        T b = valueB.clone().sub(valueA).mul(3.0).sub(tangentA.clone().mul(2.0)).sub(tangentB).mul(t * t);
        return set(a).add(b).add(tangentA.clone().mul(t)).add(valueA);
    }

    public T interpolateBilinear(T a, T b, T c, T d, double x, double y) {
        T p = a.clone().interpolateLinear(b, x);
        T q = c.clone().interpolateLinear(d, x);
        return interpolateLinear(p, q, y);
    }

}
