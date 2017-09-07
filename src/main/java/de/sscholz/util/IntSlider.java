package de.sscholz.util;

import de.sscholz.math.MathUtil;

public class IntSlider {

    private final int start;
    private final int end;

    private int value;

    public IntSlider(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public void progress(int steps) {
        value += steps;
        value = MathUtil.clamp(value, start, end);
    }

    public int getValue() {
        return value;
    }

}
