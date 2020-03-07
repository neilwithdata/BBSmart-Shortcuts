package com.bbsmart.mobile.bb.gogo.runtime.autocomplete.services;

import java.util.Vector;

import net.rim.device.api.system.Clipboard;

import com.bbsmart.mobile.bb.gogo.adapters.WebModelAdapter;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.AutoCompleteService;

public class WebAutoCompleteService extends AutoCompleteService {

    public WebAutoCompleteService() {
        super();
        
        setModelAdapter(new WebModelAdapter());
    }

    protected void cancelAutoCompleteLookup() {
    }
    
    protected void doAutoCompleteLookup() {
        Vector results = new Vector();

        Clipboard clipboard = Clipboard.getClipboard();
        
        if (clipboard != null) {
            try {
                String str = (String) clipboard.get();
                results.addElement(str);
            }
            catch (ClassCastException ignore) {
                // If it's not a String then don't show it.
            }
        }
        
        notifyListeners(results);
    }

}
