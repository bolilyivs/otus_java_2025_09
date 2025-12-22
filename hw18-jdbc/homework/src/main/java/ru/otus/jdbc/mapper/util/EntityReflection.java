package ru.otus.jdbc.mapper.util;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.List;
import lombok.SneakyThrows;
import ru.otus.jdbc.mapper.EntityClassMetaData;

public class EntityReflection<T> {

    private final MethodHandles.Lookup lookup;

    @SneakyThrows
    public EntityReflection(Class<T> tClass) {
        this.lookup = MethodHandles.privateLookupIn(tClass, MethodHandles.lookup());
    }

    @SneakyThrows
    public T createEntity(EntityClassMetaData<T> entityClassMetaData, ResultSet rs) {
        Constructor<T> constructor = entityClassMetaData.getConstructor();
        T entity = constructor.newInstance();
        entityClassMetaData.getAllFields().forEach(field -> setVar(field, entity, rs));
        return entity;
    }

    @SneakyThrows
    private void setVar(Field field, T entity, ResultSet rs) {
        Class<?> fieldClass = field.getType();
        lookup.unreflectVarHandle(field)
                .set(entity, fieldClass.cast(rs.getObject(field.getName().toLowerCase())));
    }

    @SneakyThrows
    public List<Object> unpack(EntityClassMetaData<T> entityClassMetaData, T entity) {
        return entityClassMetaData.getAllFields().stream()
                .map(field -> getVar(field, entity))
                .toList();
    }

    @SneakyThrows
    private Object getVar(Field field, T entity) {
        return lookup.unreflectVarHandle(field).get(entity);
    }
}
