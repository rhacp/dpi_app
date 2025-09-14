package com.rhacp.dip_app.services.gui.scene;

public interface ContextMenuService {

    void createContextMenu();

    void showAtCoordinates(double x, double y);

    void hide();

    double getTotalHeight();
}
