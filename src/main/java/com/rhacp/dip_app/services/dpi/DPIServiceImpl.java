package com.rhacp.dip_app.services.dpi;

import com.rhacp.dip_app.models.DPI;
import com.rhacp.dip_app.services.json.JsonWorkerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DPIServiceImpl implements DPIService {

    private final JsonWorkerService jsonWorkerService;

    public DPIServiceImpl(JsonWorkerService jsonWorkerService) {
        this.jsonWorkerService = jsonWorkerService;
    }

    @Override
    public DPI getDPIObject() {
        DPI dpi = new DPI();

        dpi.setCurrentDPI(jsonWorkerService.getCurrentDpi());
        dpi.setProfileDPI(jsonWorkerService.getCurrentDpiList());
        log.info("DPI object created and returned. Method: getDPIObject");

        return dpi;
    }
}
