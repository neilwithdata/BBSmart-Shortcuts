package com.bbsmart.mobile.bb.gogo.runtime;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.model.CodeModule;
import com.bbsmart.mobile.bb.gogo.model.Shortcut;
import com.bbsmart.mobile.bb.gogo.persistence.IPersistenceProvider;
import com.bbsmart.mobile.bb.gogo.persistence.PersistenceException;
import com.bbsmart.mobile.bb.gogo.persistence.PersistentStorePersistenceProvider;
import com.bbsmart.mobile.bb.gogo.util.CodeModuleUtils;

public class ToolbarRegistry extends PersistableRegistry {

	public static final ToolbarRegistry instance = new ToolbarRegistry();
	
	private ToolbarRegistry() {
		super();

		IPersistenceProvider provider = new PersistentStorePersistenceProvider(
				Config.TOOLBAR_REGISTRY_PERSISTENT_NAME);
		
		setPersistenceProvider(provider);

		try {
			load();
		}
		catch (PersistenceException e) {
			// Loading *failed* presumably because the store hasn't been created yet.
			// Provide an initial population of system apps.
			populate();
		}
	}
	
	public boolean exists(CodeModule codeModule) {
	    int size = objects.size();
	    
	    for (int i = 0; i < size; i++) {
	        Shortcut shortcut = (Shortcut) objects.elementAt(i);
	        if (shortcut.getCodeModule() == codeModule) {
	            return true;
	        }
	    }
	    
	    return false;
	}
	
	public void moveElement(int oldIndex, int newIndex) {
	    Object obj = objects.elementAt(oldIndex);

	    objects.removeElementAt(oldIndex);
	    
	    if (newIndex < objects.size()) {
	        objects.insertElementAt(obj, newIndex);
	    }
	    else {
	        objects.addElement(obj);
	    }
	}

	private void populate() {
		//
		// TODO: Pull this from the same XML file that CodeModuleRegistry should be using.
		//
		String[] moduleNames = new String[] {
				Config.STANDARD_MODULE_NAME_ADDRESSBOOK,
				Config.STANDARD_MODULE_NAME_BROWSER,
				Config.STANDARD_MODULE_NAME_CALCULATOR,
				Config.STANDARD_MODULE_NAME_CALENDAR,
				Config.STANDARD_MODULE_NAME_MEMO,
				Config.STANDARD_MODULE_NAME_MESSAGING,
				Config.STANDARD_MODULE_NAME_PHONE,
				Config.STANDARD_MODULE_NAME_TASKS,
		};

		int numModuleNames = moduleNames.length;
		
		for (int i = 0; i < numModuleNames; i++) {
			CodeModule codeModule = CodeModuleUtils.getModuleByModuleName(moduleNames[i]);

			// Um, let's not make any (more) assumptions about which apps are installed on
			// the user's device.  If it ain't there, don't try to use it.
			if (codeModule != null) {
			    Shortcut newShortcut = new Shortcut();
			    newShortcut.setCodeModule(codeModule);

			    add(newShortcut);
			}
		}
		
		store();
	}

}
