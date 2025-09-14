package com.rhacp.dip_app.services.user_config;

import com.rhacp.dip_app.models.UserConfig;
import com.rhacp.dip_app.services.yaml.YamlWorkerService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserConfigServiceImpl implements UserConfigService {

    private UserConfig userConfig;

    private final YamlWorkerService yamlWorkerService;

    public UserConfigServiceImpl(YamlWorkerService yamlWorkerService) {
        this.yamlWorkerService = yamlWorkerService;
    }

    @PostConstruct
    @Override
    public void init() {

    }

    @Override
    public void saveConfigToFile() {

    }

    @Override
    public void getLocalConfig() {

    }

    @Override
    public void updateLocalConfig() {

    }
}
