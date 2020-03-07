package com.bbsmart.mobile.bb.gogo.components.menu;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.util.MenuUtils;

import net.rim.device.api.system.Characters;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.util.CharacterUtilities;

public class MenuItem extends AbstractMenuItem {

	private static final int ACCELERATOR_GRAPHIC_HEIGHT    = 16;
	private static final int ACCELERATOR_GRAPHIC_WIDTH     = 16;
	private static final int SUBMENU_INDICATOR_WIDTH       = 10;

	private Font acceleratorFont = Font.getDefault().derive(Font.PLAIN, 13, Ui.UNITS_px);
	protected char accelerator;
	protected Runnable runnable;
	protected String text;
	protected int textColor;
	protected int translation;

	public MenuItem(String text) {
		this(text, (char)0);
	}
	
	public MenuItem(String text, char accelerator) {
		super(Field.FOCUSABLE);

		this.text = text;
		this.accelerator = accelerator;
	}
	
	public int getPreferredHeight() {
		int contentHeight = Math.max(ACCELERATOR_GRAPHIC_HEIGHT, acceleratorFont.getHeight());
		
		return contentHeight + (VERT_PADDING * 2);
	}

	public int getPreferredWidth() {
	    // Always reserve space for an accelerator...
	    int acceleratorWidth = ACCELERATOR_GRAPHIC_WIDTH + HORZ_PADDING;

	    int subMenuIndicatorWidth = hasSubMenu() ? SUBMENU_INDICATOR_WIDTH + HORZ_PADDING : 0;
	    
	    int preferred = 
	        HORZ_PADDING +
	        acceleratorWidth +
	        Font.getDefault().getAdvance(text) +
	        HORZ_PADDING +
	        subMenuIndicatorWidth;

		return preferred;
	}
	
	public char getAccelerator() {
		return accelerator;
	}

	public Runnable getRunnable() {
		return runnable;
	}

