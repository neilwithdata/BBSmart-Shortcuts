package com.bbsmart.mobile.bb.gogo.runtime.autocomplete.services;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.util.StringUtilities;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.model.AddressBookContact;
import com.bbsmart.mobile.bb.gogo.model.AddressBookContact.EmailAddress;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.AutoCompleteService;

public class EmailAutoCompleteService extends AutoCompleteService {

    public EmailAutoCompleteService() {
        addressbook.addAddressBookStatusListener(this);
    }

    public void onAddressBookLoadCompleted() {
        if (isCancelled()) {
            return;
        }

        searchAddressBook();
    }

    protected void cancelAutoCompleteLookup() {
    }

    protected void doAutoCompleteLookup() {
        if (! addressbook.isLoaded()) {
            // Not loaded yet.  Ask AddressBook to let us know when it's ready.
            addressbook.addAddressBookStatusListener(this);

            Vector results = new Vector();
            results.addElement(Config.RSC_STRING_ADDRESSBOOK_LOADING);
            notifyListeners(results);
            return;
        }
        
        searchAddressBook();
    }
    
    private void populateResults(AddressBookContact contact, String request, Vector results) {
        Vector emailAddresses = contact.getEmailAddresses();
        
        for (Enumeration e = emailAddresses.elements(); e.hasMoreElements();) {
            EmailAddress email = (EmailAddress) e.nextElement();
            
            if (StringUtilities.startsWithIgnoreCase(email.address, request)) {
                results.addElement(email.address);
            }
        }
    }

    private void searchAddressBook() {
        String uid = (String) RuntimeStore.getRuntimeStore().get(Config.RUNTIME_STORE_UID_ID);
        Vector results = new Vector();

        if (uid == null) {
            // Don't be opportunistic.
            if (request == null || request.equals("")) {
                notifyListeners(results);
                return;
            }

            for (Enumeration e = addressbook.getAddresses(); e.hasMoreElements();) {
                AddressBookContact contact = (AddressBookContact) e.nextElement();
                
                Vector emails = contact.getEmailAddresses();
                
                for (Enumeration f = emails.elements(); f.hasMoreElements();) {
                    EmailAddress email = (EmailAddress) f.nextElement();
                    
                    if (StringUtilities.startsWithIgnoreCase(email.address, request)) {
                        results.addElement(email.address);
                    }
                }
            }
        }
        else {
            AddressBookContact contact = addressbook.findByUID(uid);
            populateResults(contact, request, results);
        }
        
        notifyListeners(results);
    }

}
