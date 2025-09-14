package com.rhacp.dip_app.services.user_input;

import com.rhacp.dip_app.services.gui.scene.ContextMenuService;
import javafx.application.Platform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

@Slf4j
@Service
public class TrayIconClickListenerService extends MouseAdapter {

    private final ContextMenuService contextMenuService;

    public TrayIconClickListenerService(ContextMenuService contextMenuService) {
        this.contextMenuService = contextMenuService;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if ((e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
                || (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3)) {
            Platform.runLater(() -> {
                contextMenuService.showAtCoordinates(e.getXOnScreen(), e.getYOnScreen() - contextMenuService.getTotalHeight());
            });
        }
    }
}

