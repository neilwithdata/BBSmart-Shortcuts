package com.bbsmart.mobile.bb.gogo.adapters;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.model.AddressBookContact.PhoneNumber;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

public class PhoneNumberModelAdapter implements IModelAdapter {

    private String longestAttributeLabel;
    private boolean isLayoutValid = false;
    private int longestAttributeLength = 0;
    
    public void paint(Object arg, Graphics graphics) {
        if (! isLayoutValid) {
            layout(graphics);
        }

        XYRect clip = graphics.getClippingRect();

        PhoneNumber phoneNumber = (PhoneNumber) arg;
        
        int xOffset = clip.x;
        
        if (phoneNumber.attr != null) {
            graphics.drawText(phoneNumber.attr + Config.RSC_STRING_CONTACT_SEPARATOR, xOffset, clip.y);
        }
        
        xOffset += longestAttributeLength;
        
        graphics.drawText(phoneNumber.number, xOffset, clip.y);
    }
    
    public void setLongestAttributeLabel(String longestAttributeLabel) {
        this.longestAttributeLabel = longestAttributeLabel;
    }

    public String toString(Object arg) {
        String str = null;

        try {
            PhoneNumber phoneNumber = (PhoneNumber) arg;
            str = phoneNumber.number;
        }
        catch (ClassCastException e) {
            str = "";
        }

        return str;
    }
    
    private void layout(Graphics graphics) {
        Font font = graphics.getFont();
        longestAttributeLength = font.getAdvance(longestAttributeLabel);
        isLayoutValid = true;
    }

}
