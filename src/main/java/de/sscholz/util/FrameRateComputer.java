package de.sscholz.util;

import java.util.LinkedList;
import java.util.Queue;

public class FrameRateComputer {

    public final double timeoutInterval;
    private long lastTime = -1;
    private double lastElapsed = 0.0;

    private class Measurement {
        public final long time;
        public final double elapsed;

        public Measurement(long time, double elapsed) {
            this.time = time;
            this.elapsed = elapsed;
        }
    }

    private Queue<Measurement> measurements = new LinkedList<Measurement>();

    public FrameRateComputer() {
        this(5.0);
    }

    public FrameRateComputer(double timeoutInterval) {
        this.timeoutInterval = timeoutInterval;
    }

    public double getAverageFps() {
        if (measurements.isEmpty())
            return 0.0;
        double sum = 0.0;
        for (Measurement measurement : measurements) {
            sum += measurement.elapsed;
        }
        return measurements.size() / sum;
    }

    public double getElapsed() {
        return lastElapsed;
    }

    public void update() {
        long currentTime = System.nanoTime();
        if (lastTime >= 0) {
            double elapsed = (currentTime - lastTime) / 1.0e9;
            Measurement measurement = new Measurement(currentTime, elapsed);
            measurements.add(measurement);
            lastElapsed = elapsed;
        }
        lastTime = currentTime;
        removeOldMeasurements();
    }

    private void removeOldMeasurements() {
        while (!measurements.isEmpty()) {
            Measurement measurement = measurements.peek();
            double diff = (lastTime - measurement.time) / 1.0e9;
            if (diff > timeoutInterval) {
                measurements.remove();
            } else {
                break;
            }
        }
    }


}
