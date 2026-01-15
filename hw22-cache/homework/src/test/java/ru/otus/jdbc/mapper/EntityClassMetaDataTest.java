package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Manager;
import ru.otus.jdbc.mapper.impl.EntityClassMetaDataImpl;

class EntityClassMetaDataTest {

    private Map<String, EntityClassMetaData<?>> clientEntityClassMetaDataMap;

    @BeforeEach
    void setUp() {
        clientEntityClassMetaDataMap = Map.of(
                "Client", new EntityClassMetaDataImpl<>(Client.class),
                "Manager", new EntityClassMetaDataImpl<>(Manager.class));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Client", "Manager"})
    void getName(String className) {
        Assertions.assertEquals(
                className, clientEntityClassMetaDataMap.get(className).getName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Client", "Manager"})
    void getConstructor(String className) {
        Assertions.assertNotNull(clientEntityClassMetaDataMap.get(className).getConstructor());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Client", "Manager"})
    void getIdField(String className) {
        Field idField = clientEntityClassMetaDataMap.get(className).getIdField();
        Assertions.assertNotNull(idField);
        Assertions.assertFalse(idField.getName().isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Client", "Manager"})
    void getAllFields(String className) {
        List<Field> fields = clientEntityClassMetaDataMap.get(className).getAllFields();
        Assertions.assertNotNull(fields);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Client", "Manager"})
    void getFieldsWithoutId(String className) {
        List<Field> fields = clientEntityClassMetaDataMap.get(className).getFieldsWithoutId();
        Assertions.assertNotNull(fields);
    }
}
