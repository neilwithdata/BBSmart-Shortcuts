package com.bbsmart.mobile.bb.gogo.model;

import com.bbsmart.mobile.bb.gogo.invokers.Invokeable;

import net.rim.device.api.system.Bitmap;

public class CodeModule {

    // Surrogate ID that we use to uniquely identify code modules.
    // The handle isn't good enough because we sometimes use the same module/handle
    // to identify mutiple "virtual" applications, such as "Memo" and "New Memo".
    // For all third-party apps this id will equal the handle; for all overrides
    // we'll use the upper 32-bits to identify them.  It's magic, and that's why
    // it's being explained here.
    private long id;
    private int handle; // for backwards-compatibility; do not use as unique identifier
	private Bitmap icon;
	private String appName;
	private String moduleName;
	private Invokeable invoker;

	public long getID() {
	    return id;
	}

	public Bitmap getIcon() {
		return icon;
	}
	
	public Invokeable getInvoker() {
		return invoker;
	}
	
	// for backwards-compatibility; do not use as unique identifier
	public int getHandle() {
		return handle;
	}
	
	public String getModuleName() {
		return moduleName;
	}

	public String getAppName() {
		return appName;
	}
	
	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}
	
	public void setInvoker(Invokeable invoker) {
		this.invoker = invoker;
	}
	
	// for backwards-compatibility; do not use as unique identifier
	public void setHandle(int handle) {
		this.handle = handle;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public void setID(long id) {
	    this.id = id;
	}

	public String toString() {
		return appName;
	}
	
}
