package com.bbsmart.mobile.bb.gogo.util;

import com.bbsmart.mobile.bb.gogo.Config;

import net.rim.device.api.system.EventLogger;

public final class BBLogger {
	public static void initialize() {
		EventLogger.register(Config.APP_PRIVATE_KEY, "BBSmart Shortcuts",
				EventLogger.VIEWER_STRING);
	}

	public static void logEvent(String msg) {
		EventLogger.logEvent(Config.APP_PRIVATE_KEY, msg.getBytes());
	}
}
