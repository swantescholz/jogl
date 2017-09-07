package de.sscholz.math;

import de.sscholz.util.GlUtil;
import de.sscholz.util.StringUtil;

public class Matrix extends VectorSpace<Matrix> {

    public static Matrix identity() {
        return new Matrix(1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0);
    }

    public static Matrix zero() {
        return new Matrix(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    }

    public static final int ELEMENT_COUNT = 16;
    public double[] m = new double[ELEMENT_COUNT];

    public Matrix() {

    }

    public Matrix(Matrix that) {
        set(that.m);
    }

    public Matrix(double[] m) {
        set(m);
    }

    public Matrix(
            double m00, double m01, double m02, double m03,
            double m10, double m11, double m12, double m13,
            double m20, double m21, double m22, double m23,
            double m30, double m31, double m32, double m33) {
        set(m00, m01, m02, m03, m10, m11, m12, m13, m20, m21, m22, m23, m30, m31, m32, m33);
    }

    public Matrix set(Matrix that) {
        set(that.m);
        return this;
    }

    private Matrix set(double[] m) {
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            this.m[i] = m[i];
        }
        return this;
    }

    private Matrix set(double m00, double m01, double m02, double m03,
                       double m10, double m11, double m12, double m13,
                       double m20, double m21, double m22, double m23,
                       double m30, double m31, double m32, double m33) {

        m[0] = m00;
        m[1] = m01;
        m[2] = m02;
        m[3] = m03;
        m[4] = m10;
        m[5] = m11;
        m[6] = m12;
        m[7] = m13;
        m[8] = m20;
        m[9] = m21;
        m[10] = m22;
        m[11] = m23;
        m[12] = m30;
        m[13] = m31;
        m[14] = m32;
        m[15] = m33;
        return this;
    }

    public Matrix clone() {
        return new Matrix(this);
    }

