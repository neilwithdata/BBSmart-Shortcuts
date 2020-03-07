package com.bbsmart.mobile.bb.gogo.components;

import com.bbsmart.mobile.bb.gogo.components.menu.Menu;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

public class ColoredListField extends ListField {

	private int activeBackgroundColor = 0x006B86B5;
	private int activeForegroundColor = Color.WHITE;
	private int inactiveBackgroundColor = Color.WHITE;
	private int inactiveForegroundColor = 0x00101010;

	private boolean hasFocus;

	public void makeContextMenu(Menu menu) {
	}

    public int moveFocus(int amount, int status, int time) {
        invalidate(getSelectedIndex());
        return super.moveFocus(amount,status,time);
    }

    public void onFocus(int direction) {
        hasFocus = true;
        super.onFocus(direction);
    }

    public void onUnfocus() {
        hasFocus = false;
        super.onUnfocus();
        invalidate();
    }
    
    public void setActiveBackgroundColor(int color) {
    	this.activeBackgroundColor = color;
    }
    
    public void setActiveForegroundColor(int color) {
    	this.activeForegroundColor = color;
    }

    public void setInactiveBackgroundColor(int color) {
    	this.inactiveBackgroundColor = color;
    }
    
    public void setInactiveForegroundColor(int color) {
    	this.inactiveForegroundColor = color;
    }
    
	// Adapted from KB DB-00472
	protected void paint(Graphics graphics) {
	    if (getSize() == 0) {
	        return;
	    }

        //Get the current clipping region as it will be the only part that requires repainting
        XYRect redrawRect = graphics.getClippingRect();
        if(redrawRect.y < 0) {
            throw new IllegalStateException("Clipping rectangle is wrong.");
        }

        //Determine the start location of the clipping region and end.
        int rowHeight = getRowHeight();

        int curSelected;
        
        //If the ListField has focus determine the selected row.
        if (hasFocus) {
             curSelected = getSelectedIndex();
        } 
        else {
            curSelected = -1;
        }

        int startLine = redrawRect.y / rowHeight;
        int endLine = (redrawRect.y + redrawRect.height - 1) / rowHeight;
        endLine = Math.min(endLine, getSize() - 1);
        int y = startLine * rowHeight;

        ListFieldCallback callback = this.getCallback();
        
    	int curBackgroundColor;
    	int curForegroundColor;

    	// Save default color.
    	int color = graphics.getColor();

        //Draw each row
        for(; startLine <= endLine; ++startLine) {
            if (startLine == curSelected) {
            	curBackgroundColor = activeBackgroundColor;
            	curForegroundColor = activeForegroundColor;
            }
            else {
                curBackgroundColor = inactiveBackgroundColor;
                curForegroundColor = inactiveForegroundColor;
            }
            
            graphics.setColor(curBackgroundColor);
            graphics.fillRect(0, y, getPreferredWidth(), y + rowHeight);

            graphics.setColor(curForegroundColor);
            
            callback.drawListRow(this, graphics, startLine, y, getContentWidth());

            //Assign new values to the y axis moving one row down.
            y += rowHeight;
        }
        
        // Restore default color.
        graphics.setColor(color);
	}

}
