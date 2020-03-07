package com.bbsmart.mobile.bb.gogo.components.menu;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;

public final class SubMenuScreen extends AbstractMenuScreen {

	public static final int ARROW_WIDTH = 10;
	public static final int ARROW_HEIGHT = 10;
	
	private Screen parent;

	public SubMenuScreen(Screen parent, Menu menu) {
		super(menu);
		
		this.parent = parent;
		windowXOffsetPercent = 35;
	}

	protected boolean navigationMovement(int dx, int dy, int status, int time) {
	    if (dx < 0) {
	        synchronized (UiApplication.getEventLock()) {
	            UiApplication.getUiApplication().popScreen(this);
	        }
	    }

	    return super.navigationMovement(dx, dy, status, time);
	}
	
    protected void sublayout(int maxWidth, int maxHeight) {
        if (menu == null) {
            throw new IllegalArgumentException("no menu specified");
        }
        
        layoutDelegate(maxWidth - (BORDER_WIDTH * 2), maxHeight - (BORDER_WIDTH * 2));
        setPositionDelegate(BORDER_WIDTH, BORDER_WIDTH);
        
        menuWidth = getDelegate().getWidth();
        menuHeight = getDelegate().getHeight();
        
        int yOffsetDisplacement = Math.max(menuHeight, parent.getExtent().height);

        int xOffset = Display.getWidth() * windowXOffsetPercent / 100;
        int yOffset = Display.getHeight() - yOffsetDisplacement;
            
        setPosition(xOffset, yOffset);
        setExtent(menuWidth, menuHeight);
    }

}
