package com.bbsmart.mobile.bb.gogo.components.menu;

public final class MenuScreen extends AbstractMenuScreen {

	public MenuScreen(Menu menu) {
		super(menu);
	}
	
	public void onExposed() {
	    invalidate();
	}

}
