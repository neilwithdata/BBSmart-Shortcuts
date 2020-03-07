package com.bbsmart.mobile.bb.gogo.dialogs;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.XYRect;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.components.Titlebar;
import com.bbsmart.mobile.bb.gogo.components.SpacerField;
import com.bbsmart.mobile.bb.gogo.components.HrefField;
import com.bbsmart.mobile.bb.gogo.persistence.TrialManager;
import com.bbsmart.mobile.bb.gogo.screens.RegisterScreen;

public class AppInfoDialog extends Screen {

	private static final int DEFAULT_MARGIN = 10;
	private static final int SCREEN_WIDTH_PERCENTAGE = 80;

	private TrialManager tMan;

	private Bitmap background;
	private Titlebar titlebar;

	public AppInfoDialog() {
		super(new VerticalFieldManager(Manager.NO_HORIZONTAL_SCROLL
				| Manager.NO_VERTICAL_SCROLL) {
			protected void sublayout(int width, int height) {
				int extentWidth = width - (DEFAULT_MARGIN * 2);
				int extentHeight = height - (DEFAULT_MARGIN * 2);

				setExtent(extentWidth, extentHeight);
				setPosition(DEFAULT_MARGIN, DEFAULT_MARGIN);

				super.sublayout(extentWidth, extentHeight);
			}
		});

		setScreenFont();

		tMan = TrialManager.getInstance();

		titlebar = new Titlebar();
		titlebar.setTitle("About");
		add(titlebar);

		add(new LabelField("Version: " + Config.APP_VER));
		add(new LabelField("Status: " + tMan.getStateString()));

		Font linkFont = getFont().derive(Font.BOLD, 16, Ui.UNITS_px);

		if (tMan.state != TrialManager.STATE_REG) {
			HrefField register = new HrefField("Buy/Register", Field.FIELD_LEFT);

			int registerLftMrgn = (int) ((Graphics.getScreenWidth() * 0.8
					- (DEFAULT_MARGIN * 2) - register.getPreferredWidth()) / 2);
			register.setBackgroundColour(Config.COLOR_WINDOW_BACKGROUND);
			register.setTextColour(Config.COLOR_TEXT_ACTIVE_BACKGROUND);
			register.setFont(linkFont);
			register.setPadding(0, 0, 0, registerLftMrgn);

			register.setChangeListener(new FieldChangeListener() {
				public void fieldChanged(Field field, int context) {
					close();
					UiApplication.getUiApplication().pushScreen(
							new RegisterScreen(false));
				}
			});

			add(register);
		}

		add(new SpacerField(SpacerField.MODE_VERT, 10,
				Config.COLOR_WINDOW_BACKGROUND));

		add(new LabelField("Device Type: " + DeviceInfo.getDeviceName()));
		add(new LabelField("Device PIN: "
				+ Integer.toHexString(DeviceInfo.getDeviceId()).toUpperCase()));

		if (tMan.state == TrialManager.STATE_REG) {
			add(new LabelField("Registration Key: " + tMan.activationKey));
		}

		add(new SpacerField(SpacerField.MODE_VERT, 10,
				Config.COLOR_WINDOW_BACKGROUND));

		HrefField chkUpdates = new HrefField("Check for Updates",
				Field.FIELD_LEFT);

		int chkUpdatesLftMrgn = (int) ((Graphics.getScreenWidth() * 0.8
				- (DEFAULT_MARGIN * 2) - chkUpdates.getPreferredWidth()) / 2);
		chkUpdates.setBackgroundColour(Config.COLOR_WINDOW_BACKGROUND);
		chkUpdates.setTextColour(Config.COLOR_TEXT_ACTIVE_BACKGROUND);
		chkUpdates.setFont(linkFont);
		chkUpdates.setPadding(0, 0, 0, chkUpdatesLftMrgn);

		chkUpdates.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				Browser.getDefaultSession().displayPage(
						"http://www.blackberrysmart.com/updates.php?appName=shortcuts&version="
								+ Config.APP_VER);
			}
		});

		add(chkUpdates);

		HrefField contactUs = new HrefField("Contact Us", Field.FIELD_LEFT);

		int contactUsLftMrgn = (int) ((Graphics.getScreenWidth() * 0.8
				- (DEFAULT_MARGIN * 2) - contactUs.getPreferredWidth()) / 2);
		contactUs.setBackgroundColour(Config.COLOR_WINDOW_BACKGROUND);
		contactUs.setTextColour(Config.COLOR_TEXT_ACTIVE_BACKGROUND);
		contactUs.setFont(linkFont);
		contactUs.setPadding(0, 0, 0, contactUsLftMrgn);

		contactUs.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				MessageArguments mArgs = new MessageArguments(
						MessageArguments.ARG_NEW,
						"support@blackberrysmart.com",
						"BBSmart Shortcuts Support Query", "");

				Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, mArgs);
			}
		});

		add(contactUs);
	}

	private void setScreenFont() {
		Font f;
		try {
			f = FontFamily.forName("BBCasual").getFont(Font.BOLD, 17);
			f = f.derive(f.getStyle(), f.getHeight(), Ui.UNITS_px,
					Font.ANTIALIAS_STANDARD, 0);
		} catch (ClassNotFoundException cnfe) {
			f = getFont().derive(Font.BOLD, 17, Ui.UNITS_px,
					Font.ANTIALIAS_STANDARD, 0);
		}

		setFont(f);
	}

	public void open() {
		synchronized (Application.getEventLock()) {
			UiApplication.getUiApplication().pushModalScreen(this);
		}
	}

	protected void createBackground() {
		background = new Bitmap(getWidth(), getHeight());

		Graphics graphics = new Graphics(background);
		XYRect clip = graphics.getClippingRect();

		int color = graphics.getColor();

		graphics.setColor(0x00EFEFEF);
		graphics.fillRect(clip.x, clip.y, clip.width, clip.height);

		graphics.setColor(0x006B86B5);
		graphics.drawRect(clip.x, clip.y, clip.width, clip.height);

		graphics.setColor(Config.COLOR_WINDOW_BACKGROUND);
		graphics.fillRect(clip.x + 9, clip.y + 9, clip.width - 18,
				clip.height - 18);

		graphics.setColor(Color.DARKGRAY);
		graphics.drawRect(clip.x + 9, clip.y + 9, clip.width - 18,
				clip.height - 18);

		graphics.setColor(color);
	}

	protected void dialogAccepted() {
		UiApplication.getUiApplication().popScreen(this);
	}

	protected boolean keyChar(char c, int status, int time) {
		if (c == Characters.ESCAPE || c == Characters.ENTER) {
			UiApplication.getUiApplication().popScreen(this);
			return true;
		}

		return super.keyChar(c, status, time);
	}

	protected void paintBackground(Graphics graphics) {
		if (background == null) {
			createBackground();
		}

		XYRect extent = getExtent();

		graphics.rop(Graphics.ROP_SRC_COPY, 0, 0, extent.width, extent.height,
				background, 0, 0);
	}

	protected void sublayout(int width, int height) {
		int screenWidth = Display.getWidth();
		int screenHeight = Display.getHeight();

		int extentWidth = screenWidth * SCREEN_WIDTH_PERCENTAGE / 100;

		layoutDelegate(extentWidth, height);

		int extentHeight = getDelegate().getHeight() + (DEFAULT_MARGIN * 2);

		int xOffset = (screenWidth - extentWidth) / 2;
		int yOffset = (screenHeight - extentHeight) / 2;

		setExtent(extentWidth, extentHeight);
		setPosition(xOffset, yOffset);
	}
}