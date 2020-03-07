package com.bbsmart.mobile.bb.gogo.invokers;

import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.ApplicationManagerException;
import net.rim.device.api.system.CodeModuleManager;

public class ThirdPartyInvoker extends AbstractInvoker {

	public ThirdPartyInvoker() {
	    this(0, 0);
	}

	public ThirdPartyInvoker(int moduleHandle) {
	    this(moduleHandle, 0);
	}

	public ThirdPartyInvoker(int moduleHandle, int index) {
	    this.index = index;
		this.moduleHandle = moduleHandle;
	}

	public ThirdPartyInvoker(String moduleName) {
	    this(CodeModuleManager.getModuleHandle(moduleName), 0);
	}
	
	public ThirdPartyInvoker(String moduleName, int index) {
	    this(CodeModuleManager.getModuleHandle(moduleName), index);
	}

	public void invoke() {
		try {
			ApplicationDescriptor[] apDes = CodeModuleManager.getApplicationDescriptors(moduleHandle);
			ApplicationManager.getApplicationManager().runApplication(apDes[index]);
		}
		catch (ApplicationManagerException e) {
			System.out.println("ThirdPartyInvoker exception " + e.toString());
		}
	}
	
}
