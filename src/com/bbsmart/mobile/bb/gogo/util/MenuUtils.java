package com.bbsmart.mobile.bb.gogo.util;

import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;

import com.bbsmart.mobile.bb.gogo.components.menu.AbstractMenuScreen;

public class MenuUtils {
    
    public static void dismissMenu() {
        UiApplication app = UiApplication.getUiApplication();

        Screen screen = app.getActiveScreen(); 

        while (screen instanceof AbstractMenuScreen) {
            app.popScreen(screen);
            screen = app.getActiveScreen(); 
        }
    }

}
