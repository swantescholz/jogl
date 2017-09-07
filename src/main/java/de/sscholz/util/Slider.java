package de.sscholz.util;

import de.sscholz.math.MathUtil;

public class Slider {


    private final double speed;
    private final double start;
    private final double end;

    private double value;

    public Slider() {
        this(1.0);
    }

    public Slider(double speed) {
        this(speed, 0.0, 1.0);
    }

    public Slider(double start, double end) {
        this(end - start, start, end);
    }

    public Slider(double speed, double start, double end) {
        this.speed = speed;
        this.start = start;
        this.end = end;
    }

    public void progress(double elapsed) {
        value += elapsed * speed;
        value = MathUtil.clamp(value, start, end);
    }

    public double getValue() {
        return value;
    }


}
