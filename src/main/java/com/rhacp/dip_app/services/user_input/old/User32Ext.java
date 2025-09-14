package com.rhacp.dip_app.services.user_input.old;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.win32.W32APIOptions;

/**
 * INSTANCE is the actual object you use to call the native functions, and the methods are automatically mapped to the real Windows API. You don’t need any class implementing User32Ext.
 * <p>
 * 1. User32Ext INSTANCE = Native.load(...);
 *     This is a public static final field in your interface.
 *     Native.load("user32", User32Ext.class, W32APIOptions.DEFAULT_OPTIONS) tells JNA to:
 *         Load the native user32.dll from Windows.
 *         Create a dynamic proxy object that implements your User32Ext interface.
 *         Handle the native-to-Java bridging automatically, so when you call a method on INSTANCE, it actually calls the real Windows API.
 * <p>
 * 2. The methods in your interface (RegisterHotKeys and UnregisterHotKeys)
 *     JNA dynamically matches these method signatures with the corresponding functions in user32.dll.
 *     When you call INSTANCE.RegisterHotKeys(...), JNA converts your Java types to the proper native types, calls the actual Win32 function, and returns the result.
 * <p>
 * 3. You don’t write any implementation yourself
 *     JNA takes care of everything behind the scenes. The INSTANCE object is essentially a runtime-generated object that knows how to forward calls to the DLL.
 */
public interface User32Ext extends User32 {

    int WM_HOTKEY = 0x0312;

    // Load the native Windows DLL named user32.dll and bind it to my Java interface.
    // W32APIOptions.DEFAULT_OPTIONS ensures correct calling conventions for Windows APIs.
    User32Ext INSTANCE = Native.load("user32", User32Ext.class, W32APIOptions.DEFAULT_OPTIONS);

    // RegisterHotKey function from user32.dll.
    // Returns true if the hotkey was successfully registered, false otherwise.
    // id will be the id we assign to the key we press
    // vk is the virtual key code
    boolean RegisterHotKey(WinDef.HWND hwnd, int id, int fsModifiers, int vk);

    // UnregisterHotKey function from user32.dll
    boolean UnregisterHotKey(WinDef.HWND hwnd, int id);
}
