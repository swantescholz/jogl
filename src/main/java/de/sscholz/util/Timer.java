package de.sscholz.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Timer {


    private static List<Timer> timerList = new ArrayList<Timer>();

    private final double duration;
    private double elapsed = 0.0;
    private boolean expired = false;

    public Timer(double duration) {
        this.duration = duration;
        timerList.add(this);
    }

    private void update(double elapsed) {
        this.elapsed += elapsed;
        checkExpiry();
    }

    private void checkExpiry() {
        if (this.elapsed >= duration) {
            expired = true;
        }
    }

    public boolean isExpired() {
        return expired;
    }

    public void reset() {
        elapsed = 0.0;
        expired = false;
        timerList.add(this);
    }

    public static void updateAll(double elapsed) {
        Iterator<Timer> it = timerList.iterator();
        while (it.hasNext()) {
            Timer timer = it.next();
            timer.update(elapsed);
            if (timer.isExpired()) {
                it.remove();
            }
        }
    }

    public double getTime() {
        return elapsed;
    }
}
