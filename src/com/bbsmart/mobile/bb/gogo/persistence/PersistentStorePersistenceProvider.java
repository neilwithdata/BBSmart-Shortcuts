package com.bbsmart.mobile.bb.gogo.persistence;

import java.util.Stack;

import com.bbsmart.mobile.bb.gogo.util.StringUtils;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

public class PersistentStorePersistenceProvider implements IPersistenceProvider {
	
	private static final String CLASS_NAME_ATTRIBUTE	= "__class__";

	private class StoreContext {

		public GoGoHashtable table;
		public PersistentObject store;
		public String name;
		public long key;

		public StoreContext(String name) {
			this.name = name;
			this.key = StringUtils.stringToLong(name);
			store = PersistentStore.getPersistentObject(key);
			table = (GoGoHashtable) store.getContents();
			
			if (table == null) {
				table = new GoGoHashtable();
				store.setContents(table);
				store.commit();
			}
		}
	}

	private Stack contexts = new Stack();
	private StoreContext curContext = null;
	
	public PersistentStorePersistenceProvider(String name) {
		pushStoreContext(name);
	}
	
	public boolean loadBoolean(String key) throws PersistenceException {
		try {
			return ((Boolean) curContext.table.get(key)).booleanValue();
		}
		catch (Exception e) {
			throw new PersistenceException(e.toString());
		}
	}
	
	public byte loadByte(String key) throws PersistenceException {
		try {
			return ((Byte) curContext.table.get(key)).byteValue();
		}
		catch (Exception e) {
			throw new PersistenceException(e.toString());
		}
	}
	
	public byte[] loadByteArray(String key) throws PersistenceException {
		try {
			String stringValue = (String) curContext.table.get(key);
			return stringValue.getBytes();
		}
		catch (Exception e) {
			throw new PersistenceException(e.toString());
		}
	}
	
	public double loadDouble(String key) throws PersistenceException {
		try {
			return ((Double) curContext.table.get(key)).doubleValue();
		}
		catch (Exception e) {
			throw new PersistenceException(e.toString());
		}
	}
	
	public float loadFloat(String key) throws PersistenceException {
		try {
			return ((Float) curContext.table.get(key)).floatValue();
		}
		catch (Exception e) {
			throw new PersistenceException(e.toString());
		}
	}

	public int loadInt(String key) throws PersistenceException {
		try {
			return ((Integer) curContext.table.get(key)).intValue();
		}
		catch (Exception e) {
			throw new PersistenceException(e.toString());
		}
	}
	
	public long loadLong(String key) throws PersistenceException {
		try {
			return ((Long) curContext.table.get(key)).longValue();
		}
		catch (Exception e) {
			throw new PersistenceException(e.toString());
		}
	}

	public IPersistable loadPersistable(String name) throws PersistenceException {
		IPersistable persistable = null;

		String newName = curContext.name + "." + name;
		
		pushStoreContext(newName);
		
		try {
			persistable = createPersistableInstance();
			persistable.loadUsing(this);
		}
		catch (PersistenceException e) {
			throw e;
		}
		finally {
			popStoreContext();
		}

		return persistable;
	}

	public String loadString(String key) throws PersistenceException {
		try {
			return (String) curContext.table.get(key);
		}
		catch (Exception e) {
			throw new PersistenceException(e.toString());
		}
	}

	public void storeByte(String key, byte value) throws PersistenceException {
		curContext.table.put(key, new Byte(value));
		curContext.store.commit();
	}
	
	public void storeByteArray(String key, byte[] value) throws PersistenceException {
		curContext.table.put(key, new String(value));
		curContext.store.commit();
	}

	public void storeBoolean(String key, boolean value) throws PersistenceException {
		curContext.table.put(key, new Boolean(value));
		curContext.store.commit();
	}

	public void storeDouble(String key, double value) throws PersistenceException {
		curContext.table.put(key, new Double(value));
		curContext.store.commit();
	}
	
	public void storeFloat(String key, float value) throws PersistenceException {
		curContext.table.put(key, new Float(value));
		curContext.store.commit();
	}
	
	public void storeInt(String key, int value) throws PersistenceException {
		curContext.table.put(key, new Integer(value));
		curContext.store.commit();
	}
	
	public void storeLong(String key, long value) throws PersistenceException {
		curContext.table.put(key, new Long(value));
		curContext.store.commit();
	}

	public void storePersistable(String key, IPersistable value) throws PersistenceException {
		String newName = curContext.name + "." + key;
		pushStoreContext(newName);
		curContext.table.clear();
		curContext.store.commit();
		curContext.table.put(CLASS_NAME_ATTRIBUTE, value.getClass().getName());
		value.storeUsing(this);
		popStoreContext();
	}

	public void storeString(String name, String value) throws PersistenceException {
		curContext.table.put(name, value);
		curContext.store.commit();
	}
	
	private IPersistable createPersistableInstance() throws PersistenceException {
		IPersistable persistable = null;
		
		String className = (String) curContext.table.get(CLASS_NAME_ATTRIBUTE);

		try {
			Class clazz = Class.forName(className);

			if (clazz != null) {
				persistable = (IPersistable) clazz.newInstance();
			}
		}
		catch (ClassNotFoundException e) {
			throw new PersistenceException(e.getMessage());
		}
		catch (InstantiationException e) {
			throw new PersistenceException(e.getMessage());
		}
		catch (IllegalAccessException e) {
			throw new PersistenceException(e.getMessage());
		}
		catch (NullPointerException e) {
			throw new PersistenceException(e.getMessage());
		}
		
		return persistable;
	}
	
	protected void popStoreContext() {
		contexts.pop();
		curContext = (StoreContext) contexts.peek();
	}

	protected void pushStoreContext(String name) {
		contexts.push(new StoreContext(name));
		curContext = (StoreContext) contexts.peek();
	}
}
