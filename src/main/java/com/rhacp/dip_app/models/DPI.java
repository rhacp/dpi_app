package com.rhacp.dip_app.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DPI {

    private Integer currentDPI;

    private List<Integer> profileDPI = new ArrayList<Integer>();
}
