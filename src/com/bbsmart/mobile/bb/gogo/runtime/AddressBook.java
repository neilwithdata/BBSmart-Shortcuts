package com.bbsmart.mobile.bb.gogo.runtime;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.pim.Contact;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMItem;

import net.rim.blackberry.api.pdap.BlackBerryContactList;
import net.rim.blackberry.api.pdap.PIMListListener;


import com.bbsmart.mobile.bb.gogo.model.AddressBookContact;
import com.bbsmart.mobile.bb.gogo.model.AddressBookContact.EmailAddress;
import com.bbsmart.mobile.bb.gogo.model.AddressBookContact.PhoneNumber;

public class AddressBook implements PIMListListener {

    public static final int STATUS_NOT_LOADED = 0;
    public static final int STATUS_LOADING    = 1;
    public static final int STATUS_LOADED     = 2;

    public static AddressBook instance = new AddressBook();
    
    private BlackBerryContactList contactList;
    private Hashtable addresses = new Hashtable();
    private Vector listeners = new Vector();

    private int status = STATUS_NOT_LOADED;
   
    private AddressBook() {
        try {
            contactList = (BlackBerryContactList) PIM.getInstance().openPIMList(
                    PIM.CONTACT_LIST, PIM.READ_ONLY);

            contactList.addListener(this);
        }
        catch (PIMException e) {
            System.out.println("AddressBook.ctor exception: " + e.toString());
        }

        if (! isLoaded()) {
            load();
        }
    }
    
    public void addAddressBookStatusListener(AddressBookStatusListener listener) {
        if (! listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }
    
    public void removeAddressBookStatusListener(AddressBookStatusListener listener) {
        if (listeners.contains(listener)) {
            listeners.removeElement(listener);
        }
    }

    public AddressBookContact findByUID(String uid) {
        return (AddressBookContact) addresses.get(uid);
    }
    
    public Enumeration getAddresses() {
        return addresses.elements();
    }

    public boolean isLoaded() {
        return status == STATUS_LOADED;
    }

    /*
     * (non-Javadoc)
     * @see net.rim.blackberry.api.pdap.PIMListListener#itemAdded(javax.microedition.pim.PIMItem)
     */
    public void itemAdded(PIMItem item) {
        if (item instanceof Contact) {
            Contact contact = (Contact) item;
            addContact(contact);
        }
    }

    /*
     * (non-Javadoc)
     * @see net.rim.blackberry.api.pdap.PIMListListener#itemRemoved(javax.microedition.pim.PIMItem)
     */
    public void itemRemoved(PIMItem item) {
        if (item instanceof Contact) {
            Contact contact = (Contact) item;
            removeContact(contact);
        }
    }

    /*
     * (non-Javadoc)
     * @see net.rim.blackberry.api.pdap.PIMListListener#itemUpdated(javax.microedition.pim.PIMItem, javax.microedition.pim.PIMItem)
     */
    public void itemUpdated(PIMItem oldItem, PIMItem newItem) {
        if (oldItem instanceof Contact) {
            Contact oldContact = (Contact) oldItem;
            removeContact(oldContact);
            
            Contact newContact = (Contact) newItem;
            addContact(newContact);
        }
    }

    private void addContact(Contact contact) {
        AddressBookContact addressbookContact = new AddressBookContact();

        int[] fieldIds = contact.getFields();

        for (int index = 0; index < fieldIds.length; index++) {
            int id = fieldIds[index];

            if (id == Contact.EMAIL) {
                loadEmailFromContact(addressbookContact, contact, id);
            }
            else if (id == Contact.NAME) {
                loadNameFromContact(addressbookContact, contact, id);
            }
            else if (id == Contact.TEL) {
                loadPhoneFromContact(addressbookContact, contact, id);
            }
            else if (id == Contact.UID) {
                loadUIDFromContact(addressbookContact, contact, id);
            }
        }

        addresses.put(addressbookContact.getUID(), addressbookContact);
    }

    private void load() {
        new Thread() {
            public void run() {
                status = STATUS_LOADING;
                
                notifyListenersLoadStarted();

                try {
                    Enumeration en = contactList.items();

                    while (en.hasMoreElements()) {
                        Contact contact = (Contact) en.nextElement();
                        addContact(contact);
                    }
                }
                catch (PIMException e) {
                    System.out.println("AddressBook.load exception: " + e.toString());
                }

                status = STATUS_LOADED;
                
                notifyListenersLoadCompleted();
            }
        }.start();
    }
    
    private void loadEmailFromContact(AddressBookContact abContact, Contact contact, int id) {
        Vector emailAddresses = abContact.getEmailAddresses();

        for (int i = 0; i < contact.countValues(id); i++) {
            EmailAddress newEmailAddress = new EmailAddress();

            newEmailAddress.address = contact.getString(id, i);
            
            int attrs = contact.getAttributes(id, i);
            int[] supportedAttrs = contact.getPIMList().getSupportedAttributes(id);
            
            for (int j = 0; j < supportedAttrs.length; j++) {
                if ((attrs & supportedAttrs[j]) == supportedAttrs[j]) {
                    String attrName = contact.getPIMList().getAttributeLabel(supportedAttrs[j]);
                    newEmailAddress.attr = attrName;
                }
            }
            
            emailAddresses.addElement(newEmailAddress);
        }
    }

    private void loadNameFromContact(AddressBookContact abContact, Contact contact, int id) {
        for (int i = 0; i < contact.countValues(id); i++) {
            String[] values = contact.getStringArray(id, i);

            abContact.setFirstName(values[Contact.NAME_GIVEN]);
            abContact.setLastName(values[Contact.NAME_FAMILY]);
        }
    }
    
    private void loadPhoneFromContact(AddressBookContact abContact, Contact contact, int id) {
        Vector phoneNumbers = abContact.getPhoneNumbers();

        for (int i = 0; i < contact.countValues(id); i++) {
            PhoneNumber newPhoneNumber = new PhoneNumber();

            newPhoneNumber.number = contact.getString(id, i);
            
            int attrs = contact.getAttributes(id, i);
            int[] supportedAttrs = contact.getPIMList().getSupportedAttributes(id);
            
            for (int j = 0; j < supportedAttrs.length; j++) {
                if ((attrs & supportedAttrs[j]) == supportedAttrs[j]) {
                    String attrName = contact.getPIMList().getAttributeLabel(supportedAttrs[j]);
                    newPhoneNumber.attr = attrName;
                }
            }
            
            phoneNumbers.addElement(newPhoneNumber);
        }
    }
    
    private void loadUIDFromContact(AddressBookContact abContact, Contact contact, int id) {
        abContact.setUID(contact.getString(id, 0));
    }
    
    private void notifyListenersLoadCompleted() {
        for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
            AddressBookStatusListener listener = (AddressBookStatusListener) e.nextElement();
            listener.onAddressBookLoadCompleted();
        }
    }
    
    private void notifyListenersLoadStarted() {
        for (Enumeration e = listeners.elements(); e.hasMoreElements();) {
            AddressBookStatusListener listener = (AddressBookStatusListener) e.nextElement();
            listener.onAddressBookLoadStarted();
        }
    }
    
    private void removeContact(Contact contact) {
        String uid = contact.getString(Contact.UID, 0);
        addresses.remove(uid);
    }

}