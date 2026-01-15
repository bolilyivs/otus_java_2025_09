package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.otus.crm.model.Client;
import ru.otus.jdbc.mapper.impl.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.impl.EntitySQLMetaDataImpl;

class EntitySQLMetaDataImplTest {

    EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl(new EntityClassMetaDataImpl<>(Client.class));

    @Test
    void getSelectAllSql() {
        String sql = entitySQLMetaData.getSelectAllSql();
        Assertions.assertTrue(sql.contains("client"));
        Assertions.assertTrue(sql.contains("name"));
    }

    @Test
    void getSelectByIdSql() {
        String sql = entitySQLMetaData.getSelectByIdSql();
        Assertions.assertTrue(sql.contains("client"));
        Assertions.assertTrue(sql.contains("name"));
    }

    @Test
    void getInsertSql() {
        String sql = entitySQLMetaData.getInsertSql();
        Assertions.assertTrue(sql.contains("client"));
        Assertions.assertTrue(sql.contains("name"));
    }

    @Test
    void getUpdateSql() {
        String sql = entitySQLMetaData.getUpdateSql();
        Assertions.assertTrue(sql.contains("client"));
        Assertions.assertTrue(sql.contains("name"));
    }
}
