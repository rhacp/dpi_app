package com.rhacp.dip_app.services.dpi;

import com.rhacp.dip_app.models.DPI;
import com.rhacp.dip_app.services.json.JsonWorkerService;
import com.rhacp.dip_app.services.user_config.UserConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DPIServiceImpl implements DPIService {

    private final UserConfigService userConfigService;

    private final JsonWorkerService jsonWorkerService;

    public DPIServiceImpl(UserConfigService userConfigService, JsonWorkerService jsonWorkerService) {
        this.userConfigService = userConfigService;
        this.jsonWorkerService = jsonWorkerService;
    }

    @Override
    public DPI getDPIObject() {
        DPI dpi = new DPI();

        String profileName = userConfigService.getUserConfig().getProfileName();
        String slotId = userConfigService.getUserConfig().getSlotId();

        dpi.setCurrentDPI(jsonWorkerService.getCurrentDpi(profileName, slotId));
        dpi.setProfileDPI(jsonWorkerService.getCurrentDpiList(profileName, slotId));
        log.info("DPI object created and returned. Method: getDPIObject");

        return dpi;
    }
}
