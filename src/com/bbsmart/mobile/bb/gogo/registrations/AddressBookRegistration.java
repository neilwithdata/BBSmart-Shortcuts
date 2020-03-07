package com.bbsmart.mobile.bb.gogo.registrations;

import javax.microedition.pim.Contact;

import net.rim.blackberry.api.menuitem.ApplicationMenuItem;
import net.rim.blackberry.api.menuitem.ApplicationMenuItemRepository;

public class AddressBookRegistration {

	public static void register() {
		ApplicationMenuItem item = new ApplicationMenuItem(0) {
			public Object run(Object context) {
				process(context);
				return context;
			}
			public String toString() {
				return "Add to BBSmart Shortcuts";
			}
		};

		ApplicationMenuItemRepository.getInstance().addMenuItem(
				ApplicationMenuItemRepository.MENUITEM_ADDRESSBOOK_LIST, item);
	}
	
	public static void process(Object context) {
		if (! (context instanceof Contact)) {
			return;
		}
	}

}
