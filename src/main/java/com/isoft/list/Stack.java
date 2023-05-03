package com.isoft.list;

import java.util.Arrays;
import java.util.Objects;

public class Stack<T> implements List<T> {
    private static final int DEFAULT_SIZE = 15;
    private final Object[] stack;
    private int top = -1;

    public Stack() {
        this.stack = new Object[DEFAULT_SIZE];
    }

    public Stack(int size) {
        if (size < 0)
            throw new IllegalArgumentException("Invalid size");
        this.stack = new Object[size];
    }


    public void add(T e) {
        if (isFull())
            throw new IllegalArgumentException("Stack is full");
        stack[++top] = e;

    }


    public void remove(int index) {
        throw new UnsupportedOperationException("stack does not support deletion from an index");
    }

    public boolean remove(T e) {
        if (!contains(e))
            throw new IllegalArgumentException("element not found");

        boolean notFound;

        while (notFound = !pop().equals(e)) {
        }
        return !notFound;
    }

    public T pop() {
        T last = (T) stack[top];
        stack[top--] = null;
        return last;
    }

    public int size() {
        return top + 1;
    }

    public boolean contains(T e) {
        Objects.requireNonNull(e);
        for (Object t : stack)
            if (t.equals(e))
                return true;

        return false;
    }


    public void clear() {
        Arrays.fill(stack, null);
        top = -1;
    }

    public T get(int index) {
        if (index < 0)
            throw new IllegalArgumentException("Invalid index");
        return (T) stack[index];
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public boolean isFull() {
        return top == stack.length - 1;
    }

    public T peek() {
        return (T) stack[size() - 1];
    }
}
