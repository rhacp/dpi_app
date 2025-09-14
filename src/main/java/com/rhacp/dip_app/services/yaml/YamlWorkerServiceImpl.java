package com.rhacp.dip_app.services.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class YamlWorkerServiceImpl implements YamlWorkerService {

    @Qualifier("yamlObjectMapper")
    private final ObjectMapper objectMapper;

    public YamlWorkerServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void saveChanges() {

    }

    @Override
    public void loadYaml() {

    }
}
