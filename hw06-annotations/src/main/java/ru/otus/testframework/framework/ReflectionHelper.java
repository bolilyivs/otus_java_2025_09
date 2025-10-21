package ru.otus.testframework.framework;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class ReflectionHelper {
    private ReflectionHelper() {}

    public static Class<?> getClassFromName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new ReflectionException(e);
        }
    }

    public static List<Method> findMethodByAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        try {
            return Arrays.stream(clazz.getDeclaredMethods())
                    .filter(method -> method.isAnnotationPresent(annotation))
                    .toList();
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    @SuppressWarnings("java:S3011")
    public static void callMethod(Method method, Object object) {
        try {
            method.setAccessible(true);
            method.invoke(object);
        } catch (Exception e) {
            throw new ReflectionException(e.getCause());
        } finally {
            method.setAccessible(false);
        }
    }

    public static Object newInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new ReflectionException(e);
        }
    }

    public static class ReflectionException extends RuntimeException {
        public ReflectionException(Throwable e) {
            super(e);
        }
    }
}
