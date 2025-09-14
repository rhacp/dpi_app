package com.rhacp.dip_app.services.gui.component;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

public interface TopBarService {

    HBox createTopBar(Stage stage);

    Region createSeparator();
}
