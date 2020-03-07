package com.bbsmart.mobile.bb.gogo.components.menu;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.util.MenuUtils;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.container.VerticalFieldManager;

public abstract class AbstractMenuScreen extends Screen {
	
	public static final int COLOR_BACKGROUND	= 0x003955CE;
	public static final int COLOR_HIGHLIGHT	= 0x0031A2EF;

	protected static final int WINDOW_GLOBAL_ALPHA		= 0xC0;
	protected static final int WINDOW_COLOR_FILL		= 0x00525252;
	protected static final int WINDOW_COLOR_OUTLINE		= 0x00C6C6C6;

	public static final int PADDING_LEFT = 5;
	public static final int PADDING_RIGHT = 5;
	public static final int PADDING_TOP = 2;
	public static final int PADDING_BOTTOM = 2;
	public static final int BORDER_WIDTH = 1;

	protected Bitmap background;
	protected Bitmap border;
	protected Menu menu = null;
	
	protected int menuWidth = 0;
	protected int menuHeight = 0;
	protected int windowXOffsetPercent = 0;

	public AbstractMenuScreen(Menu menu) {
		super(new VerticalFieldManager());

		this.menu = menu;
		
		add(menu);
	}

	// Quick & Ugly
    protected void createBackground() {
        background = new Bitmap(getWidth(), getHeight());
        border = new Bitmap(getWidth(), getHeight());

        Graphics backgroundGraphics = new Graphics(background);
        XYRect backgroundClip = backgroundGraphics.getClippingRect();
        
        backgroundGraphics.setColor(0x00E7EBF7);
        backgroundGraphics.fillRect(backgroundClip.x, backgroundClip.y, backgroundClip.width, backgroundClip.height);

        Graphics borderGraphics = new Graphics(border);
        XYRect borderClip = backgroundGraphics.getClippingRect();

        borderGraphics.setColor(Config.COLOR_TEXT_ACTIVE_BACKGROUND);
        borderGraphics.drawRect(borderClip.x, borderClip.y, borderClip.width, borderClip.height);
    }

	protected boolean keyChar(char c, int status, int time) {
		int menuItems = menu.getFieldCount();
		
		for (int i = 0; i < menuItems; i++) {
			AbstractMenuItem unknownItem = (AbstractMenuItem) menu.get(i);
			if (unknownItem instanceof MenuItem) {
				MenuItem item = (MenuItem) unknownItem;
				if (item.hasAccelerator()) {
					if (item.getAccelerator() == c) {
						Runnable runnable = item.getRunnable();
						if (runnable != null) {
						    MenuUtils.dismissMenu();
							runnable.run();
						}
						return true;
					}
				}
			}
		}

		return super.keyChar(c, status, time);
	}

	protected boolean keyDown(int keycode, int time) {
		int key = Keypad.key(keycode);

		if (key == Keypad.KEY_ESCAPE || key == Keypad.KEY_MENU) {
			synchronized (UiApplication.getEventLock()) {
				UiApplication.getUiApplication().popScreen(this);
			}
			return true;
		}
		
		return false;
	}
	
	protected void onUndisplay() {
        // Need to delete the menu object from this field manager so it can
        // be reused later.
	    delete(menu);
	}
	
//	protected void paintBackground(Graphics graphics) {
//		XYRect clip = graphics.getClippingRect();
//		
//		int color = graphics.getColor();
//
//		graphics.setColor(0x00E7EBF7);
//		graphics.fillRect(clip.x, clip.y, clip.width, clip.height);
//		
//		graphics.setColor(color);
//	}
	
	protected void sublayout(int maxWidth, int maxHeight) {
		if (menu == null) {
			throw new IllegalArgumentException("no menu specified");
		}
		
		layoutDelegate(maxWidth - (BORDER_WIDTH * 2), maxHeight - (BORDER_WIDTH * 2));
		setPositionDelegate(BORDER_WIDTH, BORDER_WIDTH);
		
		menuWidth = getDelegate().getWidth();
		menuHeight = getDelegate().getHeight();

		int xOffset = Display.getWidth() * windowXOffsetPercent / 100;
		int yOffset = Display.getHeight() - menuHeight;
			
		setPosition(xOffset, yOffset);
		setExtent(menuWidth, menuHeight);
	}
	
    protected void paint(Graphics graphics) {
        if (background == null) {
            createBackground();
        }

        XYRect extent = getExtent();
        graphics.rop(Graphics.ROP_SRC_COPY, 0, 0, extent.width, extent.height, background, 0, 0);
        
        super.paint(graphics);

        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, extent.width, extent.height);
    }

}
