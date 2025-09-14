package com.rhacp.dip_app.models.transfer.profile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Profile {

    private Boolean activeForApplication;

    private String applicationId;

    private ArrayList<Assignment> assignments;

    private String id;

    private String name;
}
