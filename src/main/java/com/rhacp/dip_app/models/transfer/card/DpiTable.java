package com.rhacp.dip_app.models.transfer.card;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DpiTable {

    private int activeDpi;

    private int defaultDpi;

    private List<Integer> levels;

    private int shiftDpi;
}
