package com.bbsmart.mobile.bb.gogo.runtime;

import com.bbsmart.mobile.bb.gogo.model.CodeModule;

public interface CodeModuleRegistryListener {

	void onCodeModuleRegistryAdd(CodeModule metadata);
	void onCodeModuleRegistryRemove(CodeModule metadata);

}
