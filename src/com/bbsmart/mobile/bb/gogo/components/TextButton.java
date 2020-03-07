package com.bbsmart.mobile.bb.gogo.components;


import com.bbsmart.mobile.bb.gogo.screens.Shortcuts;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

public class TextButton extends AbstractButton {
    
    private int opacity;

	public TextButton() {
	    this(0);
	}
	
	public TextButton(long style) {
	    super(style);

	    state = STATE_UNFOCUSED;

	    font[STATE_FOCUSED] = Font.getDefault();
	    font[STATE_UNFOCUSED] = Font.getDefault();

	    backgroundColor[STATE_FOCUSED] = Color.WHITE;
	    backgroundColor[STATE_UNFOCUSED] = Color.WHITE;

	    foregroundColor[STATE_FOCUSED] = Color.BLACK;
	    foregroundColor[STATE_UNFOCUSED] = Color.BLACK;

	    alignment = ALIGN_LEFT;
	    
	    opacity = 0xFF; // Fully opaque
	}

	public int getPreferredHeight() {
		return Font.getDefault().getHeight() + (padding * 2);
	}

	public int getPreferredWidth() {
		return Display.getWidth() + (padding * 2);
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

    public void onFocus(int direction) {
        super.onFocus(direction);
        invalidate();
    }

    public void onUnfocus() {
        super.onUnfocus();
        invalidate();
    }
    
    // Lower value is more transparent.
    public void setOpacity(int opacity) {
        this.opacity = opacity;

        if (toolTip != null) {
            Shortcuts screen = (Shortcuts) getScreen();
            screen.onTooltipDismissRequest();
            shouldDisplayTooltip = false;
        }
    }
    
    protected void layout(int width, int height) {
        int extentWidth = Math.min(getPreferredWidth(), width);
        int extentHeight = Math.min(getPreferredHeight(), height);

        setExtent(extentWidth, extentHeight);
    }

	protected void paint(Graphics graphics) {
		graphics.getAbsoluteClippingRect(absClippingRect);

		int color = graphics.getColor();
		int alpha = graphics.getGlobalAlpha();

		graphics.setGlobalAlpha(opacity);
		graphics.setColor(backgroundColor[state]);
		graphics.fillRect(0, 0, getWidth(), getHeight());

		int xOffset = getXOffset();
		int yOffset = getYOffset();

		graphics.setColor(foregroundColor[state]);
		graphics.drawText(text, xOffset, yOffset);

		graphics.setColor(color);
		graphics.setGlobalAlpha(alpha);
	}
	
	private int getXOffset() {
	    int xOffset = 0;

	    if ((alignment & ALIGN_LEFT) == ALIGN_LEFT) {
	        xOffset = padding;
	    }
	    else if ((alignment & ALIGN_CENTER) == ALIGN_CENTER) {
	        xOffset = (getContentWidth() - Font.getDefault().getAdvance(text)) / 2;
	    }
	    else if ((alignment & ALIGN_RIGHT) == ALIGN_RIGHT) {
	        xOffset = getContentWidth() - Font.getDefault().getAdvance(text) - padding;
	    }
	    
	    return xOffset;
	}
	
	private int getYOffset() {
	    int yOffset = 0;

	    if ((alignment & ALIGN_TOP) == ALIGN_TOP) {
	        yOffset = padding;
	    }
	    else if ((alignment & ALIGN_MIDDLE) == ALIGN_MIDDLE) {
	        yOffset = (getContentHeight() - Font.getDefault().getHeight()) / 2;
	    }
	    else if ((alignment & ALIGN_BOTTOM) == ALIGN_BOTTOM) {
	        yOffset = getContentHeight() - Font.getDefault().getHeight() - padding;
	    }

	    return yOffset;
	}

}
