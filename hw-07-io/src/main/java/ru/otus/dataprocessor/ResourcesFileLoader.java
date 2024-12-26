package ru.otus.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final String fileName;
    private final ObjectMapper mapper = new ObjectMapper();

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        try (var jsonReader = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            return mapper.readValue(jsonReader, new TypeReference<>() {});
        } catch (Exception e) {
            throw new FileProcessException(e);
        }
    }
}
