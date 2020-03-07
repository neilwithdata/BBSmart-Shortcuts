package com.bbsmart.mobile.bb.gogo.util;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.XYRect;

public class Utils {

	public static Field getField(Object obj) {
		Field field = null;
		
		if (obj instanceof Field) {
			field = (Field) obj;
		}
		
		return field;
	}
	
	public static String printRect(XYRect rect) {
		return "(" + rect.x + " " + rect.y + " " + rect.width + " " + rect.height + ")";
	}

}
