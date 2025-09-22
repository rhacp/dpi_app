package com.rhacp.dip_app.services.user_config;

import com.rhacp.dip_app.models.UserConfig;

public interface UserConfigService {

    void init();

    void saveLocalUserConfig(UserConfig userConfigReceived);

    void updateLocalConfig();

    UserConfig getUserConfig();

    String getPathFromYaml();

    Boolean checkUserConfig();
}
