package ru.otus.jdbc.mapper.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;

@RequiredArgsConstructor
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;

    @Override
    public String getName() {
        return clazz.getSimpleName();
    }

    @Override
    @SneakyThrows
    public Constructor<T> getConstructor() {
        return clazz.getConstructor();
    }

    @Override
    public Field getIdField() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clazz.getDeclaredFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
