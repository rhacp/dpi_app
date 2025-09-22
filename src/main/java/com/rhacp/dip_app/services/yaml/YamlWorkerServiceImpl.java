package com.rhacp.dip_app.services.yaml;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhacp.dip_app.models.UserConfig;
import com.rhacp.dip_app.utils.AppProperties;
import com.rhacp.dip_app.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
public class YamlWorkerServiceImpl implements YamlWorkerService {

    @Qualifier("yamlObjectMapper")
    private final ObjectMapper objectMapper;

    private final AppProperties appProperties;

    public YamlWorkerServiceImpl(ObjectMapper objectMapper, AppProperties appProperties) {
        this.objectMapper = objectMapper;
        this.appProperties = appProperties;
    }

    @Override
    public void saveUserConfigToYaml(UserConfig userConfig) {
        File file = new File(Constants.USER_CONFIG_PATH_WINDOWS);
        file.getParentFile().mkdirs();

        try {
            objectMapper.writeValue(file, userConfig);
        } catch (IOException e) {
            log.info("Failed to save user config to yaml. Method: saveUserConfigToYaml", e);
        }
        log.info("User config saved to {}. Method: saveUserConfigToYaml", Constants.USER_CONFIG_PATH_WINDOWS);
    }

    @Override
    public UserConfig getUserConfigFromYaml() {
        File file = new File(Constants.USER_CONFIG_PATH_WINDOWS);
        UserConfig userConfig;

        if (!file.exists()) {
            log.info("User config not found. Creating default one. Method: getUserConfigFromYaml");
            userConfig = new UserConfig(appProperties.getJsonPath(),
                    appProperties.getProfileName(),
                    appProperties.getSlotId(),
                    appProperties.getDpiUp(),
                    appProperties.getDpiDown(),
                    appProperties.getDpiUpdate());
            saveUserConfigToYaml(userConfig);

            return userConfig;
        }

        try {
            userConfig = objectMapper.readValue(file, UserConfig.class);
            log.info("Loaded user configuration from {}. Method: getUserConfigFromYaml",
                    Constants.USER_CONFIG_PATH_WINDOWS);
        } catch (IOException e) {
            log.info("Failed to load user config, using default. Method: getUserConfigFromYaml", e);
            return new UserConfig(appProperties.getJsonPath(),
                    appProperties.getProfileName(),
                    appProperties.getSlotId(),
                    appProperties.getDpiUp(),
                    appProperties.getDpiDown(),
                    appProperties.getDpiUpdate());
        }

        return userConfig;
    }
}
