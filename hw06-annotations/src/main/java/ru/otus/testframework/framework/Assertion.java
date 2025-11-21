package ru.otus.testframework.framework;

import java.util.Objects;

public class Assertion {
    private Assertion() {}

    public static <T> void assertEqual(T value1, T value2) {
        if (!Objects.equals(value1, value2)) {
            throw new AssertionException("not equal 0");
        }
    }

    public static void assertTrue(boolean value) {
        if (!value) {
            throw new AssertionException("not true");
        }
    }

    public static class AssertionException extends RuntimeException {
        public AssertionException(String msg) {
            super(msg);
        }
    }
}
