package com.bbsmart.mobile.bb.gogo.preferences;

import java.util.Enumeration;
import java.util.Hashtable;

import com.bbsmart.mobile.bb.gogo.persistence.Directory;
import com.bbsmart.mobile.bb.gogo.persistence.IPersistenceProvider;
import com.bbsmart.mobile.bb.gogo.persistence.PersistenceException;
import com.bbsmart.mobile.bb.gogo.persistence.PersistentStorePersistenceProvider;


public class PreferenceStore implements IPreferenceStore {

	private static Hashtable storeRefs = new Hashtable();

	private Directory directory;
	private IPersistenceProvider provider;
	
	public static synchronized IPreferenceStore openPreferenceStore(String storeName) {
		IPreferenceStore storeRef = null;
		
		if (storeRefs.containsKey(storeName)) {
			try {
				storeRef = (IPreferenceStore) storeRefs.get(storeName);
			}
			catch (Exception e) {
				System.out.println("XXX PreferenceStore.openPreferenceStore: " + e.toString());
			}
		}
		else {
			storeRef = newStoreRef(storeName);
			storeRefs.put(storeName, storeRef);
		}
		
		return storeRef;
	}

	protected PreferenceStore(String storeName) {
		this.provider = new PersistentStorePersistenceProvider(storeName);
	}

	public void clear() {
		directory.clear();
	}

	public boolean containsKey(String key) {
		return directory.containsKey(key);
	}

	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		boolean value = defaultValue;

		String stringValue = (String) directory.get(key);

		if (stringValue != null) {
			value = stringValue.equals("true");
		}

		return value;
	}
	
	public byte getByte(String key) {
		return getByte(key, (byte) 0);
	}

	public byte getByte(String key, byte defaultValue) {
		byte value = defaultValue;
		
		String stringValue = (String) directory.get(key);

		if (stringValue != null) {
			value = Byte.parseByte(stringValue);
		}
		
		return value;
	}
	
	public byte[] getByteArray(String key) {
		byte[] value = null;
		
		String stringValue = (String) directory.get(key);

		if (stringValue != null) {
			value = stringValue.getBytes();
		}
		
		return value;
	}
	
	public double getDouble(String key) {
		return getDouble(key, 0.0);
	}

	public double getDouble(String key, double defaultValue) {
		double value = defaultValue;
		
		String stringValue = (String) directory.get(key);

		if (stringValue != null) {
			value = Double.parseDouble(stringValue);
		}
		
		return value;
	}

	public float getFloat(String key) {
		return getFloat(key, (float) 0.0);
	}

	public float getFloat(String key, float defaultValue) {
		float value = defaultValue;
		
		String stringValue = (String) directory.get(key);

		if (stringValue != null) {
			value = Float.parseFloat(stringValue);
		}
		
		return value;
	}
	
	public int getInt(String key) {
		return getInt(key, 0);
	}
	
	public int getInt(String key, int defaultValue) {
		int value = defaultValue;
		
		String stringValue = (String) directory.get(key);

		if (stringValue != null) {
			value = Integer.parseInt(stringValue);
		}
		
		return value;
	}

	public long getLong(String key) {
		return getLong(key, 0L);
	}

	public long getLong(String key, long defaultValue) {
		long value = defaultValue;
		
		String stringValue = (String) directory.get(key);

		if (stringValue != null) {
			value = Long.parseLong(stringValue);
		}
		
		return value;
	}

	public String getString(String key) {
		return getString(key, null);
	}

	public String getString(String key, String defaultValue) {
		String value = defaultValue;
		
		String stringValue = (String) directory.get(key);
		
		if (stringValue != null) {
			value = stringValue;
		}
		
		return value;
	}
	
	public boolean isEmpty() {
		return directory.isEmpty();
	}
	
	public Enumeration keys() {
		return directory.keys();
	}
	
	public void putBoolean(String key, boolean value) {
		putString(key, (new Boolean(value)).toString());
	}
	
	public void putByte(String key, byte value) {
		putString(key, (new Byte(value)).toString());
	}
	
	public void putByteArray(String key, byte[] value) {
		putString(key, new String(value));
	}
	
	public void putDouble(String key, double value) {
		putString(key, (new Double(value)).toString());
	}
	
	public void putFloat(String key, float value) {
		putString(key, (new Float(value)).toString());
	}

	public void putInt(String key, int value) {
		putString(key, (new Integer(value)).toString());
	}

	public void putLong(String key, long value) {
		putString(key, (new Long(value)).toString());
	}

	public void putString(String key, String value) {
		directory.put(key, value);
	}
	
	public void remove(String key) {
		directory.remove(key);
	}
	
	public void load() {
		try {
			directory = (Directory) provider.loadPersistable("directory");
		}
		catch (PersistenceException ignore) {
			// Directory does not yet exist.
		}
		
		if (directory == null) {
			directory = new Directory();
		}
	}

	public void save() {
		provider.storePersistable("directory", directory);
	}

	public int size() {
		return directory.size();
	}
	
	private static PreferenceStore newStoreRef(String storeName) {
		PreferenceStore storeRef = null;
		
		storeRef = new PreferenceStore(storeName);
		storeRef.load();

		return storeRef;
	}
	
}
