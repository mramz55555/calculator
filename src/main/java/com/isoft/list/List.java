package com.isoft.list;

public interface List<T> {
    void add(T e);

    void remove(int index);

    boolean remove(T e);

    int size();

    boolean contains(T e);

    void clear();

    T get(int index);

    boolean isEmpty();

    boolean isFull();
}
