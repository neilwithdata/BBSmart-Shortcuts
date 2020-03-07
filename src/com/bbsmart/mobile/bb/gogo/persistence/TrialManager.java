package com.bbsmart.mobile.bb.gogo.persistence;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

import com.bbsmart.mobile.bb.gogo.Config;

public class TrialManager {
	private final long MILLIS_IN_DAY = 1000 * 60 * 60 * 24;

	public static final int STATE_TRIAL = 0;
	public static final int STATE_TRIAL_EX = 1;
	public static final int STATE_FULL = 2;
	public static final int STATE_REG = 3;

	public String storeVersion;
	public int state;
	public long firstTimeRun;
	public String activationKey;

	private static PersistentObject privateStore; 

	// Singleton Accessor
	private static TrialManager instance;

	public static TrialManager getInstance() {
		if (instance == null) {
			privateStore = PersistentStore.getPersistentObject(Config.APP_PRIVATE_KEY);
			instance = new TrialManager();
		}
		return instance;
	}

	public void setFirstTimeRun() {
		firstTimeRun = System.currentTimeMillis();
	}

	public boolean isFirstRun() {
		return (firstTimeRun == 0L);
	}

	public boolean isTrialExpired() {
		long currentTime = System.currentTimeMillis();

		if (currentTime < firstTimeRun) { // Catch out funny business...
			return true;
		}

		if (currentTime - firstTimeRun >= MILLIS_IN_DAY
				* Config.TRIAL_DURATION_DAYS) {
			// Trial period has expired
			return true;
		}
		return false;
	}

	public String getTrialTimeRemaining() {
		long timeRemInMS = firstTimeRun
				+ (Config.TRIAL_DURATION_DAYS * MILLIS_IN_DAY)
				- System.currentTimeMillis();

		int daysRem = (int) (timeRemInMS / MILLIS_IN_DAY) + 1;

		if (timeRemInMS <= 0) {
			return "Expired!";
		} else {
			return daysRem + " days left";
		}
	}

	public String getStateString() {
		switch (state) {
		case STATE_FULL:
			return "Not Registered";
		case STATE_REG:
			return "Registered";
		case STATE_TRIAL:
			return "Trial (" + getTrialTimeRemaining() + ")";
		case STATE_TRIAL_EX:
			return "Trial (Expired!)";
		default:
			return "";
		}
	}

	private TrialManager() {
		Object persistentObj = privateStore.getContents();
		if (persistentObj == null) {
			// Currently not in the data store so create with defaults and save
			init();
			save();
		} else {
			persistentObj = upgradeStore(persistentObj);
			restorePersistentObj(persistentObj);
		}
	}

	// Makes sure that if a user had at a previous time installed an earlier
	// version of BBSmart Shortcuts and their trial expired, they can install
	// the new version and have their trial period reset
	private Object upgradeStore(Object persistentObj) {
		Object[] objArray = (Object[]) persistentObj;

		if (objArray[0] == null
				|| !((String) objArray[0]).equals(Config.APP_VER)) {
			// We do need to perform an upgrade of the data store

			int oldState = ((Integer) objArray[1]).intValue();
			if (oldState == TrialManager.STATE_TRIAL
					|| oldState == TrialManager.STATE_TRIAL_EX) {
				// If it was previously a trial version or the trial had
				// expired, we want to reset the trial back to beginning so the
				// user can try out the new version
				init();
			} else {
				// User has a registered or full version - upgrade to include
				// store version and restore existing data
				storeVersion = Config.APP_VER;
				state = ((Integer) objArray[1]).intValue();
				firstTimeRun = ((Long) objArray[2]).longValue();
				activationKey = (String) objArray[3];
			}

			save();
			return getPersistentObj();
		} else {
			// No upgrade needed
			return persistentObj;
		}
	}

	public void init() {
		storeVersion = Config.APP_VER;
		state = Config.APP_START_STATE;
		firstTimeRun = 0L;
		activationKey = null;
	}

	public void save() {
		synchronized (privateStore) {
			privateStore.setContents(getPersistentObj());
			privateStore.commit();
		}
	}

	public Object getPersistentObj() {
		Object[] persistentObj = new Object[4];

		persistentObj[0] = storeVersion;
		persistentObj[1] = new Integer(state);
		persistentObj[2] = new Long(firstTimeRun);
		persistentObj[3] = activationKey;

		return persistentObj;
	}

	public void restorePersistentObj(Object persistentObj) {
		Object[] objArray = (Object[]) persistentObj;

		storeVersion = (String) objArray[0];
		state = ((Integer) objArray[1]).intValue();
		firstTimeRun = ((Long) objArray[2]).longValue();
		activationKey = (String) objArray[3];
	}
}