package com.bbsmart.mobile.bb.gogo.runtime;

public interface PersistableRegistryListener {

	void onPersistableRegistryAdd(Object object);
	void onPersistableRegistryRemove(Object object);
	void onPersistableRegistryReplace(Object object);

}
