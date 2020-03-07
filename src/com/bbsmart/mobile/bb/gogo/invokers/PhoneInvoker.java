package com.bbsmart.mobile.bb.gogo.invokers;

import java.util.Hashtable;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.PhoneArguments;

public class PhoneInvoker extends AbstractInvoker {

	public void invoke() {
		Invoke.invokeApplication(Invoke.APP_TYPE_PHONE, new PhoneArguments());
	}
	
	public void invoke(Hashtable params) {
		String phone = (String) params.get("phone");

		PhoneArguments args = new PhoneArguments(PhoneArguments.ARG_CALL, phone);
		
		Invoke.invokeApplication(Invoke.APP_TYPE_PHONE, args);
	}

}
