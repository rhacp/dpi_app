package com.rhacp.dip_app.services.gui.tray;

import com.rhacp.dip_app.services.user_input.TrayIconClickListenerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.*;

@Slf4j
@Service
public class TraySupportServiceImpl implements TraySupportService {

    private final TrayIconClickListenerService trayIconClickListenerService;

    public TraySupportServiceImpl(TrayIconClickListenerService trayIconClickListenerService) {
        this.trayIconClickListenerService = trayIconClickListenerService;
    }

    @Override
    public void createTray() {
        // Check if tray supported.
        if (!SystemTray.isSupported()) {
            log.info("SystemTray not supported");
            return;
        }

        // Get system tray
        SystemTray tray = SystemTray.getSystemTray();

        // Create tray item
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png"));
        TrayIcon trayIcon = new TrayIcon(image);
        trayIcon.setImageAutoSize(true);

        // Add listener
        trayIcon.addMouseListener(trayIconClickListenerService);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }

        log.info("Icon tray added. Method: createTray");
    }
}
