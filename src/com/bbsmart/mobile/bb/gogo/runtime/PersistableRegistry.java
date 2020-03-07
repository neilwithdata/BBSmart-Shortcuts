package com.bbsmart.mobile.bb.gogo.runtime;

import java.util.Enumeration;
import java.util.Vector;

import com.bbsmart.mobile.bb.gogo.model.Shortcut;
import com.bbsmart.mobile.bb.gogo.persistence.IPersistable;
import com.bbsmart.mobile.bb.gogo.persistence.IPersistenceProvider;
import com.bbsmart.mobile.bb.gogo.persistence.PersistenceException;

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

public class PersistableRegistry implements IPersistable, ListFieldCallback {

	private static final String PERSISTENT_OBJECT_NAME	= "object";
	private static final String PERSISTENT_OBJECT_COUNT	= "numObjects";
	
	protected IPersistenceProvider provider;
	protected Vector objects = new Vector();
	protected Vector listeners = new Vector();
	protected boolean isLoaded = false;

	public PersistableRegistry() {
	}
	
	public void add(Object object) {
		objects.addElement(object);
		notifyAdd(object);
	}
	
	public void addListener(PersistableRegistryListener listener) {
		if (! listeners.contains(listener)) {
			listeners.addElement(listener);
		}
	}

	public void remove(Object object) {
		objects.removeElement(object);
		notifyRemove(object);
	}
	
	public void removeAt(int index) {
		Object object = objects.elementAt(index);
		objects.removeElementAt(index);
		notifyRemove(object);
	}

	public void drawListRow(ListField list, Graphics graphics, int index, int y, int w) {
		// TODO: Move this binding to Shortcut to an Adapter.
		Shortcut shortcut = (Shortcut) objects.elementAt(index);
		String name = (String) shortcut.getParams().get("name");
		graphics.drawText(name, 0, y, 0, w);
	}
	
	public Enumeration elements() {
		return objects.elements();
	}

	public boolean exists(Object obj) {
	    return objects.contains(obj);
	}

	public Object get(ListField listField, int index) {
		return objects.elementAt(index);
	}

	public int getPreferredWidth(ListField listField) {
		return Display.getWidth();
	}

	public int indexOfList(ListField listField, String prefix, int start) {
		return listField.getSelectedIndex();
	}
	
	public boolean isLoaded() {
		return isLoaded;
	}
	
	public void clear() {
		objects.removeAllElements();
	}
	
	public void load() throws PersistenceException {
		if (provider == null) {
			System.out.println("ShortcutRegistry: provider not set.");
			return;
		}
		
		loadUsing(provider);
		
		isLoaded = true;
	}
	
	public void loadUsing(IPersistenceProvider provider) throws PersistenceException {
		int index = provider.loadInt(PERSISTENT_OBJECT_COUNT);

		for (int i = 0; i < index; i++) {
			try {
				Object object = provider.loadPersistable(PERSISTENT_OBJECT_NAME + i);
				add(object);
			} catch (PersistenceException pe) {
				// If one of the particular items that we are trying to load in to the registry should fail:
				// ignore it, and move on
			}
		}
	}

	public void notifyAdd(Object object) {
		for (Enumeration en = listeners.elements(); en.hasMoreElements();) {
			PersistableRegistryListener listener = (PersistableRegistryListener) en.nextElement();
			listener.onPersistableRegistryAdd(object);
		}
	}
	
	public void notifyRemove(Object object) {
		for (Enumeration en = listeners.elements(); en.hasMoreElements();) {
			PersistableRegistryListener listener = (PersistableRegistryListener) en.nextElement();
			listener.onPersistableRegistryRemove(object);
		}
	}
	
    public void notifyReplace(Object object) {
        for (Enumeration en = listeners.elements(); en.hasMoreElements();) {
            PersistableRegistryListener listener = (PersistableRegistryListener) en.nextElement();
            listener.onPersistableRegistryReplace(object);
        }
    }
	
	public void removeListener(PersistableRegistryListener listener) {
		if (listeners.contains(listener)) {
			listeners.removeElement(listener);
		}
	}
	
	public void setPersistenceProvider(IPersistenceProvider provider) {
		this.provider = provider;
	}
	
	public int size() {
	    return objects.size();
	}

	public void store() {
		if (provider == null) {
			System.out.println("ShortcutRegistry: provider not set.");
			return;
		}
		
		storeUsing(provider);
	}

	public void storeUsing(IPersistenceProvider provider) throws PersistenceException {
		try {
			int index = 0;
			int size = objects.size();

			provider.storeInt(PERSISTENT_OBJECT_COUNT, size);
			
			for (int i = 0; i < size; i++) {
				IPersistable object = (IPersistable) objects.elementAt(i);
				provider.storePersistable(PERSISTENT_OBJECT_NAME + index++, object);
			}
		}
		catch (PersistenceException sysout) {
			System.out.println("ShortcutRegistry.loadUsing: unable to store");
		}
	}

}
