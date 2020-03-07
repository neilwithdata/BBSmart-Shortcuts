package com.bbsmart.mobile.bb.gogo.components;

import com.bbsmart.mobile.bb.gogo.screens.Shortcuts;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.XYEdges;
import net.rim.device.api.ui.XYRect;

public abstract class ActiveField extends Field implements Observable {
    
    public static final int STATE_FOCUSED    = 0;
    public static final int STATE_UNFOCUSED  = 1;

    public static final int ALIGN_LEFT      = 0x00000001;
    public static final int ALIGN_CENTER    = 0x00000010;
    public static final int ALIGN_RIGHT     = 0x00000100;

    public static final int ALIGN_TOP       = 0x00001000;
    public static final int ALIGN_MIDDLE    = 0x00010000;
    public static final int ALIGN_BOTTOM    = 0x00100000;
    
    public static final int USE_ALL_WIDTH   = 0x00000001;
    public static final int USE_ALL_HEIGHT  = 0x00000010;

	protected String toolTip;
	protected XYRect absClippingRect = new XYRect();
	protected XYEdges padding = new XYEdges();
	protected boolean shouldDisplayTooltip;
	protected int alignment;
	protected int style;
	
    protected Font[] font;
    protected String text;
    protected int state;
    protected int[] backgroundColor;
    protected int[] foregroundColor;

	public ActiveField() {
	    this(0);
	}
	
	public ActiveField(long style) {
	    super(style);
	    
	    font = new Font[2];
	    backgroundColor = new int[2];
	    foregroundColor = new int[2];
	}
	
	public XYRect getAbsoluteClippingRect() {
	    return absClippingRect;
	}

	public String getToolTip() {
		return toolTip;
	}
	
    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }
    
    public void setBackgroundColor(int state, int color) {
        this.backgroundColor[state] = color;
    }
    
    public void setFont(int state, Font font) {
        this.font[state] = font;
    }
    
    public void setForegroundColor(int state, int color) {
        this.foregroundColor[state] = color;
    }
    
    public void setPadding(int each) {
        this.setPadding(each, each, each, each);
    }
    
    public void setPadding(int top, int right, int bottom, int left) {
        padding.top = top;
        padding.right = right;
        padding.bottom = bottom;
        padding.left = left;
    }

    public void setStyle(int style) {
        this.style = style;
    }
	
	public void setToolTip(String toolTip) {
		this.toolTip = toolTip;
		shouldDisplayTooltip = true;
	}

	protected void onFocus(int direction) {
	    state = STATE_FOCUSED;

	    if (shouldDisplayTooltip) {
	        if (toolTip != null) {
	            Shortcuts screen = (Shortcuts) getScreen();
	            screen.onTooltipDisplayRequest(this);
	        }
	    }
		
		super.onFocus(direction);
	}
	
	protected void onUnfocus() {
	    state = STATE_UNFOCUSED;

	    if (toolTip != null) {
	        Shortcuts screen = (Shortcuts) getScreen();
	        screen.onTooltipDismissRequest();
	    }
	    super.onUnfocus();
	}
	

}
