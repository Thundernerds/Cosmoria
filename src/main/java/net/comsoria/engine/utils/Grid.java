package net.comsoria.engine.utils;

import org.joml.Vector2f;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Array.newInstance;

public class Grid<T> {
    private final List<T> rawItems;

    private final int width;
    private final int height;

    public Grid(int width, int height) {
        this(width, height, null);
    }

    public Grid(int width, int height, T nullvalue) {
        this.width = width;
        this.height = height;

        this.rawItems = new ArrayList<>();
        for (int i = 0; i < this.length(); i++) {
            this.rawItems.add(nullvalue);
        }
    }

    public int getHeight() {
        return height;
    }
    public int getWidth() {
        return width;
    }

    public int getIndex(int x, int y) {
        return (this.width * y) + x;
    }

    public T get(int x, int y) {
        return this.rawItems.get(this.getIndex(x, y));
    }
    public void set(int x, int y, T object) {
        this.rawItems.set(this.getIndex(x, y), object);
    }

    public T get(int i) {
        return this.rawItems.get(i);
    }
    public void set(int i, T object) {
        this.rawItems.set(i, object);
    }

    public boolean contains(T object) {
        for (T item : this.rawItems) {
            if (item.equals(object))  return true;
        }
        return false;
    }

    public void clear() {
        this.rawItems.clear();
    }

    public void fill(T object) {
        for (int i = 0; i < this.rawItems.size(); i++) {
            this.rawItems.set(i, object);
        }
    }

    public T[] getArray(Class tClass) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) newInstance(tClass, this.rawItems.size());
        for (int i = 0; i < this.rawItems.size(); i++) {
            array[i] = this.rawItems.get(i);
        }
        return array;
    }

    public void foreach(IterationCallback callback) {
        for (int i = 0; i < this.rawItems.size(); i++) {
            if (callback.run(this.rawItems.get(i), i)) return;
        }
    }

    public int length() {
        return width * height;
    }

    @Override
    public String toString() {
        return "Grid[" + this.length() + "]";
    }

    public Vector2i getXY(int i) {
        int x = i % this.width;
        int y = (i - x) / this.height;
        return new Vector2i(x, y);
    }
}
