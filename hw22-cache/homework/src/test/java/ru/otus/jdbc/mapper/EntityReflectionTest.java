package ru.otus.jdbc.mapper;

import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.otus.crm.model.Client;
import ru.otus.jdbc.mapper.impl.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.util.EntityReflection;

class EntityReflectionTest {

    ResultSet resultSet = Mockito.mock(ResultSet.class);
    EntityClassMetaData<Client> entityClassMetaData = new EntityClassMetaDataImpl<>(Client.class);
    EntityReflection<Client> entityReflection = new EntityReflection<>(Client.class);

    @Test
    void createEntity() throws SQLException {
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getObject("id")).thenReturn(1L);
        when(resultSet.getObject("name")).thenReturn("test");
        Client client = entityReflection.createEntity(entityClassMetaData, resultSet);
        Assertions.assertNotNull(client);
    }

    @Test
    void unpack() {
        Client client = new Client();
        client.setId(1L);
        client.setName("test");

        List<Object> values = entityReflection.unpack(entityClassMetaData, client);
        Assertions.assertNotNull(values);
        Assertions.assertEquals(1, values.size());
    }
}
