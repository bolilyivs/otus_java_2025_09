package ru.otus.jdbc.mapper;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Сохраняет объект в базу, читает объект из базы
 */
@SuppressWarnings("java:S1068")
@RequiredArgsConstructor
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData entitySQLMetaData;
    private final EntityClassMetaData<T> entityClassMetaData;
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();


    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (rs.next()) {
                    return createEntity(rs);
                }
                return null;
            } catch (SQLException e) {
                throw new DataTemplateException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var clientList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            clientList.add(createEntity(rs));
                        }
                        return clientList;
                    } catch (SQLException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T client) {
        try {
            return dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getInsertSql(), unpack(client));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            dbExecutor.executeStatement(
                    connection, entitySQLMetaData.getUpdateSql(), unpack(client));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @SneakyThrows
    private T createEntity(ResultSet rs) {
        Constructor<T> constructor = entityClassMetaData.getConstructor();
        T entity = constructor.newInstance();
        entityClassMetaData.getAllFields()
                .forEach(field -> setVar(field, entity, rs));
        return entity;
    }

    @SneakyThrows
    private void setVar(Field field, T entity, ResultSet rs) {
        field.setAccessible(true);
        Class<?> fieldClass = field.getType();
        lookup.unreflectVarHandle(field).set(entity, fieldClass.cast(rs.getObject(field.getName().toLowerCase())));
    }

    @SneakyThrows
    private List<Object> unpack(T entity) {
        return entityClassMetaData.getAllFields()
                .stream()
                .map(field -> getVar(field, entity))
                .toList();
    }

    @SneakyThrows
    private Object getVar(Field field, T entity) {
        field.setAccessible(true);
        return lookup.unreflectVarHandle(field).get(entity);
    }

}
