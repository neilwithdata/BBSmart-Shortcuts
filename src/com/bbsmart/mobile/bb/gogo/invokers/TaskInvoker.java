package com.bbsmart.mobile.bb.gogo.invokers;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.TaskArguments;

public class TaskInvoker extends AbstractInvoker {

	public void invoke() {
		Invoke.invokeApplication(Invoke.APP_TYPE_TASKS, new TaskArguments());
	}

}
