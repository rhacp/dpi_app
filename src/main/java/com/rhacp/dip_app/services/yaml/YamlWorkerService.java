package com.rhacp.dip_app.services.yaml;

import com.rhacp.dip_app.models.UserConfig;

public interface YamlWorkerService {

    void saveUserConfigToYaml(UserConfig userConfig);

    UserConfig getUserConfigFromYaml();
}
