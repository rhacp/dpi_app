package com.rhacp.dip_app.services.json;

import java.io.File;
import java.util.List;

public interface JsonWorkerService {

    Integer getCurrentDpi();

    List<Integer> getCurrentDpiList();

    String getJsonSettings();

    void exportJsonSettings(String json);

    String readJsonFromFile(File file);
}
