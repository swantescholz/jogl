package de.sscholz.math;

public class Dimension {

    public int width = 0;
    public int height = 0;

    public Dimension() {

    }

    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int size() {
        return width * height;
    }

    public String toString() {
        return "(" + width + ", " + height + ")";
    }
}
