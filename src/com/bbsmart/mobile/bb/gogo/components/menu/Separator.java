package com.bbsmart.mobile.bb.gogo.components.menu;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.XYRect;

public class Separator extends AbstractMenuItem {

	public int getPreferredHeight() {
		return 1 + (VERT_PADDING * 2);
	}

	public int getPreferredWidth() {
		return Display.getWidth();
	}

	public void paint(Graphics graphics) {
		XYRect clip = graphics.getClippingRect();
		XYRect extent = getExtent();
		
		int color = graphics.getColor();
		graphics.setColor(Color.BLACK);
		graphics.drawLine(0, clip.y + VERT_PADDING, extent.width, clip.y + VERT_PADDING);
		graphics.setColor(color);
	}

	protected void layout(int width, int height) {
		int extentWidth = Math.min(getPreferredWidth(), width);
		int extentHeight = Math.min(getPreferredHeight(), height);
		
		setExtent(extentWidth, extentHeight);
	}

}
