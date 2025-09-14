package com.rhacp.dip_app.models.transfer.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MouseSettings {

    private DpiTable dpiTable;
}
