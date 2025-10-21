package ru.otus.testframework.test;

import ru.otus.testframework.framework.Assertion;
import ru.otus.testframework.framework.annotation.After;
import ru.otus.testframework.framework.annotation.Before;
import ru.otus.testframework.framework.annotation.Test;

public class TestCase3 {

    private int testValue;
    private int testValue2;

    @Before
    public void init() {
        testValue = 0;
    }

    @Before
    public void init2() {
        testValue2 = 0;
    }

    @Test
    public void test1() {
        Assertion.assertEqual(testValue, 0);
        Assertion.assertEqual(testValue2, 0);
        testValue = 1;
        testValue2 = 1;
    }

    @Test
    public void test2() {
        Assertion.assertEqual(testValue, 0);
        Assertion.assertEqual(testValue2, 0);
        testValue = 2;
        testValue2 = 2;
    }

    @After
    public void finish() {
        testValue = 0;
    }

    @After
    public void finish2() {
        testValue2 = 0;
    }
}
