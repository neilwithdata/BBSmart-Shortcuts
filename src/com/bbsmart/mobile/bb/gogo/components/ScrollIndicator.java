package com.bbsmart.mobile.bb.gogo.components;

import net.rim.device.api.ui.Graphics;

public class ScrollIndicator extends ActiveField {

    public static final int TYPE_LEFTPOINTING   = 1;
    public static final int TYPE_RIGHTPOINTING  = 2;
    public static final int TYPE_CIRCLE         = 3;
    
    public static final int DEFAULT_SCROLLBAR_INDICATOR_WIDTH = 8;
    public static final int DEFAULT_SCROLLBAR_INDICATOR_HEIGHT = 8;

    private int type;

    public ScrollIndicator(int type) {
        this.type = type;
    }
    
    public int getPreferredHeight() {
        return DEFAULT_SCROLLBAR_INDICATOR_HEIGHT + padding.top + padding.bottom;
    }
    
    public int getPreferredWidth() {
        return DEFAULT_SCROLLBAR_INDICATOR_WIDTH + padding.left + padding.right;
    }
    
    public int getType() {
        return type;
    }
    
    public void setType(int type) {
        this.type = type;
    }
    
    protected void layout(int width, int height) {
        int extentWidth = 0;
        int extentHeight = 0;
        
        if ((style & ActiveField.USE_ALL_WIDTH) == ActiveField.USE_ALL_WIDTH) {
            extentWidth = width;
        }
        else {
            extentWidth = Math.min(getPreferredWidth(), width);
        }
        
        if ((style & ActiveField.USE_ALL_HEIGHT) == ActiveField.USE_ALL_HEIGHT) {
            extentHeight = height;
        }
        else {
            extentHeight = Math.min(getPreferredHeight(), height);
        }
        
        setExtent(extentWidth, extentHeight);
    }

    protected void paint(Graphics graphics) {
        graphics.getAbsoluteClippingRect(absClippingRect);

        switch (type) {
        case TYPE_LEFTPOINTING:
            paintLeftPointingScrollIndicator(graphics);
            break;
        case TYPE_RIGHTPOINTING:
            paintRightPointingScrollIndicator(graphics);
            break;
        case TYPE_CIRCLE:
            paintCircle(graphics);
            break;
        }
    }
    
    private void paintCircle(Graphics graphics) {
        int contentHeight = getContentHeight();
        
        int yOffset = (contentHeight - DEFAULT_SCROLLBAR_INDICATOR_HEIGHT) / 2;

        int cx = padding.left + (DEFAULT_SCROLLBAR_INDICATOR_WIDTH / 2);
        int cy = contentHeight / 2;
        int px = padding.left + DEFAULT_SCROLLBAR_INDICATOR_WIDTH;
        int py = contentHeight / 2;
        int qx = padding.left + (DEFAULT_SCROLLBAR_INDICATOR_WIDTH / 2);
        int qy = contentHeight - yOffset;

        graphics.setDrawingStyle(Graphics.DRAWSTYLE_AALINES, true);
        graphics.setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, true);

        int origColor = graphics.getColor();
        graphics.setColor(foregroundColor[state]);
        graphics.fillEllipse(cx, cy, px, py, qx, qy, 0, 360);
        graphics.setColor(origColor);
    }

    private void paintLeftPointingScrollIndicator(Graphics graphics) {
        int contentHeight = getContentHeight();

        int yOffset = (contentHeight - DEFAULT_SCROLLBAR_INDICATOR_HEIGHT) / 2;

        int xPts[] = new int[] { padding.left, padding.left + DEFAULT_SCROLLBAR_INDICATOR_WIDTH, padding.left + DEFAULT_SCROLLBAR_INDICATOR_WIDTH };
        int yPts[] = new int[] { contentHeight / 2, yOffset, contentHeight - yOffset };

        byte[] pointTypes = new byte[] { Graphics.CURVEDPATH_END_POINT, 
                Graphics.CURVEDPATH_END_POINT, Graphics.CURVEDPATH_END_POINT };

        graphics.setDrawingStyle(Graphics.DRAWSTYLE_AALINES, true);
        graphics.setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, true);

        int origColor = graphics.getColor();
        graphics.setColor(foregroundColor[state]);
        graphics.drawFilledPath(xPts, yPts, pointTypes, null);
        graphics.setColor(origColor);
    }
    
    private void paintRightPointingScrollIndicator(Graphics graphics) {
        int contentHeight = getContentHeight();

        int yOffset = (contentHeight - DEFAULT_SCROLLBAR_INDICATOR_HEIGHT) / 2;

        int xPts[] = new int[] { padding.left, DEFAULT_SCROLLBAR_INDICATOR_WIDTH, padding.left };
        int yPts[] = new int[] { yOffset, contentHeight / 2, contentHeight - yOffset };

        byte[] pointTypes = new byte[] { Graphics.CURVEDPATH_END_POINT, 
                Graphics.CURVEDPATH_END_POINT, Graphics.CURVEDPATH_END_POINT };

        graphics.setDrawingStyle(Graphics.DRAWSTYLE_AALINES, true);
        graphics.setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, true);

        int origColor = graphics.getColor();
        graphics.setColor(foregroundColor[state]);
        graphics.drawFilledPath(xPts, yPts, pointTypes, null);
        graphics.setColor(origColor);
    }

}
