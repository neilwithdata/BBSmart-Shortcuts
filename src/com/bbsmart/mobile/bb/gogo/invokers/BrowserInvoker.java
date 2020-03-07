package com.bbsmart.mobile.bb.gogo.invokers;

import java.util.Hashtable;

import net.rim.blackberry.api.browser.Browser;

public class BrowserInvoker extends AbstractInvoker {

	public void invoke() {
		Browser.getDefaultSession().showBrowser();
	}
	
	public void invoke(Hashtable params) {
		String url = (String) params.get("url");

		Browser.getDefaultSession().displayPage(url);
	}

}
