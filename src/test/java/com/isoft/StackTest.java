package com.isoft;

import com.isoft.list.Stack;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class StackTest {
    private Stack<Integer> stack;

    @BeforeEach
    void setUp() {
        stack = new Stack<>(4);
        stack.add(231);
        stack.add(31);
        stack.add(11);
        stack.add(-1331);
    }

    @Test
    void add() {
        Assertions.assertEquals(4, stack.size());
    }

    @Test
    void remove() {
        Assertions.assertTrue(stack.remove((Integer) 11));
    }

    @Test
    void pop() {
        Assertions.assertEquals(-1331, stack.pop());
        Assertions.assertEquals(3, stack.size());
    }

    @Test
    void contains() {
        Assertions.assertTrue(stack.contains(31));
    }
}