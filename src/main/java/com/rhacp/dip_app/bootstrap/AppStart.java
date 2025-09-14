package com.rhacp.dip_app.bootstrap;

import com.rhacp.dip_app.services.gui.scene.ConfigService;
import com.rhacp.dip_app.services.gui.scene.ContextMenuService;
import com.rhacp.dip_app.services.gui.scene.OverlayService;
import com.rhacp.dip_app.services.gui.tray.TraySupportService;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

/**
 * Spring-managed entry point for starting the JavaFX application.
 * <p>
 * By implementing CommandLineRunner, this class is fully managed by Spring,
 * allowing dependency injection for Overlay, ContextMenu, Config, and TraySupportFx.
 * <p>
 * A JFXPanel is created to initialize the JavaFX runtime without blocking the
 * Spring Boot startup. Platform.setImplicitExit(false) ensures the application
 * continues running even if no JavaFX windows are open.
 * <p>
 * Using Platform.runLater ensures that JavaFX components are created on the
 * JavaFX Application Thread. This avoids launching OverlayFxApp directly,
 * which would bypass Spring and prevent dependency injection.
 * <p>
 * In summary, this Spring-first approach ensures that JavaFX runs within a
 * Spring-managed environment, enabling proper dependency injection and
 * flexible startup control, while still leveraging the JavaFX runtime safely.
 * <p>
 * We cannot use Application.launch(OverlayFxApp.class, args) here because it
 * blocks the current thread (the Spring Boot main thread) and prevents any
 * code after it from running. Additionally, OverlayFxApp would be instantiated
 * by JavaFX, bypassing Spring, so dependency injection would not work.
 * <p>
 * Using JFXPanel initializes the JavaFX runtime without blocking, allowing
 * Spring-managed beans to safely create UI components via Platform.runLater.
 */
@Slf4j
@Service
public class AppStart implements CommandLineRunner {

    private final OverlayService overlayService;

    private final ContextMenuService contextMenuService;

    private final ConfigService configService;

    private final TraySupportService traySupportService;

    public AppStart(OverlayService overlayService, ContextMenuService contextMenuService, ConfigService configService, TraySupportService traySupportService) {
        this.overlayService = overlayService;
        this.contextMenuService = contextMenuService;
        this.configService = configService;
        this.traySupportService = traySupportService;
    }

    @Override
    public void run(String... args) {
        log.info("App started. Method: run");

        new JFXPanel();
        Platform.setImplicitExit(false);

        Platform.runLater(overlayService::createOverlay);
        log.info("Overlay created. Method: run");

        Platform.runLater(contextMenuService::createContextMenu);
        log.info("Tray context menu created. Method: run");

        Platform.runLater(traySupportService::createTray);
        log.info("Icon tray created. Method: run");

        Platform.runLater(configService::createConfig);
        log.info("Config created. Method: run");
    }
}
