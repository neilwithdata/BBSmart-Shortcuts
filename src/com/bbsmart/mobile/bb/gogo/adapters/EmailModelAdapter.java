package com.bbsmart.mobile.bb.gogo.adapters;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

public class EmailModelAdapter implements IModelAdapter {

    public void paint(Object arg, Graphics graphics) {
        XYRect clip = graphics.getClippingRect();

        String emailAddress = toString(arg);
        
        graphics.drawText(emailAddress, clip.x, clip.y);
    }
    
    public String toString(Object arg) {
        String str = null;

        try {
            str = (String) arg;
        }
        catch (ClassCastException e) {
            str = "";
        }

        return str;
    }

}
