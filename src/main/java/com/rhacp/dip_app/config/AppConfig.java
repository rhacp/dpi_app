package com.rhacp.dip_app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AppConfig {

    @Bean
    @Primary
    public ObjectMapper jsonObjectMapper() {
        return new ObjectMapper();
    }

    @Bean(name = "yamlObjectMapper")
    public ObjectMapper yamlObjectMapper() {
        YAMLFactory yamlFactory = new YAMLFactory();
        yamlFactory.disable(YAMLGenerator.Feature.USE_NATIVE_TYPE_ID);
        yamlFactory.disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);
        return new ObjectMapper(yamlFactory);
    }
}
