package com.bbsmart.mobile.bb.gogo.components;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.XYRect;

public class Tooltip {

    private static final int PADDING = 3;

    private int x;
    private int y;
    private int rectWidth;
    private int rectHeight;
    private int textWidth;
    private int textHeight;
    
    private int totalPadding = PADDING * 2;
    private boolean isLayoutValid = false;
    
    private ActiveField owner;
    private Font font = Font.getDefault().derive(Font.PLAIN, 14, Ui.UNITS_px);
    private String text;

    public void paint(Graphics graphics) {
        if (shouldLayout()) {
            layout();
        }

        int origColor = graphics.getColor();
        Font origFont = graphics.getFont();

        graphics.setFont(font);

        graphics.setColor(Color.LIGHTYELLOW);
        graphics.fillRect(x, y, rectWidth, rectHeight);
        
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, rectWidth, rectHeight);
        graphics.drawText(text, x + PADDING, y + PADDING);
        
        graphics.setColor(origColor);
        graphics.setFont(origFont);
    }
    
    public void setOwner(ActiveField field) {
        this.owner = field;
        isLayoutValid = false;
    }
    
    public boolean shouldLayout() {
        return isLayoutValid == false;
    }
    
    protected void layout() {
        setText(owner.getToolTip());
        
        XYRect fieldLocation = owner.getAbsoluteClippingRect();

        int xOffset = fieldLocation.x + 10;
        int yOffset = fieldLocation.y + fieldLocation.height - 10;
        
        textWidth = font.getAdvance(text);
        textHeight = font.getHeight();
        
        rectWidth = textWidth + totalPadding;
        rectHeight = textHeight + totalPadding;
        
        int displayWidth = Display.getWidth();
        int displayHeight = Display.getHeight();
        
        int xOverflow = (xOffset + rectWidth) - displayWidth;
        
        if (xOverflow > 0) {
            xOffset -= xOverflow;
        }
        
        int yOverflow = (yOffset + rectHeight) - displayHeight;
        
        if (yOverflow > 0) {
            yOffset -= yOverflow;
        }
        
        setPosition(xOffset, yOffset);

        isLayoutValid = true;
    }

    protected void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    protected void setText(String text) {
        this.text = text;
    }

}
