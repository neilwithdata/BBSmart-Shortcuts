package com.bbsmart.mobile.bb.gogo.components;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

public class ActiveEditField extends HorizontalFieldManager implements Observable {

	private static final int PADDING	= 3;
	
	private Bitmap background;
	private EditField editField;
	private HorizontalFieldManager hfm;
	private XYRect absClippingRect = new XYRect();

	public ActiveEditField(EditField field) {
		super(Manager.NO_HORIZONTAL_SCROLL);

		hfm = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL | Manager.NO_HORIZONTAL_SCROLLBAR) {
		    protected void sublayout(int width, int height) {
		        Field editField = getField(0);
		        layoutChild(editField, 1000, height);
		        setPositionChild(editField, 0, 0);
		        setExtent(width, editField.getHeight());
		    }
		};
		
		editField = field;

		hfm.add(editField);
		
		add(hfm);
	}
	
	public XYRect getAbsoluteClippingRect() {
	    return absClippingRect;
	}

	public EditField getEditField() {
	    return editField;
	}

	public String getText() {
		return editField.getText();
	}

	public void setChangeListener(FieldChangeListener listener) {
	    editField.setChangeListener(listener);
	}

	public void setText(String text) {
		editField.setText(text);
	}

	protected void createBackground() {
		background = new Bitmap(getWidth(), getHeight());

		Graphics graphics = new Graphics(background);
		XYRect clip = graphics.getClippingRect();
		
		int color = graphics.getColor();
		
		graphics.setColor(0x006B86B5);
		graphics.drawRect(clip.x, clip.y, clip.width, clip.height);
		
		graphics.setColor(color);
	}
	
	protected void paint(Graphics graphics) {
	    // Doing this craziness because we have a glitch in the UI where the auto-complete
	    // component is getting different values for the abs clipping region.  Requires
	    // further investigation.
	    XYRect absClip = new XYRect();
	    XYRect extent = getExtent();

	    graphics.getAbsoluteClippingRect(absClip);
	    
	    if (absClip.width == extent.width) {
	        absClippingRect = absClip;
	    }
	    
	    super.paint(graphics);
	}

	protected void sublayout(int width, int height) {
		int xOffset = PADDING;
		int yOffset = PADDING;
		int childWidth = width - (PADDING * 2);
		int childHeight = height - (PADDING * 2);
		
		layoutChild(hfm, childWidth, childHeight);
		setPositionChild(hfm, xOffset, yOffset);
		
		int extentWidth = hfm.getWidth() + (PADDING * 2);
		int extentHeight = hfm.getHeight() + (PADDING * 2);
		
		setExtent(extentWidth, extentHeight);
	}
	
	protected void subpaint(Graphics graphics) {
		if (background == null) {
			createBackground();
		}

		graphics.rop(Graphics.ROP_SRC_COPY, 0, 0, getWidth(), getHeight(), background, 0, 0);

		paintChild(graphics, hfm);
	}

}
