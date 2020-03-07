package com.bbsmart.mobile.bb.gogo.components;

import com.bbsmart.mobile.bb.gogo.components.menu.SubMenu;

public interface IMenuItem {

	boolean isActive();
	SubMenu getSubMenu();
	boolean hasSubMenu();
	void setActive(boolean isActive);
	void setSubMenu(SubMenu subMenu);

}
