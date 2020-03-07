package com.bbsmart.mobile.bb.gogo.runtime.autocomplete.services;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.util.StringUtilities;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.adapters.FullNameModelAdapter;
import com.bbsmart.mobile.bb.gogo.adapters.SimpleStringAdapter;
import com.bbsmart.mobile.bb.gogo.model.AddressBookContact;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.AutoCompleteService;

public class FullNameAutoCompleteService extends AutoCompleteService {

    public FullNameAutoCompleteService() {
        super();
        
        setModelAdapter(new FullNameModelAdapter());
    }

    public void onAddressBookLoadCompleted() {
        if (isCancelled()) {
            return;
        }

        setModelAdapter(new FullNameModelAdapter());
        searchAddressBook();
    }

    protected void cancelAutoCompleteLookup() {
    }
    
    protected void doAutoCompleteLookup() {
        // Don't be opportunistic.
        if (request == null || request.equals("")) {
            notifyListeners(null);
        }

        if (! addressbook.isLoaded()) {
            // Not loaded yet.  Ask AddressBook to let us know when it's ready.
            addressbook.addAddressBookStatusListener(this);

            Vector results = new Vector();
            results.addElement(Config.RSC_STRING_ADDRESSBOOK_LOADING);
            setModelAdapter(new SimpleStringAdapter());
            notifyListeners(results);
            return;
        }
        
        searchAddressBook();
    }

    private void searchAddressBook() {
        Vector results = new Vector();
        
        // Don't be opportunistic.
        if (request == null || request.equals("")) {
            notifyListeners(results);
            return;
        }

        for (Enumeration e = addressbook.getAddresses(); e.hasMoreElements();) {
            AddressBookContact contact = (AddressBookContact) e.nextElement();

            if (contact.firstName != null) {
                if (StringUtilities.startsWithIgnoreCase(contact.firstName, request)) {
                    results.addElement(contact);
                    continue;
                }
            }
            
            if (contact.lastName != null) {
                if (StringUtilities.startsWithIgnoreCase(contact.lastName, request)) {
                    results.addElement(contact);
                    continue;
                }
            }
            
            String wholeName = getWholeName(contact);
            
            if (StringUtilities.startsWithIgnoreCase(wholeName, request)) {
                results.addElement(contact);
                continue;
            }
        }
        
        notifyListeners(results);
    }
    
    private String getWholeName(AddressBookContact contact) {
        StringBuffer buf = new StringBuffer();

        if (contact.firstName != null) {
            buf.append(contact.firstName);
            buf.append(" ");
        }

        if (contact.lastName != null) {
            buf.append(contact.lastName);
        }
        
        return buf.toString();
    }
    
}
