package com.bbsmart.mobile.bb.gogo.adapters;

import com.bbsmart.mobile.bb.gogo.model.AddressBookContact;

import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

public class FullNameModelAdapter implements IModelAdapter {

    public void paint(Object arg, Graphics graphics) {
        String fullName = toString(arg);

        XYRect clip = graphics.getClippingRect();
        
        graphics.drawText(fullName, clip.x, clip.y);
    }
    
    public String toString(Object arg) {
        AddressBookContact contact = null;
        
        try {
            contact = (AddressBookContact) arg;
        }
        catch (ClassCastException e) {
            return null;
        }
        
        return contact.toString();
    }

}
