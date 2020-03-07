package com.bbsmart.mobile.bb.gogo.runtime.autocomplete.services;

import java.util.Enumeration;
import java.util.Vector;

import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.Font;
import net.rim.device.api.util.StringUtilities;

import com.bbsmart.mobile.bb.gogo.Config;
import com.bbsmart.mobile.bb.gogo.adapters.PhoneNumberModelAdapter;
import com.bbsmart.mobile.bb.gogo.model.AddressBookContact;
import com.bbsmart.mobile.bb.gogo.model.AddressBookContact.PhoneNumber;
import com.bbsmart.mobile.bb.gogo.runtime.autocomplete.AutoCompleteService;

public class PhoneNumberAutoCompleteService extends AutoCompleteService {

    PhoneNumberModelAdapter adapter = new PhoneNumberModelAdapter();

    public PhoneNumberAutoCompleteService() {
        super();
        
        setModelAdapter(adapter);
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
    
    private void populateResults(AddressBookContact addressbookContact, String request, Vector results) {
        Vector phoneNumbers = addressbookContact.getPhoneNumbers();
        
        for (Enumeration e = phoneNumbers.elements(); e.hasMoreElements();) {
            PhoneNumber phone = (PhoneNumber) e.nextElement();
            
            if (StringUtilities.startsWithIgnoreCase(phone.number, request)) {
                results.addElement(phone);
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
                
                Vector phoneNumbers = contact.getPhoneNumbers();
                
                for (Enumeration f = phoneNumbers.elements(); f.hasMoreElements();) {
                    PhoneNumber phone = (PhoneNumber) f.nextElement();
                    
                    if (StringUtilities.startsWithIgnoreCase(phone.number, request)) {
                        results.addElement(phone);
                    }
                }
            }
        }
        else {
            AddressBookContact contact = addressbook.findByUID(uid);
            populateResults(contact, request, results);
        }
        
        configureAdapter(results);
        
        notifyListeners(results);
    }
    
    private void configureAdapter(Vector results) {
        Font font = Font.getDefault();
        String longestAttributeLabel = null;
        boolean haveAttr = false;
        int longestAttributeLength = 0;

        for (Enumeration enumeration = results.elements(); enumeration.hasMoreElements();) {
            PhoneNumber phone = (PhoneNumber) enumeration.nextElement();
            
            if (phone.attr != null) {
                haveAttr = true;
                if (font.getAdvance(phone.attr) > longestAttributeLength) {
                    longestAttributeLabel = phone.attr;
                }
            }
        }
        
        if (haveAttr) {
            longestAttributeLabel += Config.RSC_STRING_CONTACT_SEPARATOR;
        }
        
        adapter.setLongestAttributeLabel(longestAttributeLabel);
    }
    
}
