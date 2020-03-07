package com.bbsmart.mobile.bb.gogo.model;

import java.util.Vector;

import net.rim.device.api.util.StringUtilities;

public class AddressBookContact {
    
    public Vector emailAddresses = new Vector();
    public Vector phoneNumbers = new Vector();
    public String firstName;
    public String lastName;
    public String uid;
    
    public static class EmailAddress {
        public String attr;
        public String address;
    }
    
    public static class PhoneNumber {
        public String attr;
        public String number;
    }
    
    public Vector getEmailAddresses() {
        return emailAddresses;
    }
    
    public Vector getPhoneNumbers() {
        return phoneNumbers;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    public String getUID() {
        return uid;
    }
    
    public boolean emailMatches(String match) {
        int numAddresses = emailAddresses.size();
        
        for (int i = 0; i < numAddresses; i++) {
            EmailAddress email = (EmailAddress) emailAddresses.elementAt(i);
            
            //System.out.println("emailMatches: checking " + email.address + " with " + match);
            
            if (StringUtilities.startsWithIgnoreCase(email.address, match)) {
                return true;
            }
        }
        
        return false;
    }

    public boolean nameMatches(String match) {
        if (firstName != null) {
            if (StringUtilities.startsWithIgnoreCase(firstName, match)) {
                return true;
            }
        }
        
        if (lastName != null) {
            if (StringUtilities.startsWithIgnoreCase(lastName, match)) {
                return true;
            }
        }
        
        if (StringUtilities.startsWithIgnoreCase(toString(), match)) {
            return true;
        }
        
        return false;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUID(String uid) {
        this.uid = uid;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();

        if (firstName != null) {
            buf.append(firstName).append(" ");
        }
        if (lastName != null) {
            buf.append(lastName);
        }
        
        return buf.toString();
    }

}
