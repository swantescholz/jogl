package de.sscholz.math;

public class MathUtil {

    public static final double EPSILON = 0.000001;

    public static double toDegree(double radian) {
        return radian / (Math.PI * 2) * 360.0;
    }

    public static double toRadian(double degree) {
        return degree * (Math.PI * 2) / 360.0;
    }

    public static double interpolate(double a, double b, double t) {
        return (1 - t) * a + t * b;
    }

    public static int clamp(int s, int min, int max) {
        if (s < min) return min;
        if (s > max) return max;
        return s;
    }

    public static double clamp(double s, double min, double max) {
        if (s < min) return min;
        if (s > max) return max;
        return s;
    }

    public static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static double randomDouble(double min, double max) {
        return Math.random() * (max - min) + min;
    }

    public static boolean almostEqual(double a, double b) {
        return Math.abs(b - a) < EPSILON;

    }

    public static double clamp(double d) {
        return clamp(d, 0.0, 1.0);
    }

    public static int unsignByte(byte b) {
        return b & 0xFF;
    }

    public static double byteToDouble(byte b) {
        int i = unsignByte(b);
        return i / 255.0;
    }
}
