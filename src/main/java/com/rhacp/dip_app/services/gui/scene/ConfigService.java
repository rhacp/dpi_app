package com.rhacp.dip_app.services.gui.scene;

public interface ConfigService {

    void createConfig();

    void showAtCoordinates(double x, double y);

    void hideConfig();

    double getTotalHeight();

    double getTotalWidth();
}
