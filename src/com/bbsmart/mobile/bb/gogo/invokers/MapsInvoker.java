package com.bbsmart.mobile.bb.gogo.invokers;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MapsArguments;

public class MapsInvoker extends AbstractInvoker {

	public void invoke() {
		Invoke.invokeApplication(Invoke.APP_TYPE_MAPS, new MapsArguments());
	}

}
