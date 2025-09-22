package com.rhacp.dip_app.utils;

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;

import java.util.HashMap;
import java.util.Map;

public class KeyMapper {

    private static final Map<String, Integer> KEY_MAP = Map.ofEntries(
            Map.entry("F1", NativeKeyEvent.VC_F1),
            Map.entry("F2", NativeKeyEvent.VC_F2),
            Map.entry("F3", NativeKeyEvent.VC_F3),
            Map.entry("F4", NativeKeyEvent.VC_F4),
            Map.entry("F5", NativeKeyEvent.VC_F5),
            Map.entry("F6", NativeKeyEvent.VC_F6),
            Map.entry("F7", NativeKeyEvent.VC_F7),
            Map.entry("F8", NativeKeyEvent.VC_F8),
            Map.entry("F9", NativeKeyEvent.VC_F9),
            Map.entry("F10", NativeKeyEvent.VC_F10),
            Map.entry("F11", NativeKeyEvent.VC_F11),
            Map.entry("F12", NativeKeyEvent.VC_F12),
            Map.entry("F13", NativeKeyEvent.VC_F13),
            Map.entry("F14", NativeKeyEvent.VC_F14),
            Map.entry("F15", NativeKeyEvent.VC_F15),
            Map.entry("F16", NativeKeyEvent.VC_F16),
            Map.entry("F17", NativeKeyEvent.VC_F17),
            Map.entry("F18", NativeKeyEvent.VC_F18),
            Map.entry("F19", NativeKeyEvent.VC_F19),
            Map.entry("F20", NativeKeyEvent.VC_F20),
            Map.entry("F21", NativeKeyEvent.VC_F21),
            Map.entry("F22", NativeKeyEvent.VC_F22),
            Map.entry("F23", NativeKeyEvent.VC_F23),
            Map.entry("F24", NativeKeyEvent.VC_F24)
    );

    public static Integer getKey(String keyName) {
        if (keyName == null) {
            return -1;
        }

        return KEY_MAP.get(keyName);
    }
}
