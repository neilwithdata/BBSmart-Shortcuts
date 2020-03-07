package com.bbsmart.mobile.bb.gogo;

import net.rim.device.api.ui.UiApplication;

import com.bbsmart.mobile.bb.gogo.preferences.IPreferenceStore;
import com.bbsmart.mobile.bb.gogo.preferences.PreferenceStore;
import com.bbsmart.mobile.bb.gogo.screens.Shortcuts;

import com.bbsmart.mobile.bb.gogo.persistence.TrialManager;
import com.bbsmart.mobile.bb.gogo.screens.TrialEndedScreen;
import com.bbsmart.mobile.bb.gogo.screens.RegisterScreen;
import com.bbsmart.mobile.bb.gogo.util.BBLogger;

public class ShortcutsApp extends UiApplication {

	private static ShortcutsApp app;
	private TrialManager tMan;

	private IPreferenceStore preferenceStore = PreferenceStore
			.openPreferenceStore("shortcutsPrefs");

	public static void main(String[] args) {
		ShortcutsApp app = new ShortcutsApp();
		app.enterEventDispatcher();
	}

	private ShortcutsApp() {
		tMan = TrialManager.getInstance();
		app = this;

		BBLogger.initialize();

		pushScreen(new Shortcuts());
	}

	public void activate() {
		switch (tMan.state) {
		case TrialManager.STATE_TRIAL:
			// Remember the first time the application is run
			if (tMan.isFirstRun()) {
				tMan.setFirstTimeRun();
				tMan.save();
			}

			if (tMan.isTrialExpired()) {
				tMan.state = TrialManager.STATE_TRIAL_EX;
				tMan.save();

				pushScreen(new TrialEndedScreen());
				break;
			}

			// intentional fall-through, trial has not expired
		case TrialManager.STATE_REG:
			// Do nothing, screen will automatically be foregrounded
			break;
		case TrialManager.STATE_TRIAL_EX:
			pushScreen(new TrialEndedScreen());
			break;
		case TrialManager.STATE_FULL:
			pushScreen(new RegisterScreen(true));
			break;
		}
	}

	public static ShortcutsApp getInstance() {
		return app;
	}

	public IPreferenceStore getPreferenceStore() {
		return preferenceStore;
	}
}