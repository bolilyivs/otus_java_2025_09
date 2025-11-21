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

        public ProxyHandler(T original) {
            this.original = original;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (log.isInfoEnabled() && isAnnotationPresent(method, Log.class)) {
                log.info("executed method: {}, param: {}", method.getName(), Arrays.toString(args));
            }
            return method.invoke(original, args);
        }

        private boolean isAnnotationPresent(Method method, Class<? extends Annotation> annotatinClass) {
            return Arrays.stream(original.getClass().getMethods())
                    .anyMatch(origMethod -> origMethod.isAnnotationPresent(annotatinClass)
                            && MethodUtils.areMethodsEqual(origMethod, method));
        }
    }
}
