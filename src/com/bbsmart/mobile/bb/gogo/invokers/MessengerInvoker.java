package com.bbsmart.mobile.bb.gogo.invokers;

import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MessageArguments;

public class MessengerInvoker extends AbstractInvoker {

	public void invoke() {
		Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, new MessageArguments());
	}
	
	public void invoke(Hashtable params) {
		if (params.containsKey("email")) {
			invokeEmail(params);
		}
		else {
			invokeMessaging(params);
		}
	}
	
	private void invokeEmail(Hashtable params) {
		String email = (String) params.get("email");

		Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, 
				new MessageArguments(MessageArguments.ARG_NEW, email, "", ""));
	}

	private void invokeMessaging(Hashtable params) {
		try {
			String phone = (String) params.get("phone");
			String uri = "sms://" + phone;

			MessageConnection mc = (MessageConnection) Connector.open(uri);
			TextMessage message = (TextMessage) mc.newMessage(MessageConnection.TEXT_MESSAGE);

			message.setAddress(uri);
			message.setPayloadText("");

			Invoke.invokeApplication(Invoke.APP_TYPE_MESSAGES, new MessageArguments(message));
		}
		catch (IOException e) {
			System.out.println("MessengerInvoker.invoke exception: " + e.toString());
		}
	}

}
