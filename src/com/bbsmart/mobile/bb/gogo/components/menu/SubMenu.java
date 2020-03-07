package com.bbsmart.mobile.bb.gogo.components.menu;

import net.rim.device.api.system.Display;

public class SubMenu extends Menu {

	public SubMenu() {
	}

	public SubMenu addSubMenu(String title) {
		throw new IllegalArgumentException("Submenus are not allowed to nest");
	}

	// Submenus can be smaller, but not comically small.
	public int getPreferredWidth() {
	    return new Double(Display.getWidth() * 0.25).intValue();
	}

}
