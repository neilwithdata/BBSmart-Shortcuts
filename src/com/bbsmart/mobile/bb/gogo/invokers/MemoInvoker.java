package com.bbsmart.mobile.bb.gogo.invokers;

import net.rim.blackberry.api.invoke.Invoke;
import net.rim.blackberry.api.invoke.MemoArguments;

public class MemoInvoker extends AbstractInvoker {

	public void invoke() {
		Invoke.invokeApplication(Invoke.APP_TYPE_MEMOPAD, new MemoArguments());
	}

}
