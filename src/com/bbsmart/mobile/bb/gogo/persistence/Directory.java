package com.bbsmart.mobile.bb.gogo.persistence;

import java.util.Enumeration;

public class Directory extends GoGoHashtable implements IPersistable {
	public void loadUsing(IPersistenceProvider provider) throws PersistenceException {
		int size = provider.loadInt("size");
		
		for (int i = 0; i < size; i++) {
			String key = provider.loadString("key" + i);
			String value = provider.loadString("value" + i);
			put(key, value);
		}
	}

	public void storeUsing(IPersistenceProvider provider) throws PersistenceException {
		provider.storeInt("size", size());
		
		int i = 0;
		for (Enumeration en = keys(); en.hasMoreElements();) {
			String key = (String) en.nextElement();
			String value = (String) get(key);
			provider.storeString("key" + i, key);
			provider.storeString("value" + i, value);
			i++;
		}
	}
}
