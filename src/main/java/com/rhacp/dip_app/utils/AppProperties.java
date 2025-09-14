package com.rhacp.dip_app.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class AppProperties {

    @Value("${app.json.path-one}")
    private String jsonPathOne;

    @Value("${app.json.profileName}")
    private String profileName;

    @Value("${app.json.slotId}")
    private String slotId;

    @Value("${app.configuration.dpiUp}")
    private String dpiUp;

    @Value("${app.configuration.dpiDown}")
    private String dpiDown;
}
