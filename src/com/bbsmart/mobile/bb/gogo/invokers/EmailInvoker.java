package com.bbsmart.mobile.bb.gogo.invokers;

import java.util.Hashtable;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;

public class EmailInvoker extends AbstractInvoker {

	public void invoke(Hashtable params) {
		String email = (String) params.get("email");

		Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, 
				new MessageArguments(MessageArguments.ARG_NEW, email, "", ""));
	}

}
