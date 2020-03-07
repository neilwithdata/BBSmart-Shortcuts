package com.bbsmart.mobile.bb.gogo.components;

import com.bbsmart.mobile.bb.gogo.Config;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

public class Titlebar extends Field {

	private static final int SEPARATOR_BAR_HEIGHT		= 4;

	private String title;
	private int padding;

	public int getPreferredHeight() {
		return Font.getDefault().getHeight() + padding * 2 + SEPARATOR_BAR_HEIGHT;
	}

	public int getPreferredWidth() {
		return Display.getWidth();
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
    protected void onExposed() {
        invalidate();
    }

    protected void layout(int width, int height) {
        int extentWidth = Math.min(getPreferredWidth(), width);
        int extentHeight = Math.min(getPreferredHeight(), height);
        
        setExtent(extentWidth, extentHeight);
    }

    protected void paint(Graphics graphics) {
        XYRect clip = graphics.getClippingRect();
        
        int color = graphics.getColor();

        graphics.setColor(Config.COLOR_TEXT_ACTIVE_BACKGROUND);
        graphics.fillRect(clip.x, clip.y, clip.width, clip.height - SEPARATOR_BAR_HEIGHT);
        
        graphics.setColor(0x006B86B5);
        graphics.fillRect(clip.x, clip.height - SEPARATOR_BAR_HEIGHT, clip.width, SEPARATOR_BAR_HEIGHT);

        Font font = Font.getDefault();
        int xOffset = (getContentWidth() - font.getAdvance(title)) / 2;
        int yOffset = padding;
        
        graphics.setColor(0xFFFFFF);
        graphics.drawText(title, xOffset, yOffset);
        
        graphics.setColor(color);
    }

}
