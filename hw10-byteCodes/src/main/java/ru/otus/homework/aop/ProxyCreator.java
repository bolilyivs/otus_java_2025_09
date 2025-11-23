package ru.otus.homework.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyCreator {

    private static final Logger log = LoggerFactory.getLogger(ProxyCreator.class);

    private ProxyCreator() {}

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> interfaceClass, Supplier<T> implClass) {
        InvocationHandler handler = new ProxyHandler<>(implClass.get());
        return (T) Proxy.newProxyInstance(
                ProxyCreator.class.getClassLoader(), List.of(interfaceClass).toArray(new Class<?>[] {}), handler);
    }

    static class ProxyHandler<T> implements InvocationHandler {

        private final T original;
        private final List<MethodSignature> annotatedLogMethods;

        public ProxyHandler(T original) {
            this.original = original;
            this.annotatedLogMethods = findAnnotatedMethodSignature(this.original, Log.class);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (log.isInfoEnabled() && isAnnotatedMethod(method, annotatedLogMethods)) {
                log.info("executed method: {}, param: {}", method.getName(), Arrays.toString(args));
            }
            return method.invoke(original, args);
        }

        private boolean isAnnotatedMethod(Method method, List<MethodSignature> annotatedMethods) {
            MethodSignature methodSignature = MethodUtils.createMethodSignature(method);
            return annotatedMethods.contains(methodSignature);
        }

        private List<MethodSignature> findAnnotatedMethodSignature(
                T original, Class<? extends Annotation> annotationClass) {
            List<Method> methods = findAnnotatedMethods(original, annotationClass);
            return methods.stream().map(MethodUtils::createMethodSignature).toList();
        }

        private List<Method> findAnnotatedMethods(T original, Class<? extends Annotation> annotationClass) {
            return Arrays.stream(original.getClass().getMethods())
                    .filter(origMethod -> origMethod.isAnnotationPresent(annotationClass))
                    .toList();
        }
    }
}
