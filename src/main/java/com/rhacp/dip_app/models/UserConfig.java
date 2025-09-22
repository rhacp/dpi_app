package com.rhacp.dip_app.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserConfig {

    private String path;

    private String profileName;

    private String slotId;

    private String dpiUp;

    private String dpiDown;

    private String dpiUpdate;
}