    @Override
    public Matrix negate() {
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            m[i] = -m[i];
        }
        return this;
    }


    private int getIndex(int row, int col) {
        return row * 4 + col;
    }

    public double get(int row, int col) {
        return m[getIndex(row, col)];
    }

    public Matrix set(int row, int col, double value) {
        m[getIndex(row, col)] = value;
        return this;
    }

    // *********************************

    public Matrix add(double s) {
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            this.m[i] += s;
        }
        return this;
    }

    public Matrix add(Matrix that) {
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            this.m[i] += that.m[i];
        }
        return this;
    }

    public Matrix sub(Matrix that) {
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            this.m[i] -= that.m[i];
        }
        return this;
    }

    public Matrix mul(double s) {
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            this.m[i] *= s;
        }
        return this;
    }

    @Override
    public boolean almostEqual(Matrix b) {
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            if (!MathUtil.almostEqual(m[i], b.m[i]))
                return false;
        }
        return true;
    }

    public Matrix mul(Matrix that) {
        this.mul(this.clone(), that);
        return this;
    }

    public Matrix mul(Matrix a, Matrix b) {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                double value = 0.0;
                for (int i = 0; i < 4; i++) {
                    value += a.get(y, i) * b.get(i, x);
                }
                set(y, x, value);
            }
        }
        return this;
    }

    public Matrix transpose() {
        for (int y = 1; y < 4; y++) {
            for (int x = 0; x < y; x++) {
                swapElements(y, x, x, y);
            }
        }
        return this;
    }

    public Matrix transpose(Matrix that) {
        set(that);
        return transpose();
    }

    private void swapElements(int rowa, int cola, int rowb, int colb) {
        double tmp = get(rowa, cola);
        set(rowa, cola, get(rowb, colb));
        set(rowb, colb, tmp);
    }

    public double determinant() {
        return m[0] * (m[5] * m[10] - m[6] * m[9]) -
                m[1] * (m[4] * m[10] - m[6] * m[8]) +
                m[2] * (m[4] * m[9] - m[5] * m[8]);
    }


    public Matrix invert() {
        Matrix tmp = this.clone();
        this.invert(tmp);
        return this;
    }

    public Matrix invert(Matrix o) {
        double fInvDet = 1.0 / o.determinant();

        m[0] = fInvDet * (o.m[5] * o.m[10] - o.m[6] * o.m[9]);
        m[1] = -fInvDet * (o.m[1] * o.m[10] - o.m[2] * o.m[9]);
        m[2] = fInvDet * (o.m[1] * o.m[6] - o.m[2] * o.m[5]);
        m[3] = 0.0;
        m[4] = -fInvDet * (o.m[4] * o.m[10] - o.m[6] * o.m[8]);
        m[5] = fInvDet * (o.m[0] * o.m[10] - o.m[2] * o.m[8]);
        m[6] = -fInvDet * (o.m[0] * o.m[6] - o.m[2] * o.m[4]);
        m[7] = 0.0;
        m[8] = fInvDet * (o.m[4] * o.m[9] - o.m[5] * o.m[8]);
        m[9] = -fInvDet * (o.m[0] * o.m[9] - o.m[1] * o.m[8]);
        m[10] = fInvDet * (o.m[0] * o.m[5] - o.m[1] * o.m[4]);
        m[11] = 0.0;
        m[12] = -(o.m[12] * m[0] + o.m[13] * m[4] + o.m[14] * o.m[8]);
        m[13] = -(o.m[12] * m[1] + o.m[13] * m[5] + o.m[14] * o.m[9]);
        m[14] = -(o.m[12] * m[2] + o.m[13] * m[6] + o.m[14] * o.m[10]);
        m[15] = 1.0;
        return this;
    }

    public Vector3 transform(Vector3 v) {
        double x = v.x * m[0] + v.y * m[4] + v.z * m[8] + m[12];
        double y = v.x * m[1] + v.y * m[5] + v.z * m[9] + m[13];
        double z = v.x * m[2] + v.y * m[6] + v.z * m[10] + m[14];
        double w = v.x * m[3] + v.y * m[7] + v.z * m[11] + m[15];
        v.set(x, y, z);
        if (w != 1.0) {
            v.mul(1.0 / w);
        }
        return v;
    }

    public Vector3 transformNormal(Vector3 v) {
        double fLength = v.length();
        if (fLength == 0.0f) return v;
        Matrix mTransform = this.clone();
        mTransform.invert();
        mTransform.transpose();
        v.set(
                v.x * mTransform.m[0] + v.y * mTransform.m[4] + v.z * mTransform.m[8],
                v.x * mTransform.m[1] + v.y * mTransform.m[5] + v.z * mTransform.m[9],
                v.x * mTransform.m[2] + v.y * mTransform.m[6] + v.z * mTransform.m[10]);
        v.normalize();
        v.mul(fLength);
        return v;
    }

    public static Matrix translation(Vector3 v) {
        return new Matrix(
                1.0, 0.0, 0.0, 0.0,
                0.0, 1.0, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                v.x, v.y, v.z, 1.0);
    }

    public static Matrix rotation(Vector3 axis, double angle) {
        double fSin = Math.sin(-angle);
        double fCos = Math.cos(-angle);
        double fOneMinusCos = 1.0 - fCos;

        Vector3 vAxis = axis.clone();
        vAxis.normalize();

        return new Matrix(
                (vAxis.x * vAxis.x) * fOneMinusCos + fCos,
                (vAxis.x * vAxis.y) * fOneMinusCos - (vAxis.z * fSin),
                (vAxis.x * vAxis.z) * fOneMinusCos + (vAxis.y * fSin),
                0.0,
                (vAxis.y * vAxis.x) * fOneMinusCos + (vAxis.z * fSin),
                (vAxis.y * vAxis.y) * fOneMinusCos + fCos,
                (vAxis.y * vAxis.z) * fOneMinusCos - (vAxis.x * fSin),
                0.0,
                (vAxis.z * vAxis.x) * fOneMinusCos - (vAxis.y * fSin),
                (vAxis.z * vAxis.y) * fOneMinusCos + (vAxis.x * fSin),
                (vAxis.z * vAxis.z) * fOneMinusCos + fCos,
                0.0,
                0.0, 0.0, 0.0, 1.0);
    }

    public static Matrix scaling(double s) {
        return Matrix.scaling(new Vector3(s, s, s));
    }

    public static Matrix scaling(Vector3 v) {
        return new Matrix(
                v.x, 0.0, 0.0, 0.0,
                0.0, v.y, 0.0, 0.0,
                0.0, 0.0, v.z, 0.0,
                0.0, 0.0, 0.0, 1.0);
    }

    public static Matrix projection(double fFOV, double fAspect, double fNearPlane, double fFarPlane) {
        double dxInv = 1.0 / (2.0 * fNearPlane * Math.tan(fFOV * 0.5));
        double dyInv = dxInv * fAspect;
        double dzInv = 1.0 / (fFarPlane - fNearPlane);
        double X = 2.0 * fNearPlane;
        double C = -(fNearPlane + fFarPlane) * dzInv;
        double D = -fNearPlane * fFarPlane * dzInv;
        return new Matrix(X * dxInv, 0, 0, 0, 0, X * dyInv, 0, 0, 0, 0, C, -1, 0, 0, D, 0);
    }

    public static Matrix ortho(double left, double right, double bottom, double top, double near, double far) {
        double a = 2 / (right - left);
        double b = 2 / (top - bottom);
        double c = -2 / (far - near);
        return new Matrix(
                a, 0, 0, -(right + left) / (right - left),
                0, b, 0, -(top + bottom) / (top - bottom),
                0, 0, c, -(far + near) / (far - near),
                0, 0, 0, 1);
    }

    public static Matrix camera(Vector3 vPos, Vector3 vDir, Vector3 vUp) {
        Vector3 vZAxis = vDir.clone();
        vZAxis.normalize();
        vZAxis.negate();
        Vector3 vXAxis = new Vector3();
        vXAxis.cross(vUp, vZAxis);
        vXAxis.normalize();
        Vector3 vYAxis = new Vector3();
        vYAxis.cross(vZAxis, vXAxis);
        vYAxis.normalize();

        Vector3 negPos = vPos.clone();
        negPos.negate();
        Matrix result = Matrix.translation(negPos);
        result.mul(new Matrix(
                vXAxis.x, vYAxis.x, vZAxis.x, 0.0,
                vXAxis.y, vYAxis.y, vZAxis.y, 0.0,
                vXAxis.z, vYAxis.z, vZAxis.z, 0.0,
                0.0, 0.0, 0.0, 1.0));
        return result;
    }

    public String toString() {
        final int desiredLen = 10;
        String sep = " ";
        String s = "[" + sline(0, sep, desiredLen) + StringUtil.NEWLINE;
        s += " " + sline(4, sep, desiredLen) + StringUtil.NEWLINE;
        s += " " + sline(8, sep, desiredLen) + StringUtil.NEWLINE;
        s += " " + sline(12, sep, desiredLen) + "]";
        return s;
    }

    private String sline(int startIndex, String sep, int desiredLen) {
        String s = ralign(m[startIndex], desiredLen);
        for (int i = 1; i < 4; i++) {
            s += sep + ralign(m[startIndex + i], desiredLen);
        }
        return s;
    }

    private String ralign(double number, int desiredLen) {
        String s = "" + number;
        while (s.length() < desiredLen) {
            s += " ";
        }
        return s;
    }

    public void glLoad() {
        GlUtil.getGl().glLoadMatrixd(m, 0);
    }

    public void glMult() {
        GlUtil.getGl().glMultMatrixd(m, 0);
    }


    public static Matrix shear(double amount) {
        return new Matrix(
                1, 0, 0, 0,
                0, 1, 0, 0,
                amount, amount, 1, 0,
                0, 0, 0, 1
        );
    }

}
