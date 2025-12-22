package ru.otus.jdbc.mapper.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.jdbc.annotation.Id;
import ru.otus.jdbc.mapper.EntityClassMetaData;

@RequiredArgsConstructor
public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> clazz;

    @Getter
    private final String name;

    @Getter
    private final Constructor<T> constructor;

    @Getter
    private final Field idField;

    @Getter
    private final List<Field> allFields;

    @Getter
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        this.clazz = clazz;
        this.name = initName();
        this.constructor = initConstructor();
        this.idField = initIdField();
        this.allFields = initAllFields();
        this.fieldsWithoutId = initFieldsWithoutId();
    }

    private String initName() {
        return clazz.getSimpleName();
    }

    @SneakyThrows
    private Constructor<T> initConstructor() {
        return clazz.getConstructor();
    }

    private Field initIdField() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
    }

    private List<Field> initAllFields() {
        return List.of(clazz.getDeclaredFields());
    }

    private List<Field> initFieldsWithoutId() {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
