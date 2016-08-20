package org.dan.cipher.demo;

import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class Dao {
    private final Path location;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public Pojo load(String id) {
        return objectMapper.readValue(location.resolve(id).toFile(), Pojo.class);
    }

    @SneakyThrows
    public void store(Pojo pojo) {
        objectMapper.writeValue(location.resolve(pojo.getId()).toFile(), pojo);
    }
}
