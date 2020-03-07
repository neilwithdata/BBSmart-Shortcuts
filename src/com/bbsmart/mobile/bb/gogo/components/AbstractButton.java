package com.bbsmart.mobile.bb.gogo.components;

public abstract class AbstractButton extends ActiveField {

	protected Runnable runnable;
	protected int padding;
	
	public AbstractButton() {
	    this(0);
	}
	
	public AbstractButton(long style) {
	    super(style);
	}

	protected boolean navigationClick(int status, int time) {
		if (runnable != null) {
			runnable.run();
		}

		return true;
	}

	public boolean isFocusable() {
		return true;
	}
	
	public Runnable getRunnable() {
		return runnable;
	}
	
	public void setPadding(int padding) {
		this.padding = padding;
	}
	
	public void setRunnable(Runnable runnable) {
		this.runnable = runnable;
	}

}
