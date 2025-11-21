package ru.otus.homework.aop;

import java.lang.reflect.Method;
import java.util.Arrays;

public class MethodUtils {
    private MethodUtils() {}

    public static boolean areMethodsEqual(Method method1, Method method2) {
        if (method1 == null || method2 == null) {
            return false;
        }

        if (!method1.getName().equals(method2.getName())) {
            return false;
        }

        if (!method1.getReturnType().equals(method2.getReturnType())) {
            return false;
        }

        return Arrays.equals(method1.getParameterTypes(), method2.getParameterTypes());
    }
}
