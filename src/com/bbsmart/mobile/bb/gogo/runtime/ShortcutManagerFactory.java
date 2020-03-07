package com.bbsmart.mobile.bb.gogo.runtime;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.components.IMacroManager;
import com.bbsmart.mobile.bb.gogo.macros.EmailMacroManager;
import com.bbsmart.mobile.bb.gogo.macros.MessagingMacroManager;
import com.bbsmart.mobile.bb.gogo.macros.PhoneMacroManager;
import com.bbsmart.mobile.bb.gogo.macros.WebMacroManager;
import com.bbsmart.mobile.bb.gogo.persistence.IPersistenceProvider;
import com.bbsmart.mobile.bb.gogo.persistence.PersistenceException;
import com.bbsmart.mobile.bb.gogo.persistence.PersistentStorePersistenceProvider;

public class ShortcutManagerFactory {

	public static final ShortcutManagerFactory instance = new ShortcutManagerFactory();

	private ShortcutManagerFactory() {
		
	}

	public IMacroManager getManager(int type) {
		IMacroManager manager = null;
		String persistenceName = null;
		String managerTitle = null;
		String moduleName = null;

		switch(type) {
		case Config.SHORTCUT_MANAGER_TYPE_EMAIL:
			manager = EmailMacroManager.instance;
			managerTitle = Config.SHORTCUT_MANAGER_EMAIL_TITLE;
			persistenceName = Config.PERSISTENCE_NAME_EMAIL;
			moduleName = Config.STANDARD_MODULE_NAME_MESSAGING;
			break;
		case Config.SHORTCUT_MANAGER_TYPE_MESSAGING:
			manager = MessagingMacroManager.instance;
			managerTitle = Config.SHORTCUT_MANAGER_MESSAGING_TITLE;
			persistenceName = Config.PERSISTENCE_NAME_MESSAGING;
			moduleName = Config.STANDARD_MODULE_NAME_MESSAGING;
			break;
		case Config.SHORTCUT_MANAGER_TYPE_PHONE:
			manager = PhoneMacroManager.instance;
			managerTitle = Config.SHORTCUT_MANAGER_PHONE_TITLE;
			persistenceName = Config.PERSISTENCE_NAME_PHONE;
			moduleName = Config.STANDARD_MODULE_NAME_PHONE;
			break;
		case Config.SHORTCUT_MANAGER_TYPE_WEB:
			manager = WebMacroManager.instance;
			managerTitle = Config.SHORTCUT_MANAGER_WEB_TITLE;
			persistenceName = Config.PERSISTENCE_NAME_WEB;
			moduleName = Config.STANDARD_MODULE_NAME_BROWSER;
			break;
		}
		
		if (manager != null) {
			configureManager(manager, managerTitle, persistenceName, moduleName);
		}

		return manager;
	}
	
	private void configureManager(IMacroManager manager, String title, 
			String persistenceName, String moduleName) {
		MacroRegistry registry = new MacroRegistry();
		IPersistenceProvider provider = new PersistentStorePersistenceProvider(
				persistenceName);

		manager.setModuleName(moduleName);
		manager.setRegistry(registry);
		manager.setTitle(title);

		registry.setPersistenceProvider(provider);
		
		try {
            registry.load();
        }
        catch (PersistenceException ignore) {
        }
	}

}
