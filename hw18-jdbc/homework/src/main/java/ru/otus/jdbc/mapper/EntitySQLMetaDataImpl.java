package ru.otus.jdbc.mapper;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private final EntityClassMetaData<?> entityClassMetaData;

    @Override
    public String getSelectAllSql() {
        String fields = getFieldsSqlPart(entityClassMetaData.getAllFields());
        String tableName = entityClassMetaData.getName();

        return "SELECT %s from %s".formatted(fields, tableName);
    }

    @Override
    public String getSelectByIdSql() {
        String idField = entityClassMetaData.getIdField().getName();
        return "%s where %s = ?".formatted(getSelectAllSql(), idField);
    }

    @Override
    public String getInsertSql() {
        String fields = getFieldsSqlPart(entityClassMetaData.getFieldsWithoutId());
        String tableName = entityClassMetaData.getName();
        String fieldValues = "?".repeat(entityClassMetaData.getFieldsWithoutId().size());
        return "insert into %s(%s) values (%s)".formatted(tableName, fields, fieldValues);
    }

    @Override
    public String getUpdateSql() {
        String fields = getFieldsSqlPart(entityClassMetaData.getFieldsWithoutId(), " = ?");
        String tableName = entityClassMetaData.getName();
        String idField = entityClassMetaData.getIdField().getName();
        return "update %s set %s where %s = ?".formatted(tableName, fields, idField);
    }

    private String getFieldsSqlPart(List<Field> fieldList) {
        return getFieldsSqlPart(fieldList, "");
    }

    private String getFieldsSqlPart(List<Field> fieldList, String postFix) {
        return fieldList.stream()
                .filter(Objects::nonNull)
                .map(field -> field.getName() + postFix)
                .collect(Collectors.joining(", "));
    }
}
