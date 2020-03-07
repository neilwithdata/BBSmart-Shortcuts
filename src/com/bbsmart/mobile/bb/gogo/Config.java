package com.bbsmart.mobile.bb.gogo;

import com.bbsmart.mobile.bb.gogo.persistence.TrialManager;

public class Config {

    public static final String APP_TITLE							= "BBSmart Shortcuts";
    public static final String APP_VER								= "1.5";
    public static final int TRIAL_DURATION_DAYS						= 10;
    public static final int APP_START_STATE							= TrialManager.STATE_FULL;
    
	public static final long APP_PRIVATE_KEY						= 0xf0e57012f7ab48e7L;
	
	public static final int TOOLTIP_DISPLAY_DELAY                   = 1500; // ms.

    public static final int APPLICATION_ICON_HEIGHT					= 24;
    public static final int APPLICATION_ICON_WIDTH					= 24;

    public static final int BUTTON_OPACITY_WHILE_MOVING             = 0x60;

    public static final int COLOR_WINDOW_BACKGROUND					= 0x00E7EBF7;

    public static final int COLOR_TEXT_ACTIVE_BACKGROUND			= 0x00395994;
    public static final int COLOR_TEXT_ACTIVE_FOREGROUND			= 0x00FFFFFF;
    public static final int COLOR_TEXT_INACTIVE_BACKGROUND			= 0x00FFFFFF;
    public static final int COLOR_TEXT_INACTIVE_FOREGROUND			= 0x00000000;

    public static final String PREF_ON_FIRST_LAUNCH                 = "prefFirstLaunch"; 

    // "com.bbsmart.shortcuts.bb.runtime.uid"
    public static final long RUNTIME_STORE_UID_ID                   = 0xefb73e6cf9d36554L;

    public static final int SHORTCUT_MANAGER_TYPE_EMAIL				= 1;
    public static final int SHORTCUT_MANAGER_TYPE_MESSAGING			= 2;
    public static final int SHORTCUT_MANAGER_TYPE_PHONE				= 3;
    public static final int SHORTCUT_MANAGER_TYPE_WEB				= 4;

    public static final String SHORTCUT_MANAGER_EMAIL_TITLE			= "Email";
    public static final String SHORTCUT_MANAGER_MESSAGING_TITLE		= "Messaging";
    public static final String SHORTCUT_MANAGER_PHONE_TITLE			= "Phone";
    public static final String SHORTCUT_MANAGER_WEB_TITLE			= "Web";

    public static final String PERSISTENCE_NAME_EMAIL				= "com.bbsmart.mobile.bb.persistence.email";
    public static final String PERSISTENCE_NAME_MESSAGING			= "com.bbsmart.mobile.bb.persistence.messaging";
    public static final String PERSISTENCE_NAME_PHONE				= "com.bbsmart.mobile.bb.persistence.phone";
    public static final String PERSISTENCE_NAME_WEB					= "com.bbsmart.mobile.bb.persistence.web";
    public static final String TOOLBAR_REGISTRY_PERSISTENT_NAME 	= "com.bbsmart.mobile.bb.gogo.toolbar";

    // RIM Invoke API on 4.2.0+
    public static final String STANDARD_MODULE_NAME_ADDRESSBOOK		= "net_rim_bb_addressbook_app";
    public static final String STANDARD_MODULE_NAME_CALENDAR		= "net_rim_bb_calendar_app";
    public static final String STANDARD_MODULE_NAME_CAMERA          = "net_rim_bb_camera";
    public static final String STANDARD_MODULE_NAME_MAPS            = "net_rim_bb_lbs";
    public static final String STANDARD_MODULE_NAME_MEMO			= "net_rim_bb_memo_app";
    public static final String STANDARD_MODULE_NAME_MESSAGING		= "net_rim_bb_messaging_app";
    public static final String STANDARD_MODULE_NAME_PHONE			= "net_rim_bb_phone_app";
    public static final String STANDARD_MODULE_NAME_TASKS			= "net_rim_bb_task_app";
    
    // RIM Invoke API on 4.2.0+ "New" action invokers
    public static final String STANDARD_MODULE_NAME_NEW_MEMO        = "net_rim_bb_memo_app";
    
    // ThirdParty Invokers
    public static final String STANDARD_MODULE_NAME_ALARM           = "net_rim_bb_alarm_app";
    public static final String STANDARD_MODULE_NAME_BRICKBREAKER    = "net_rim_device_apps_games_brickbreaker";
    public static final String STANDARD_MODULE_NAME_CALCULATOR      = "net_rim_bb_standardcalculator_app";
    public static final String STANDARD_MODULE_NAME_HELP            = "net_rim_bb_help";
    // User can press END key if they want to do this...
    // public static final String STANDARD_MODULE_NAME_RIBBON          = "net_rim_bb_ribbon_app";
    public static final String STANDARD_MODULE_NAME_MANAGE_CONNECTIONS = "net_rim_bb_manage_connections";
    public static final String STANDARD_MODULE_NAME_MEDIA           = "net_rim_bb_file_explorer";
    // Who's going to want this one?
    // public static final String STANDARD_MODULE_NAME_MEMORY_CLEANER  = "net_rim_bb_mc_app";
    public static final String STANDARD_MODULE_NAME_OPTIONS         = "net_rim_bb_options_app";
    public static final String STANDARD_MODULE_NAME_PASSWORD_KEEPER = "net_rim_bb_passwordkeeper";
    public static final String STANDARD_MODULE_NAME_PIN_MESSENGER   = "net_rim_bb_qm_peer";
    public static final String STANDARD_MODULE_NAME_PROFILES        = "net_rim_bb_profiles_app";
    public static final String STANDARD_MODULE_NAME_SEARCH          = "net_rim_bb_globalsearch_app";
    public static final String STANDARD_MODULE_NAME_SETUP_WIZARD    = "net_rim_bb_setupwizard_app";
    public static final String STANDARD_MODULE_NAME_VIDEO_RECORDER  = "net_rim_bb_videorecorder";
    public static final String STANDARD_MODULE_NAME_VOICE_DIALING   = "net_rim_vad";
    public static final String STANDARD_MODULE_NAME_VOICE_NOTES     = "net_rim_bb_voicenotesrecorder";
    
    // Special cases
    public static final String STANDARD_MODULE_NAME_BROWSER         = "net_rim_bb_browser_daemon";

    // Static strings.  Should be part of a resource bundle ideally
    public static final String RSC_STRING_ADDRESSBOOK_LOADING       = "Loading contacts...";
    public static final String RSC_STRING_CONTACT_SEPARATOR         = ":  ";
    
    public static char deleteItemAccelerator            = 0;
    public static char editItemAccelerator              = 0;
    public static char moveItemAccelerator              = 0;
    public static char toolbarShortcutAccelerator       = 0;
    public static char emailShortcutAccelerator         = 0;
    public static char messagingShortcutAccelerator     = 0;
    public static char phoneShortcutAccelerator         = 0;
    public static char webShortcutAccelerator           = 0;
    public static char closeAppAccelerator              = 0;
    public static char appInfoAccelerator               = 0;
    public static char buyAppAccelerator                = 0;

}
