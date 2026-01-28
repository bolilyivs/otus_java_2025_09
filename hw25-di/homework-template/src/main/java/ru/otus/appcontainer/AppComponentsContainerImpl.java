package ru.otus.appcontainer;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(String packageName) {
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(packageName))
                .setScanners(Scanners.TypesAnnotated));

        // This will return all classes found in the package and its sub-packages
        // Note: Object.class is passed to get all subtypes (i.e., all classes)
        Set<Class<?>> configClasses = reflections.getTypesAnnotatedWith(AppComponentsContainerConfig.class);
        processConfig(configClasses);
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClass) {
        processConfig(Arrays.stream(initialConfigClass).toList());
    }

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Collection<Class<?>> configClassList) {
        configClassList.stream()
                .sorted(Comparator.comparing(configClass -> configClass
                        .getAnnotation(AppComponentsContainerConfig.class)
                        .order()))
                .forEach(this::processConfig);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        Object configObj = createConfigObject(configClass);

        Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .map(method -> AppComponentMeta.of(configObj, method))
                .sorted(Comparator.comparing(AppComponentMeta::order))
                .forEach(this::createAppComponentInstance);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @SneakyThrows
    private Object createConfigObject(Class<?> configClass) {
        MethodType mt = MethodType.methodType(void.class);
        MethodHandle newIntegerMH = publicLookup.findConstructor(configClass, mt);
        return newIntegerMH.invoke();
    }

    @SneakyThrows
    private void createAppComponentInstance(AppComponentMeta appComponentMeta) {
        if (appComponentsByName.containsKey(appComponentMeta.name())) {
            throw new AppComponentException("Компонент с таким именем уже есть!");
        }

        Method method = appComponentMeta.method();
        List<Object> parameters = Arrays.stream(method.getParameters())
                .map(this::findAppComponentFromParameter)
                .toList();

        MethodHandle methodHandle = publicLookup.unreflect(method);
        methodHandle = methodHandle.bindTo(appComponentMeta.configObject());
        Object instance = methodHandle.invokeWithArguments(parameters);
        appComponents.add(instance);
        appComponentsByName.put(appComponentMeta.name(), instance);
    }

    private Object findAppComponentFromParameter(Parameter parameter) {
        Object component = getAppComponent(parameter.getName());
        if (Objects.nonNull(component)) {
            return component;
        }
        return getAppComponent(parameter.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> components = appComponents.stream()
                .filter(component -> componentClass.isAssignableFrom(component.getClass()))
                .toList();
        if (components.size() > 1) {
            throw new AppComponentException("Надено более 1 компонента!");
        }
        return (C) components.stream().findFirst().orElseThrow(() -> new AppComponentException("Компонент не найден"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        if (appComponentsByName.containsKey(componentName)) {
            return (C) appComponentsByName.get(componentName);
        }
        throw new AppComponentException("Компонент не найден");
    }

    public record AppComponentMeta(Object configObject, Method method, int order, String name) {
        public static AppComponentMeta of(Object configObject, Method method) {
            AppComponent annotation = method.getAnnotation(AppComponent.class);
            return new AppComponentMeta(configObject, method, annotation.order(), annotation.name());
        }
    }

    public static class AppComponentException extends RuntimeException {
        public AppComponentException(String message) {
            super(message);
        }
    }
}
