package com.bbsmart.mobile.bb.gogo.invokers;

import java.util.Hashtable;

import net.rim.device.api.system.CodeModuleManager;

public abstract class AbstractInvoker implements Invokeable {

    protected int index;
    protected int moduleHandle;

	public void invoke() {}
	public void invoke(Hashtable params) {}
	public void setIndex(int index) { this.index = index; }

	public void setModuleHandle(int moduleHandle) {
	    this.moduleHandle = moduleHandle;
	}

	public void setModuleName(String moduleName) {
	    this.moduleHandle = CodeModuleManager.getModuleHandle(moduleName);
	}

}
