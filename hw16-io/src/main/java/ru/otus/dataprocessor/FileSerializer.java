package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final ObjectMapper mapper;
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
        this.mapper = JsonMapper.builder().build();
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        try (OutputStream outputStream = new FileOutputStream(fileName)) {
            mapper.writeValue(outputStream, data);
        } catch (Exception e) {
            throw new DataProcessorException(e);
        }
    }
}
