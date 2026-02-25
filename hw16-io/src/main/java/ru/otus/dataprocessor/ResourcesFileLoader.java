package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.InputStream;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final ObjectMapper mapper;
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.mapper = JsonMapper.builder().build();
    }

    @Override
    public List<Measurement> load() {
        try (InputStream inputStream =
                ResourcesFileLoader.class.getClassLoader().getResourceAsStream(fileName)) {
            // читает файл, парсит и возвращает результат
            return mapper.readValue(inputStream, new TypeReference<>() {});
        } catch (Exception e) {
            throw new DataProcessorException(e);
        }
    }
}
