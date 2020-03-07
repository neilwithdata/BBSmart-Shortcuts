package com.bbsmart.mobile.bb.gogo.invokers;

import net.rim.blackberry.api.invoke.CameraArguments;
import net.rim.blackberry.api.invoke.Invoke;

public class CameraInvoker extends AbstractInvoker {

	public void invoke() {
		Invoke.invokeApplication(Invoke.APP_TYPE_CAMERA, new CameraArguments());
	}

}
