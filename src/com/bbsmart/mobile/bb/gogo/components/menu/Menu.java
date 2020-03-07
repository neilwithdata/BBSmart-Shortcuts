package com.bbsmart.mobile.bb.gogo.components.menu;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class Menu extends VerticalFieldManager {
	
	private int selectedIndex = -1;

	public MenuItem add(String title) {
		return add(title, (char)0);
	}
	
	public MenuItem add(String title, char shortcut) {
		MenuItem item = new MenuItem(title, shortcut);

		super.add(item);
		
		return item;
	}
	
	public void addSeparator() {
		super.add(new Separator());
	}

	public SubMenu addSubMenu(String title) {
		return addSubMenu(title, (char)0);
	}
	
	public SubMenu addSubMenu(String title, char shortcut) {
		SubMenu subMenu = new SubMenu();

		MenuItem item = new MenuItem(title, shortcut);
		item.setSubMenu(subMenu);
		
		super.add(item);
		
		return subMenu;
	}
	
	public Object get(int index) {
		return super.getField(index);
	}
	
	public Object getFirst() {
	    return get(0);
	}
	
	public Object getLast() {
	    return get(getFieldCount() - 1);
	}
	
	// We want the main menu to be at least half the width of the screen.
	// No puny menus.
	public int getPreferredWidth() {
	    return new Double(Display.getWidth() * 0.50).intValue();
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(int index) {
		selectedIndex = index;
	}
	
	public boolean shouldOpen() {
		return selectedIndex != -1;
	}

	public int size() {
		return super.getFieldCount();
	}
	
	protected void sublayout(int width, int height) {
	    int extentWidth = 0;
	    int maxChildWidth = getMaxChildWidth();
	    int preferredWidth = getPreferredWidth();
	    
	    if (maxChildWidth < preferredWidth) {
	        extentWidth = preferredWidth;
	    }
	    else {
	        extentWidth = maxChildWidth;
	    }

	    layoutChildren(extentWidth, height);
	}
	
	private int getMaxChildWidth() {
	    int maxWidth = 0;
	    int fieldCount = getFieldCount();

	    for (int i = 0; i < fieldCount; i++) {
	        Field field = getField(i);
	        
	        if (field instanceof MenuItem) {
	            maxWidth = Math.max(maxWidth, field.getPreferredWidth());
	        }
	    }
	    
	    return maxWidth;
	}
	
	private void layoutChildren(int width, int height) {
	    int yOffset = 0;
	    int fieldCount = getFieldCount();

	    for (int i = 0; i < fieldCount; i++) {
	        Field field = getField(i);

	        layoutChild(field, width, height);
	        setPositionChild(field, 0, yOffset);

	        yOffset += field.getHeight();
	    }
	    
	    setExtent(width, yOffset);
	}

}
