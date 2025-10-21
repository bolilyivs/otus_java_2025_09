package ru.otus.testframework;

import ru.otus.testframework.framework.TestRunner;

public class Main {

    private Main() {}

    private static final String[] TEST_CLASS_NAMES = {
        "ru.otus.testframework.test.TestCase1",
        "ru.otus.testframework.test.TestCase2",
        "ru.otus.testframework.test.TestCase3"
    };

    public static void main(String[] args) {
        TestRunner.runTest(TEST_CLASS_NAMES);
    }
}
