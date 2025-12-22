package ru.otus.jdbc.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.crm.model.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

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
        return Arrays.stream(clazz.getFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Field> getAllFields() {
        return List.of(clazz.getFields());
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return Arrays.stream(clazz.getFields())
                .filter(field -> !field.isAnnotationPresent(Id.class))
                .toList();
    }
}
