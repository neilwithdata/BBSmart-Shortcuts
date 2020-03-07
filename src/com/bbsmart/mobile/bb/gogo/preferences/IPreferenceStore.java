package com.bbsmart.mobile.bb.gogo.preferences;

import java.util.Enumeration;

public interface IPreferenceStore {

	void clear();
	
	boolean containsKey(String key);

	boolean getBoolean(String key);
	boolean getBoolean(String key, boolean defaultValue);
	
	byte getByte(String key);
	byte getByte(String key, byte defaultValue);
	
	byte[] getByteArray(String key);
	
	double getDouble(String key);
	double getDouble(String key, double defaultValue);
	
	float getFloat(String key);
	float getFloat(String key, float defaultValue);
	
	int getInt(String key);
	int getInt(String key, int defaultValue);
	
	long getLong(String key);
	long getLong(String key, long defaultValue);
	
	String getString(String key);
	String getString(String key, String defaultValue);
	
	boolean isEmpty();
	
	Enumeration keys();
	
	void load();

	void putBoolean(String key, boolean value);
	void putByte(String key, byte value);
	void putByteArray(String key, byte[] value);
	void putDouble(String key, double value);
	void putFloat(String key, float value);
	void putInt(String key, int value);
	void putLong(String key, long value);
	void putString(String key, String value);
	
	void remove(String key);
	
	void save();
	int size();
	
}
