package com.bbsmart.mobile.bb.gogo.model;

import java.util.Enumeration;
import java.util.Hashtable;

import com.bbsmart.mobile.bb.gogo.persistence.IPersistable;
import com.bbsmart.mobile.bb.gogo.persistence.IPersistenceProvider;
import com.bbsmart.mobile.bb.gogo.persistence.PersistenceException;
import com.bbsmart.mobile.bb.gogo.util.CodeModuleUtils;

public class Shortcut implements IPersistable, Runnable {

	private static final String PERSISTENT_CODE_MODULE_ID_NAME		= "id";
	private static final String PERSISTENT_PARAM_SIZE_NAME			= "size";
	private static final String PERSISTENT_PARAM_KEY_NAME			= "key";
	private static final String PERSISTENT_PARAM_VALUE_NAME			= "value";
	private static final String PERSISTENT_TOOLTIP_NAME				= "tooltip";

	private CodeModule codeModule;
	private Hashtable params = new Hashtable();
	private String tooltip;
	
	public Shortcut() {
	}

	public CodeModule getCodeModule() {
		return codeModule;
	}
	
	public String getParam(String key) {
	    return (String) params.get(key);
	}

	public Hashtable getParams() {
		return params;
	}

	public String getTooltip() {
        return tooltip;
    }

    public void loadUsing(IPersistenceProvider provider)
			throws PersistenceException {
    	// load up the code module
    	try {
			// The data store is pre Shortcuts 1.5
			int moduleHandle = provider.loadInt("codeModuleHandle");
			codeModule = CodeModuleUtils.getModuleByHandle(moduleHandle);
		} catch (PersistenceException pe) {
			// The data store is not pre-Shortcuts 1.5
			long codeModuleID = provider
					.loadLong(PERSISTENT_CODE_MODULE_ID_NAME);
			codeModule = CodeModuleUtils.getModuleByID(codeModuleID);
		}
    	
    	if (codeModule == null) {
		    throw new PersistenceException("code module handle not found");
		}
    	
    	// load up the parameters
    	try {
			int size = provider.loadInt(PERSISTENT_PARAM_SIZE_NAME);

			for (int i = 0; i < size; i++) {
				String key = provider.loadString(PERSISTENT_PARAM_KEY_NAME + i);
				String value = provider.loadString(PERSISTENT_PARAM_VALUE_NAME
						+ i);
				params.put(key, value);
			}
		} catch (PersistenceException ignore) {
			// Optional section, so it isn't an error if this doesn't work.
		}
		
		// load up the tooltip
		tooltip = provider.loadString(PERSISTENT_TOOLTIP_NAME);
		if (tooltip == null) {
			if (params.containsKey("email")) {
				tooltip = (String) params.get("email");
			} else if (params.containsKey("phone")) {
				tooltip = (String) params.get("phone");
			} else if (params.containsKey("url")) {
				tooltip = (String) params.get("url");
			}
		}
	}
	
	public void run() {
		if (params.size() > 0) {
			codeModule.getInvoker().invoke(params);
		}
		else {
			codeModule.getInvoker().invoke();
		}
	}
	
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

	public void storeUsing(IPersistenceProvider provider) throws PersistenceException {
		provider.storeLong(PERSISTENT_CODE_MODULE_ID_NAME, codeModule.getID());
		
		int size = params.size();
		provider.storeInt(PERSISTENT_PARAM_SIZE_NAME, size);
		
		int i = 0;
		for (Enumeration en = params.keys(); en.hasMoreElements();) {
			String key = (String) en.nextElement();
			String value = (String) params.get(key);
			provider.storeString(PERSISTENT_PARAM_KEY_NAME + i, key);
			provider.storeString(PERSISTENT_PARAM_VALUE_NAME + i, value);
			i++;
		}
		
		if (tooltip != null) {
			provider.storeString(PERSISTENT_TOOLTIP_NAME, tooltip);
		}
	}
	
	public void setCodeModule(CodeModule codeModule) {
		this.codeModule = codeModule;
	}

	public void setParam(String name, String value) {
		params.put(name, value);
	}

	public String toString() {
		return codeModule.getAppName();
	}	
}