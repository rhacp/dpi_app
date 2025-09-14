package com.rhacp.dip_app.services.user_input.old;

import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.event.KeyEvent;

@Slf4j
@Service
public class KeyListenerImpl implements KeyListener {

    private static final int hotKeyIdUp = 1;

    private static final int hotKeyIdDown = 2;

    private final WinDef.HWND hwnd = null;

    @Override
    @PostConstruct
    public void init() {
        boolean upRegister = User32Ext.INSTANCE.RegisterHotKey(hwnd, hotKeyIdUp, 0, KeyEvent.VK_PAGE_UP);
        boolean downRegister = User32Ext.INSTANCE.RegisterHotKey(hwnd, hotKeyIdDown, 0, KeyEvent.VK_PAGE_DOWN);

        // Tell you whether your program successfully told Windows: “Hey, I want to listen for this key.”
        if (!upRegister && !downRegister) {
            log.warn("Failed to load register.");
        } else {
            log.info("Register load up properly.");
        }

        new Thread(this::messageLoop,"HotKey-Listener-Thread").start();
    }

    @Override
    @PreDestroy
    public void destroy() {
        // Unregistered keys
        User32Ext.INSTANCE.UnregisterHotKey(hwnd, hotKeyIdUp);
        User32Ext.INSTANCE.UnregisterHotKey(hwnd, hotKeyIdDown);
        log.info("Register unload properly.");
    }

    private void messageLoop() {
        WinUser.MSG msg = new WinUser.MSG();

        while (true) {
            int result = User32.INSTANCE.GetMessage(msg, null, 0, 0);

            if (result == -1) {
                break;
            }

            //  WM_HOTKEY message which is transmitted in Windows if a key is pressed. Always the same value.
            if (msg.message == User32Ext.WM_HOTKEY) {
                int hotKeyId = msg.wParam.intValue();

                if (hotKeyId == hotKeyIdUp) {
                    log.info("DPI up.");
                }

                if (hotKeyId == hotKeyIdDown) {
                    log.info("DPI down.");
                }
            }
            User32.INSTANCE.TranslateMessage(msg);
            User32.INSTANCE.DispatchMessage(msg);
        }
    }
}
