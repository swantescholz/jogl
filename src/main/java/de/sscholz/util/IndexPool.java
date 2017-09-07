package de.sscholz.util;

import java.util.LinkedList;
import java.util.Queue;

public class IndexPool {

    Queue<Integer> free = new LinkedList<Integer>();
    Queue<Integer> used = new LinkedList<Integer>();

    public void add(int value) {
        free.add(value);
    }

    public int aquire() {
        Integer value = free.poll();
        if (value == null)
            throw new RuntimeException("No free index left to aquire.");
        used.add(value);
        return value;
    }

    public void release(int value) {
        if (used.contains(value)) {
            used.remove(value);
            free.add(value);
        }
    }

}
