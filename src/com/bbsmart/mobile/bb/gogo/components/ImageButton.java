package com.bbsmart.mobile.bb.gogo.components;

import com.bbsmart.mobile.bb.gogo.effects.OpacityFilter;
import com.bbsmart.mobile.bb.gogo.screens.Shortcuts;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

public class ImageButton extends AbstractButton {

	protected static final int FOCUS_RECT_WIDTH	= 1;

	private Bitmap current;
	private Bitmap original;
	private Bitmap transformed;
	private int fixedWidth;
	private int fixedHeight;

	public ImageButton() {
        this(0, 0);
    }

	public ImageButton(int width, int height) {
	    fixedWidth = width;
	    fixedHeight = height;
	    shouldDisplayTooltip = true;
	}

	public Bitmap getBitmap() {
		return current;
	}
	
	public int getPreferredHeight() {
	    int preferredHeight = fixedHeight;

	    if (preferredHeight == 0) {
	        preferredHeight = current.getHeight();
	    }

	    return preferredHeight;
	}

	public int getPreferredWidth() {
	    int preferredWidth = fixedWidth;
	    
	    if (preferredWidth == 0) {
	        preferredWidth = current.getWidth();
	    }
	    
	    return preferredWidth;
	}

	public void setBitmap(Bitmap bitmap) {
		original = bitmap;
		current = original;
	}
	
	// Lower value is more transparent.
	public void setOpacity(int opacity) {
	    OpacityFilter opacityFilter = new OpacityFilter(opacity);
	    transformed = new Bitmap(original.getWidth(), original.getHeight());
	    opacityFilter.filter(original, transformed);

	    current = transformed;
	    
	    if (toolTip != null) {
	        Shortcuts screen = (Shortcuts) getScreen();
	        screen.onTooltipDismissRequest();
	        shouldDisplayTooltip = false;
	    }
	}
	
	public void useOriginalBitmap() {
	    current = original;
	    shouldDisplayTooltip = true;
	}

	protected void drawFocus(Graphics graphics, boolean on) {
		XYRect rect = new XYRect();
		getFocusRect(rect);

		int color = graphics.getColor();

		graphics.setColor(Color.WHITE);
		graphics.fillRoundRect(rect.x, rect.y, rect.width, rect.height, 3, 3);

		graphics.setColor(Color.DARKGRAY);
		graphics.drawRoundRect(rect.x, rect.y, rect.width, rect.height, 3, 3);

		graphics.setColor(color);
		
		paint(graphics);
	}

	protected void layout(int width, int height) {
		int myWidth = getPreferredWidth() + padding * 2 + FOCUS_RECT_WIDTH * 2;
		int myHeight = getPreferredHeight() + padding * 2 + FOCUS_RECT_WIDTH * 2;

		int extentWidth = Math.min(myWidth, width);
		int extentHeight = Math.min(myHeight, height);

		setExtent(extentWidth, extentHeight);
	}

    protected void onFocus(int direction) {
        super.onFocus(direction);
        invalidate();
    }

    protected void onUnfocus() {
        super.onUnfocus();
        invalidate();
    }

	protected void paint(Graphics graphics) {
		graphics.getAbsoluteClippingRect(absClippingRect);
		
		XYRect clip = graphics.getClippingRect();

		int bitmapX = (clip.width - current.getWidth()) / 2;
		int bitmapY = (clip.height - current.getHeight()) / 2;
		
		graphics.drawBitmap(bitmapX, bitmapY, clip.width, clip.height, current, 0, 0);
	}

}
