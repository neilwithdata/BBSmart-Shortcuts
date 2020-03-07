package com.bbsmart.mobile.bb.gogo.invokers;

import net.rim.blackberry.api.invoke.CalendarArguments;
import net.rim.blackberry.api.invoke.Invoke;

public class CalendarInvoker extends AbstractInvoker {

	public void invoke() {
		Invoke.invokeApplication(Invoke.APP_TYPE_CALENDAR, 
				new CalendarArguments(CalendarArguments.ARG_VIEW_DEFAULT));
	}

}
