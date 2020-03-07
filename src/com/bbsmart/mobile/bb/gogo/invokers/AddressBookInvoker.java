package com.bbsmart.mobile.bb.gogo.invokers;

import net.rim.blackberry.api.invoke.AddressBookArguments;
import net.rim.blackberry.api.invoke.Invoke;

public class AddressBookInvoker extends AbstractInvoker {

	public void invoke() {
		Invoke.invokeApplication(Invoke.APP_TYPE_ADDRESSBOOK, new AddressBookArguments());
	}

}
