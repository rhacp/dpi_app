package com.rhacp.dip_app.services.user_config;

public interface UserConfigService {

    void init();

    void saveConfigToFile();

    void getLocalConfig();

    void updateLocalConfig();
}
