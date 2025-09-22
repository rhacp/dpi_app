package com.rhacp.dip_app.services.json;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public interface JsonWorkerService {

    Integer getCurrentDpi(String profileName, String slotId);

    List<Integer> getCurrentDpiList(String profileName, String slotId);

    String getJsonSettings();

    void exportJsonSettings(String json);

    String readJsonFromFile(File file);

    HashMap<String, Boolean> checkJsonPropertiesUserConfig(String profileName, String slotId);
}
