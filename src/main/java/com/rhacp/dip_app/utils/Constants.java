package com.rhacp.dip_app.utils;

import java.util.Set;

public final class Constants {

    public static final Set<String> GHUB_PROCESSES = Set.of(
            "lghub.exe",
            "lghub_agent.exe",
            "lghub_updater.exe"
    );

    public static final String USER_CONFIG_PATH_WINDOWS = System.getenv("APPDATA") + "\\dpi_app\\user_settings.yaml";

    public static final String USER_CONFIG_PATH_LINUX_MAC = System.getProperty("user.home") + "/.config/dpi_app/user_settings.yaml";
}
