package ru.otus.homework.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class MethodUtils {
    private MethodUtils() {}

    public static MethodSignature createMethodSignature(Method method) {
        List<String> paramTypes =
                Arrays.stream(method.getParameterTypes()).map(Class::getName).toList();
        return new MethodSignature(method.getName(), paramTypes);
    }
}
