package com.rhacp.dip_app.models.transfer.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Assignment {

    private String cardId;

    private String slotId;
}
