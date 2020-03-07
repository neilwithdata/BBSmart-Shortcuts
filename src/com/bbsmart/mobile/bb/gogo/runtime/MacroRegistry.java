package com.bbsmart.mobile.bb.gogo.runtime;

import com.bbsmart.mobile.bb.gogo.model.Shortcut;

public class MacroRegistry extends PersistableRegistry {

	public static final MacroRegistry instance = new MacroRegistry();
	
	public void moveElement(int oldIndex, int newIndex) {
	    Object obj = objects.elementAt(oldIndex);

	    objects.removeElementAt(oldIndex);
	    
	    if (newIndex < objects.size()) {
	        objects.insertElementAt(obj, newIndex);
	    }
	    else {
	        objects.addElement(obj);
	    }
	}
	
	public void replace(Shortcut original, Shortcut replacement) {
	    int numElements = objects.size();
	    
	    for (int i = 0; i < numElements; i++) {
	        Shortcut obj = (Shortcut) objects.elementAt(i);
	        
	        if (obj == original) {
	            objects.setElementAt(replacement, i);
	            notifyReplace(replacement);
	            break;
	        }
	    }
	}

}
