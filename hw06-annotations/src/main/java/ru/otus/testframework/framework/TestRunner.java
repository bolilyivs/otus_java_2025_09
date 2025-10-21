package ru.otus.testframework.framework;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.testframework.framework.annotation.After;
import ru.otus.testframework.framework.annotation.Before;
import ru.otus.testframework.framework.annotation.Test;

public class TestRunner {

    private static final Logger log = LoggerFactory.getLogger(TestRunner.class);

    private TestRunner() {}

    public static void runTest(String... testClassNames) {
        Arrays.stream(testClassNames).map(ReflectionHelper::getClassFromName).forEach(TestRunner::runTestForClass);
    }

    private static void runTestForClass(Class<?> testClass) {
        List<Method> beforeMethods = ReflectionHelper.findMethodByAnnotation(testClass, Before.class);
        List<Method> testMethods = ReflectionHelper.findMethodByAnnotation(testClass, Test.class);
        List<Method> afterMethods = ReflectionHelper.findMethodByAnnotation(testClass, After.class);

        List<Exception> testExceptions = testMethods.stream()
                .map(testMethod -> runTest(testClass, testMethod, beforeMethods, afterMethods))
                .toList();

        showStatistics(testExceptions);
    }

    private static Exception runTest(
            Class<?> testClass, Method testMethod, List<Method> beforeMethods, List<Method> afterMethods) {
        Object testObject = ReflectionHelper.newInstance(testClass);

        List<Exception> beforeExceptions = beforeMethods.stream()
                .map(method -> runMethod(method, testObject))
                .filter(Objects::nonNull)
                .toList();
        if (!beforeExceptions.isEmpty()) {
            log.error("Before test errors: {}", beforeMethods);
        }

        Exception exception = runMethod(testMethod, testObject);

        List<Exception> afterExceptions = afterMethods.stream()
                .map(method -> runMethod(method, testObject))
                .filter(Objects::nonNull)
                .toList();
        if (!afterExceptions.isEmpty()) {
            log.error("After test errors: {}", afterExceptions);
        }

        return exception;
    }

    private static Exception runMethod(Method method, Object object) {
        try {
            ReflectionHelper.callMethod(method, object);
            return null;
        } catch (Exception e) {
            return e;
        }
    }

    private static void showStatistics(List<Exception> exceptions) {
        long total = exceptions.size();
        long success = exceptions.stream().filter(Objects::isNull).count();
        long fail = exceptions.stream().filter(Objects::nonNull).count();
        log.info("Test: success: {}, fail: {}, total: {}", success, fail, total);
        if (fail > 0) {
            log.info(
                    "Fails: {}",
                    exceptions.stream()
                            .filter(Objects::nonNull)
                            .map(Exception::getLocalizedMessage)
                            .toList());
        }
    }
}
