package com.bbsmart.mobile.bb.gogo.util;

import java.util.Enumeration;

import com.bbsmart.mobile.bb.gogo.model.CodeModule;
import com.bbsmart.mobile.bb.gogo.runtime.CodeModuleRegistry;

public class CodeModuleUtils {

	public static CodeModule getModuleByHandle(int moduleHandle) {
		CodeModule codeModule = null;

		CodeModuleRegistry registry = CodeModuleRegistry.instance;
		for (Enumeration en = registry.elements(); en.hasMoreElements();) {
			CodeModule module = (CodeModule) en.nextElement();

			if (module.getHandle() == moduleHandle) {
				codeModule = module;
				break;
			}
		}

		return codeModule;
	}

	public static CodeModule getModuleByID(long id) {
		CodeModule codeModule = null;

		CodeModuleRegistry registry = CodeModuleRegistry.instance;
		for (Enumeration en = registry.elements(); en.hasMoreElements();) {
			CodeModule module = (CodeModule) en.nextElement();

			if (module.getID() == id) {
				codeModule = module;
				break;
			}
		}

		return codeModule;
	}

	public static CodeModule getModuleByModuleName(String moduleName) {
		CodeModule codeModule = null;

		CodeModuleRegistry registry = CodeModuleRegistry.instance;
		for (Enumeration en = registry.elements(); en.hasMoreElements();) {
			CodeModule module = (CodeModule) en.nextElement();

			if (module.getModuleName().equals(moduleName)) {
				codeModule = module;
				break;
			}
		}

		return codeModule;
	}

}
