package com.bbsmart.mobile.bb.gogo.components;

import java.util.Vector;

import com.bbsmart.mobile.bb.gogo.model.CodeModule;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

public class SimpleListFieldCallback implements ListFieldCallback {

	private Vector registry = new Vector();

	public void drawListRow(ListField listField, Graphics graphics, int index, int y, int width) {
	}

	public Object get(ListField listField, int index) {
		return registry.elementAt(index);
	}
	
	public int getPreferredWidth(ListField listField) {
		return Display.getWidth();
	}

	public int indexOfList(ListField listField, String prefix, int start) {
		return listField.getSelectedIndex();
	}
	
	public void add(CodeModule metadata) {
		registry.addElement(metadata);
	}
	
}
