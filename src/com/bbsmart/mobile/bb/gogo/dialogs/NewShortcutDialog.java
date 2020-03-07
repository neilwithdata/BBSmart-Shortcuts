package com.bbsmart.mobile.bb.gogo.dialogs;

import java.util.Enumeration;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.components.ColoredListField;
import com.bbsmart.mobile.bb.gogo.components.HrefField;
import com.bbsmart.mobile.bb.gogo.model.CodeModule;
import com.bbsmart.mobile.bb.gogo.runtime.CodeModuleRegistry;
import com.bbsmart.mobile.bb.gogo.runtime.CodeModuleRegistryListener;
import com.bbsmart.mobile.bb.gogo.runtime.ToolbarListFieldCallback;
import com.bbsmart.mobile.bb.gogo.runtime.ToolbarRegistry;

import net.rim.device.api.system.Application;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class NewShortcutDialog extends Screen implements CodeModuleRegistryListener {

	private static final int DEFAULT_MARGIN				= 10;
	private static final int DEFAULT_PADDING			= 2;
	
	private static final int SCREEN_HEIGHT_PERCENTAGE	= 80;
	private static final int SCREEN_WIDTH_PERCENTAGE	= 80;
	
	private ColoredListField listField = new ColoredListField();
	private CodeModuleRegistry registry = CodeModuleRegistry.instance;
	private ToolbarListFieldCallback callback = new ToolbarListFieldCallback();
	
	protected HrefField okButton = new HrefField("Ok");
	
	private CodeModule metadata;

	public NewShortcutDialog() {
		super(new VerticalFieldManager(
				Manager.NO_HORIZONTAL_SCROLL | Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
			protected void sublayout(int width, int height) {
				int extentWidth = width - (DEFAULT_MARGIN * 2);
				int extentHeight = height - (DEFAULT_MARGIN * 2);
				
				setExtent(extentWidth, extentHeight);
				setPosition(DEFAULT_MARGIN, DEFAULT_MARGIN);
				
				Field field = getField(0);
				layoutChild(field, extentWidth, 100);
				setPositionChild(field, 0, 0);
			}
		});
		
		listField.setCallback(callback);
		listField.setRowHeight(28);
		listField.setActiveBackgroundColor(0x006B86B5);
		listField.setActiveForegroundColor(Config.COLOR_TEXT_ACTIVE_FOREGROUND);
		listField.setInactiveBackgroundColor(Config.COLOR_TEXT_INACTIVE_BACKGROUND);
		listField.setInactiveForegroundColor(0x00505050);
		
		Enumeration en = registry.elements();
		
		int i = 0;
		while (en.hasMoreElements()) {
		    CodeModule codeModule = (CodeModule) en.nextElement();
		    if (!ToolbarRegistry.instance.exists(codeModule)) {
		        callback.insert(i, codeModule);
		        listField.insert(i);
		        i++;
		    }
		}
		callback.sort();
		
		add(listField);
	}
	
	protected void dialogAccepted() {
		metadata = (CodeModule) callback.get(listField, listField.getSelectedIndex());
		UiApplication.getUiApplication().popScreen(this);
	}

	protected boolean keyChar(char c, int status, int time) {
		if (c == Characters.ESCAPE) {
			UiApplication.getUiApplication().popScreen(this);
			return true;
		}
		
		if (c == Characters.ENTER) {
		    navigationClick(status, time);
		    return true;
		}
		
		return super.keyChar(c, status, time);
	}

	protected boolean navigationClick(int status, int time) {
		dialogAccepted();
		return true;
	}

	public void onCodeModuleRegistryAdd(CodeModule metadata) {
		System.out.println("onCodeModuleRegistryAdd");
		listField.insert(listField.getSize());
		if (isVisible()) {
			invalidate();
		}
	}

	public void onCodeModuleRegistryRemove(CodeModule metadata) {
		System.out.println("onCodeModuleRegistryRemove");
		listField.delete(listField.getSize() - 1);
		if (isVisible()) {
			invalidate();
		}
	}

	public CodeModule open() {
		synchronized(Application.getEventLock()) {
			UiApplication.getUiApplication().pushModalScreen(this);
		}

		return metadata;
	}

	protected void onDisplay() {
		registry.addListener(this);
		super.onDisplay();
	}

	protected void paintBackground(Graphics graphics) {
		XYRect clip = graphics.getClippingRect();
		XYRect absClip = new XYRect();
		graphics.getAbsoluteClippingRect(absClip);
		
		int color = graphics.getColor();
		
		graphics.setColor(0x00EFEFEF);
		graphics.fillRect(clip.x, clip.y, clip.width, clip.height);
		
		graphics.setColor(0x006B86B5);
		graphics.drawRect(clip.x, clip.y, clip.width, clip.height);
		
		graphics.setColor(Color.WHITE);
		graphics.fillRect(clip.x + 9, clip.y + 9, clip.width - 18, clip.height - 18);

		graphics.setColor(Color.DARKGRAY);
		graphics.drawRect(clip.x + 9, clip.y + 9, clip.width - 18, clip.height - 18);
		
		graphics.setColor(color);
	}

	protected void sublayout(int width, int height) {
		int screenWidth = Display.getWidth();
		int screenHeight = Display.getHeight();
		
		int extentWidth = screenWidth * SCREEN_WIDTH_PERCENTAGE / 100;
		int extentHeight = screenHeight * SCREEN_HEIGHT_PERCENTAGE / 100;
		
		//
		// This is to dynamically size the window so that the delegate manager can
		// fit an even number of items.
		//
		int delegateRowHeight = Config.APPLICATION_ICON_HEIGHT + (DEFAULT_PADDING * 2);
		int delegateHeight = extentHeight - (DEFAULT_MARGIN * 2);
		int multiple = (delegateHeight / delegateRowHeight);
		
		delegateHeight = multiple * delegateRowHeight;
		extentHeight = delegateHeight + (DEFAULT_MARGIN * 2);
		
		int xOffset = (screenWidth - extentWidth) / 2;
		int yOffset = (screenHeight - extentHeight) / 2;
		
		setExtent(extentWidth, extentHeight);
		setPosition(xOffset, yOffset);
		
		layoutDelegate(extentWidth, extentHeight);
	}
	
}
