package com.rhacp.dip_app.services.user_input;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.rhacp.dip_app.services.gui.scene.OverlayService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class GlobalHotkeyService implements NativeKeyListener {

    private final OverlayService overlayService;

    public GlobalHotkeyService(OverlayService overlayService) {
        this.overlayService = overlayService;
    }

    @PostConstruct
    public void init() {
        try {
            // Disable logging from JNativeHook
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
            logger.setLevel(java.util.logging.Level.OFF);
            logger.setUseParentHandlers(false);

            // Register global listener
            GlobalScreen.registerNativeHook();
            GlobalScreen.addNativeKeyListener(this);
            log.info("Global hotkey listener started.");
        } catch (Exception e) {
            log.error("Failed to start global hotkey listener.", e);
        }
    }

    @PreDestroy
    public void destroy() {
        try {
            GlobalScreen.unregisterNativeHook();
            log.info("Global hotkey listener stopped.");
        } catch (Exception e) {
            log.error("Failed to stop global hotkey listener.", e);
        }
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();

        switch (keyCode) {
            case NativeKeyEvent.VC_F13 -> overlayService.buttonUpPressed();
            case NativeKeyEvent.VC_F14 -> overlayService.buttonDownPressed();
            case NativeKeyEvent.VC_F15 -> overlayService.updateOverlay();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
    }
}