	public boolean hasAccelerator() {
		return accelerator != 0;
	}
	
	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}
	
	protected boolean keyChar(char c, int status, int time) {
	    if (c == Characters.ENTER) {
	        navigationClick(status, time);
	    }

	    return super.keyChar(c, status, time);
	}

	protected void layout(int width, int height) {
		int extentWidth = width;
		int extentHeight = Math.min(getPreferredHeight(), height);
		
		setExtent(extentWidth, extentHeight);
	}
	
	//
	// If we're really a submenu then bring it up, otherwise execute our runnable.
	//
	protected boolean navigationClick(int status, int time) {
		if (hasSubMenu()) {
			SubMenu subMenu = getSubMenu();
			SubMenuScreen activeSubMenuScreen = new SubMenuScreen(getScreen(), subMenu);
			UiApplication.getUiApplication().pushScreen(activeSubMenuScreen);
			return true;
		}
		
		MenuUtils.dismissMenu();

		if (runnable != null) {
			runnable.run();
		}

		return true;
	}

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
	    if (dx > 0) {
	        if (hasSubMenu()) {
	            SubMenu subMenu = getSubMenu();
	            SubMenuScreen activeSubMenuScreen = new SubMenuScreen(getScreen(), subMenu);
	            UiApplication.getUiApplication().pushScreen(activeSubMenuScreen);
	            return true;
	        }
	    }

	    return super.navigationMovement(dx, dy, status, time);
	}

	protected void onFocus(int direction) {
		isActive = true;
		invalidate();
	}

	protected void onUnfocus() {
		isActive = false;
		invalidate();
	}
	
	protected void paint(Graphics graphics) {
		XYRect clip = graphics.getClippingRect();
		XYRect absClip = new XYRect();
		graphics.getAbsoluteClippingRect(absClip);
		XYRect extent = getExtent();
		
		translation = extent.x - clip.x;
		
		int color = graphics.getColor();

		if (isActive) {
			textColor = Color.WHITE;
			paintActiveMenuBackground(graphics);
		}
		else {
		    textColor = Config.COLOR_TEXT_ACTIVE_BACKGROUND;
		}

		int xOffset = HORZ_PADDING;

		if (hasAccelerator()) {
			XYRect region = new XYRect(xOffset, 0, ACCELERATOR_GRAPHIC_WIDTH, extent.height);
			graphics.pushRegion(region);
			paintAcceleratorGraphic(graphics);
			graphics.popContext();
		}
		
		xOffset += ACCELERATOR_GRAPHIC_WIDTH + HORZ_PADDING;

		XYRect region = new XYRect(xOffset, 0, extent.width, extent.height);
		graphics.pushRegion(region);
		paintText(graphics);
		graphics.popContext();
		
		if (hasSubMenu()) {
		    xOffset = extent.width - SUBMENU_INDICATOR_WIDTH - HORZ_PADDING;
			region = new XYRect(xOffset, extent.y, SUBMENU_INDICATOR_WIDTH, extent.height);
			graphics.pushRegion(region);
			paintSubMenuIndicator(graphics);
			graphics.popContext();
		}
		
		graphics.setColor(color);
	}

	protected void paintActiveMenuBackground(Graphics graphics) {
		XYRect clip = graphics.getClippingRect();
		
		int color = graphics.getColor();
		//graphics.setColor(Config.COLOR_TEXT_ACTIVE_BACKGROUND);
		// try this
		graphics.setColor(0x006B86B5);
		graphics.fillRect(clip.x, clip.y, clip.width, clip.height);
		graphics.setColor(color);
	}
	
	protected void paintAcceleratorGraphic(Graphics graphics) {
	    char uppercaseAccelerator = CharacterUtilities.toUpperCase(accelerator);

		XYRect clip = graphics.getClippingRect();

		int xOffset = (clip.width - acceleratorFont.getAdvance(uppercaseAccelerator)) / 2;
		int yOffset = (clip.height - acceleratorFont.getHeight()) / 2 + 1;
		
		graphics.setColor(textColor);
		graphics.setFont(acceleratorFont);
		graphics.drawText(uppercaseAccelerator, xOffset, yOffset, 0, clip.width);
		graphics.setFont(Font.getDefault());
		
		xOffset = (clip.width - ACCELERATOR_GRAPHIC_WIDTH) / 2;
		yOffset = (clip.height - ACCELERATOR_GRAPHIC_HEIGHT) / 2;

		graphics.drawRoundRect(xOffset, yOffset, 
				ACCELERATOR_GRAPHIC_WIDTH, ACCELERATOR_GRAPHIC_HEIGHT, 3, 3);
	}
	
	protected void paintSubMenuIndicator(Graphics graphics) {
		XYRect clip = graphics.getClippingRect();
		int indicatorHeight = acceleratorFont.getHeight() - 4;
		
		int xOffset = clip.x;
		int yOffset = (clip.height - indicatorHeight) / 2;
		
		int xPts[] = new int[] { xOffset, clip.width, xOffset };
		int yPts[] = new int[] { yOffset, clip.height / 2, clip.height - yOffset };
		byte[] pointTypes = new byte[] { Graphics.CURVEDPATH_END_POINT, 
		        Graphics.CURVEDPATH_END_POINT, Graphics.CURVEDPATH_END_POINT };
		
		graphics.setDrawingStyle(Graphics.DRAWSTYLE_AALINES, true);
		graphics.setDrawingStyle(Graphics.DRAWSTYLE_AAPOLYGONS, true);

		graphics.drawFilledPath(xPts, yPts, pointTypes, null);
	}
	
	protected void paintText(Graphics graphics) {
		XYRect clip = graphics.getClippingRect();
		Font font = Font.getDefault();

		int xOffset = clip.x;
		int yOffset = (clip.height - font.getHeight()) / 2;
		
		graphics.setColor(textColor);
		graphics.translate(translation, 0);
		graphics.drawText(text, xOffset, yOffset);
	}
	
}
