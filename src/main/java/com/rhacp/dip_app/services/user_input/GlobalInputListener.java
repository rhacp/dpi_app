//package com.rhacp.dip_app.services.user_input;
//
//import com.github.kwhat.jnativehook.GlobalScreen;
//import com.github.kwhat.jnativehook.NativeHookException;
//import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
//import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
//import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
//import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
//import com.rhacp.dip_app.services.scenes.Overlay;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.HashSet;
//import java.util.Set;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//@Slf4j
//@Service
//public class GlobalInputListener implements NativeKeyListener, NativeMouseListener {
//
//    private final Set<Integer> pressedKeys = new HashSet<>();
//
//    private final Overlay overlay;
//
//    public GlobalInputListener(Overlay overlay) {
//        this.overlay = overlay;
//    }
//
//    @PostConstruct
//    public void startListener() {
//        try {
//            // Disable noisy JNativeHook logging
//            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
//            logger.setLevel(Level.WARNING);
//            logger.setUseParentHandlers(false);
//
//            GlobalScreen.registerNativeHook();
//            GlobalScreen.addNativeKeyListener(this);
//            GlobalScreen.addNativeMouseListener(this);
//
//            log.info("Global input listener started.");
//        } catch (NativeHookException e) {
//            throw new RuntimeException("Failed to register native hook", e);
//        }
//    }
//
//    @PreDestroy
//    public void stopListener() {
//        try {
//            GlobalScreen.unregisterNativeHook();
//            log.info("Global input listener stopped.");
//        } catch (NativeHookException e) {
//            e.printStackTrace();
//        }
//    }
//
//    // Keyboard events
//    @Override
//    public void nativeKeyPressed(NativeKeyEvent e) {
////        pressedKeys.add(e.getKeyCode());
////        System.out.println(e.getKeyCode());
////
////        if (pressedKeys.contains(NativeKeyEvent.VC_CAPS_LOCK)
////                && pressedKeys.contains(NativeKeyEvent.VC_F10)
////                && pressedKeys.contains(NativeKeyEvent.VC_F11)) {
////            System.out.println("Global key pressed 1.");
////        }
////
////        if (pressedKeys.contains(NativeKeyEvent.VC_F22)
////                && pressedKeys.contains(NativeKeyEvent.VC_F21)
////                && pressedKeys.contains(NativeKeyEvent.VC_F9)
////                && pressedKeys.contains(NativeKeyEvent.VC_F8)) {
////            System.out.println("Global key pressed 2.");
////        }
//
//        pressedKeys.add(e.getKeyCode());
//
//        if (pressedKeys.contains(NativeKeyEvent.VC_F13)) {
//            overlay.buttonUpPressed();
//            log.info("up");
//            pressedKeys.remove(NativeKeyEvent.VC_F13);
//        }
//
//        if (pressedKeys.contains(NativeKeyEvent.VC_F14)) {
//            overlay.buttonDownPressed();
//            log.info("down");
//            pressedKeys.remove(NativeKeyEvent.VC_F14);
//        }
//    }
//
//    @Override
//    public void nativeKeyReleased(NativeKeyEvent e) {
//    }
//
//    @Override
//    public void nativeKeyTyped(NativeKeyEvent e) {
//    }
//
//    // Mouse events
//    @Override
//    public void nativeMousePressed(NativeMouseEvent e) {
////        System.out.println(e.getButton());
//    }
//
//    @Override
//    public void nativeMouseReleased(NativeMouseEvent e) {
//    }
//
//    @Override
//    public void nativeMouseClicked(NativeMouseEvent e) {
//    }
//
////    public void dpiUpPressed(NativeKeyEvent e) {
////        pressedKeys.add(e.getKeyCode());
////
////        if (pressedKeys.contains(NativeKeyEvent.VC_PAGE_UP)) {
////            overlay.buttonUpPressed();
////        }
////    }
////
////    public void dpiDownPressed(NativeKeyEvent e) {
////        pressedKeys.add(e.getKeyCode());
////
////        if (pressedKeys.contains(NativeKeyEvent.VC_PAGE_DOWN)) {
////            overlay.buttonDownPressed();
////        }
////    }
//}
