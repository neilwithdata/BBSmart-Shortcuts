package com.bbsmart.mobile.bb.gogo.screens;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.ShortcutsApp;
import com.bbsmart.mobile.bb.gogo.components.Titlebar;

import net.rim.blackberry.api.browser.Browser;
import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

import com.bbsmart.mobile.bb.gogo.components.ColorLabelField;
import com.bbsmart.mobile.bb.gogo.components.HrefField;

public class TrialEndedScreen extends MainScreen {
	private String buyNowURL = "https://www.mobihand.com/mobilecart/mc1.asp?posid=16&pid=19893&did="
			+ Integer.toHexString(DeviceInfo.getDeviceId()).toUpperCase();

	private ButtonField submitFeedbackBtn;
	private ButtonField registerButton;

	public TrialEndedScreen() {
		setScreenFont();
		initButtons();
		initDisplay();
	}

	private void setScreenFont() {
		setFont(getFont().derive(Font.BOLD, 17, Ui.UNITS_px,
				Font.ANTIALIAS_STANDARD, 0));
	}

	private void initButtons() {
		submitFeedbackBtn = new ButtonField("Submit Feedback",
				ButtonField.NEVER_DIRTY | ButtonField.CONSUME_CLICK
						| ButtonField.FIELD_HCENTER);
		submitFeedbackBtn.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				MessageArguments mArgs = new MessageArguments(
						MessageArguments.ARG_NEW,
						"support@blackberrysmart.com",
						"BBSmart Shortcuts Feedback", "");

				Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, mArgs);
			}
		});

		registerButton = new ButtonField("Register", ButtonField.CONSUME_CLICK
				| ButtonField.NEVER_DIRTY | ButtonField.FIELD_HCENTER);
		registerButton.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				UiApplication.getUiApplication().popScreen(
						UiApplication.getUiApplication().getActiveScreen());
				UiApplication.getUiApplication().pushScreen(
						new RegisterScreen(true));
			}
		});
	}

	private void initDisplay() {
		addTitlebar();
		displayText();
	}

	private void addTitlebar() {
		Titlebar titlebar = new Titlebar();
		titlebar.setPadding(2);
		titlebar.setTitle("Trial Ended!");
		add(titlebar);
	}

	private void displayText() {
		heading("> THANKS FOR STOPPING BY", true);
		text("Your free trial of BBSmart Shortcuts has now ended!\n");

		heading("> GET THE FULL VERSION", true);
		text("If you enjoyed using this application and wish to continue using it, please follow the purchasing instructions below.\n");

		heading(">> From your PC", false);
		text("From your PC, head on over to the Handango website and pick up a copy of BBSmart Shortcuts.");
		link("www.handango.com", "http://www.handango.com");

		spacer();

		heading(">> From your BlackBerry", false);
		text("Alternatively you can buy online right now from our secure mobile-friendly store by clicking the \"Buy Now\" link below.");
		link("Buy Now", buyNowURL);

		spacer();

		heading("> WHAT DID YOU THINK?", true);
		text("Got some feedback you would like to give? Loved it? Hated it? Got a cool idea to make it better? We'd love to hear from you!");
		add(submitFeedbackBtn);

		spacer();

		heading("> ALREADY BOUGHT IT?", true);
		text("If you have already purchased BBSmart Shortcuts, click the \"Register\" button below and on the following screen enter in your purchase registration key.");
		add(registerButton);
	}

	private void heading(String headingText, boolean major) {
		add(new ColorLabelField(headingText,
				major ? Config.COLOR_TEXT_ACTIVE_BACKGROUND : Color.GREEN));
	}

	private void text(String text) {
		add(new BasicEditField("", text.toString(),
				BasicEditField.DEFAULT_MAXCHARS, BasicEditField.READONLY));
	}

	private void link(String show, final String url) {
		HrefField siteLink = new HrefField(show, Field.FIELD_LEFT);
		siteLink.setTextColour(Color.BLUE);
		siteLink.setPadding(0, 0, 0, (Graphics.getScreenWidth() - siteLink
				.getPreferredWidth()) / 2);
		siteLink.setFont(getFont());

		siteLink.setChangeListener(new FieldChangeListener() {
			public void fieldChanged(Field field, int context) {
				Browser.getDefaultSession().displayPage(url);
			}
		});

		add(siteLink);
	}

	private void spacer() {
		add(new LabelField("", LabelField.READONLY));
	}

	public boolean onClose() {
		close();
		ShortcutsApp.getInstance().requestBackground();
		return true;
	}
}
