package com.bbsmart.mobile.bb.gogo.components.menu;

import net.rim.device.api.ui.Field;

import com.bbsmart.mobile.bb.gogo.components.IMenuItem;

public abstract class AbstractMenuItem extends Field implements IMenuItem {

	protected static final int HORZ_PADDING		= 4;
	protected static final int VERT_PADDING		= 2;

	protected SubMenu subMenu;
	protected boolean isActive;
	protected int priority;

	public AbstractMenuItem() {
		this(Field.NON_FOCUSABLE);
	}

	public AbstractMenuItem(long style) {
		super(style);
	}

	public SubMenu getSubMenu() {
		return subMenu;
	}

	public boolean hasSubMenu() {
		return subMenu != null;
	}

	public boolean isActive() {
		return isActive;
	}
	
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public void setSubMenu(SubMenu subMenu) {
		this.subMenu = subMenu;
	}

}
