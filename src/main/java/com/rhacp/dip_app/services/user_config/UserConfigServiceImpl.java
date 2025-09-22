package com.rhacp.dip_app.services.user_config;

import com.rhacp.dip_app.exceptions.JsonFileReadException;
import com.rhacp.dip_app.models.UserConfig;
import com.rhacp.dip_app.services.jdbc.JdbcTemplateProviderService;
import com.rhacp.dip_app.services.json.JsonWorkerService;
import com.rhacp.dip_app.services.yaml.YamlWorkerService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class UserConfigServiceImpl implements UserConfigService {

    private final JsonWorkerService jsonWorkerService;

    private final JdbcTemplateProviderService jdbcTemplateProviderService;

    private final YamlWorkerService yamlWorkerService;

    private UserConfig userConfig;

    public UserConfigServiceImpl(JsonWorkerService jsonWorkerService, JdbcTemplateProviderService jdbcTemplateProviderService, YamlWorkerService yamlWorkerService) {
        this.jsonWorkerService = jsonWorkerService;
        this.jdbcTemplateProviderService = jdbcTemplateProviderService;
        this.yamlWorkerService = yamlWorkerService;
    }


    @PostConstruct
    @Override
    public void init() {
        userConfig = yamlWorkerService.getUserConfigFromYaml();
    }

    @Override
    public void saveLocalUserConfig(UserConfig userConfigReceived) {
        userConfig = userConfigReceived;
        yamlWorkerService.saveUserConfigToYaml(userConfig);
        log.info("Saving local user config. Method: saveLocalUserConfig");
        jdbcTemplateProviderService.updateJdbcTemplateSource(userConfig.getPath(), getPathFromYaml());
    }

    @Override
    public void updateLocalConfig() {
        log.info("Updating local user config. Method: updateLocalConfig");
        UserConfig userConfigYaml = yamlWorkerService.getUserConfigFromYaml();

        if (userConfigYaml.equals(userConfig)) {
            return;
        }

        updateUserConfigObject(userConfigYaml);
    }

    @Override
    public UserConfig getUserConfig() {
//        log.info("Local user config retrieved. Method: getUserConfig");
        return userConfig;

//        return null;
    }

    @Override
    public String getPathFromYaml() {
        log.info("Local path retrieved. Method: getPathFromYaml");
        return yamlWorkerService.getUserConfigFromYaml().getPath();
    }

    @Override
    public Boolean checkUserConfig() {
        log.info("Checking local user config. Method: checkUserConfig");

        File file = new File(userConfig.getPath());
        if (!file.exists() || !file.isFile()) {
            log.error("Bad user configuration (path file does not exist): \"{}\". Method: checkUserConfig", file.getAbsolutePath());
            return false;
        }

        if (!file.getName().endsWith(".db")) {
            log.error("Bad user configuration (path file not ending with \".db\": \"{}\". Method: checkUserConfig", file.getAbsolutePath());
            return false;
        }

        jdbcTemplateProviderService.init(userConfig.getPath());

        try {
            jdbcTemplateProviderService.getJdbcTemplate()
                    .queryForObject("SELECT name FROM sqlite_master WHERE type='table' AND name='data';", String.class);

            List<String> columnList = jdbcTemplateProviderService.getJdbcTemplate()
                    .query("PRAGMA table_info(data);", (rs, rowNum) -> rs.getString("name"));

            if (columnList.stream()
                    .noneMatch(element -> element.equals("file"))) {
                log.error("Bad user configuration (field \"file\" not found in \"data\" table): \"{}\". Method: checkUserConfig", file.getAbsolutePath());
                return false;
            }
        } catch (DataAccessException e) {
            log.error("Bad user configuration (table \"data\" not found or DB invalid): \"{}\". Method: checkUserConfig", file.getAbsolutePath(), e);
            return false;
        }

        HashMap<String, Boolean> map = jsonWorkerService.checkJsonPropertiesUserConfig(
                userConfig.getProfileName(),
                userConfig.getSlotId());

        if (map.containsKey("profileName")) {
            log.error("Bad user configuration (Profile Name): \"{}\". Method: checkUserConfig", userConfig.getProfileName());
            return false;
        }

        if (map.containsKey("slotId")) {
            log.error("Bad user configuration (Slot Id): \"{}\". Method: checkUserConfig", userConfig.getSlotId());
            return false;
        }

        return true;
    }

    private void updateUserConfigObject(UserConfig userConfigReceived) {
        if (!userConfigReceived.getPath().equals(userConfig.getPath())) {
            userConfig.setPath(userConfig.getPath());
        }

        if (!userConfigReceived.getProfileName().equals(userConfig.getProfileName())) {
            userConfig.setProfileName(userConfig.getProfileName());
        }

        if (!userConfigReceived.getSlotId().equals(userConfig.getSlotId())) {
            userConfig.setSlotId(userConfig.getSlotId());
        }

        if (!userConfigReceived.getDpiUp().equals(userConfig.getDpiUp())) {
            userConfig.setDpiUp(userConfig.getDpiUp());
        }

        if (!userConfigReceived.getDpiDown().equals(userConfig.getDpiDown())) {
            userConfig.setDpiDown(userConfig.getDpiDown());
        }

        if (!userConfigReceived.getDpiUpdate().equals(userConfig.getDpiUpdate())) {
            userConfig.setDpiUpdate(userConfig.getDpiUpdate());
        }
    }
}
