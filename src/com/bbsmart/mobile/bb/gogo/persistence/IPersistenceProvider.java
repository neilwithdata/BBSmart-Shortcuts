package com.bbsmart.mobile.bb.gogo.persistence;

public interface IPersistenceProvider {

	boolean loadBoolean(String key) throws PersistenceException;
	byte loadByte(String key) throws PersistenceException;
	byte[] loadByteArray(String key) throws PersistenceException;
	double loadDouble(String key) throws PersistenceException;
	float loadFloat(String key) throws PersistenceException;
	int loadInt(String key) throws PersistenceException;
	long loadLong(String key) throws PersistenceException;
	IPersistable loadPersistable(String key) throws PersistenceException;
	String loadString(String key) throws PersistenceException;

	void storeBoolean(String key, boolean value) throws PersistenceException;
	void storeByte(String key, byte value) throws PersistenceException;
	void storeByteArray(String key, byte[] value) throws PersistenceException;
	void storeDouble(String key, double value) throws PersistenceException;
	void storeFloat(String key, float value) throws PersistenceException;
	void storeInt(String key, int value) throws PersistenceException;
	void storeLong(String key, long value) throws PersistenceException;
	void storePersistable(String key, IPersistable value) throws PersistenceException;
	void storeString(String key, String value) throws PersistenceException;

}
