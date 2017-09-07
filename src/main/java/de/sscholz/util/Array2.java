package de.sscholz.util;

import java.util.ArrayList;
import java.util.Iterator;

public class Array2<T> implements Iterable<T> {

    public ArrayList<ArrayList<T>> data = new ArrayList<ArrayList<T>>();

    public Array2(int width, int height) {
        for (int x = 0; x < width; x++) {
            ArrayList<T> column = new ArrayList<T>();
            for (int y = 0; y < height; y++) {
                column.add(null);
            }
            data.add(column);
        }
    }

    public int getWidth() {
        return data.size();
    }

    public int getHeight() {
        return data.get(0).size();
    }

    public T get(int x, int y) {
        if (x < 0 || y < 0 || x >= getWidth() || y >= getHeight())
            return null;
        return data.get(x).get(y);
    }

    public void set(int x, int y, T value) {
        data.get(x).set(y, value);
    }

    private class MyIterator implements Iterator<T> {

        int x = 0;
        int y = 0;
        Array2<T> grid;

        public MyIterator(Array2<T> grid) {
            this.grid = grid;
        }

        @Override
        public boolean hasNext() {
            return y < grid.getHeight();
        }

        @Override
        public T next() {
            T current = grid.get(x, y);
            x++;
            if (x >= grid.getWidth()) {
                x = 0;
                y++;
            }
            return current;
        }

        @Override
        public void remove() {
            throw new RuntimeException("not implemented");
        }

    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator(this);
    }

}
