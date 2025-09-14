package com.rhacp.dip_app.services.gui.scene;

import javafx.stage.Stage;

public interface OverlayService {

    void createOverlay();

    void updateOverlay();

    void deleteOverlay();

    void buttonUpPressed();

    void buttonDownPressed();

    void showOverlay(Stage overlay);
}
