package ru.otus.testframework.test;

import ru.otus.testframework.framework.Assertion;
import ru.otus.testframework.framework.annotation.After;
import ru.otus.testframework.framework.annotation.Before;
import ru.otus.testframework.framework.annotation.Test;

@SuppressWarnings("java:S112")
public class TestCase2 {

    private int testValue;

    @Before
    public void init() {
        testValue = 0;
    }

    @Test
    public void test1() {
        Assertion.assertEqual(testValue, 0);
        testValue = 1;
        Assertion.assertTrue(true);
    }

    @Test
    public void test2() {
        Assertion.assertEqual(testValue, 0);
        testValue = 2;
        Assertion.assertTrue(false);
    }

    @After
    public void finish() {
        testValue = 0;
    }
}
