//package com.rhacp.dip_app.services.user_input;
//
//import com.github.kwhat.jnativehook.GlobalScreen;
//import com.github.kwhat.jnativehook.NativeHookException;
//import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
//import com.github.kwhat.jnativehook.mouse.NativeMouseListener;
//import jakarta.annotation.PostConstruct;
//import org.springframework.stereotype.Service;
//
//@Service
//public class DpiButtonListener implements NativeMouseListener {
//
//    @PostConstruct
//    public void init() {
//        try {
//            // Register global mouse listener
//          {"index":2,"trigger":"simple"}  GlobalScreen.registerNativeHook();
//            GlobalScreen.addNativeMouseListener(this);
//            System.out.println("Mouse debug listener started. Press any button...");
//        } catch (NativeHookException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void nativeMouseClicked(NativeMouseEvent e) {}
//
//    @Override
//    public void nativeMousePressed(NativeMouseEvent e) {
//        // Print the button code whenever any button is pressed
//        System.out.println("Mouse pressed: button code = " + e.getButton());
//    }
//
//    @Override
//    public void nativeMouseReleased(NativeMouseEvent e) {
//        System.out.println("Mouse released: button code = " + e.getButton());
//    }
//}
//
