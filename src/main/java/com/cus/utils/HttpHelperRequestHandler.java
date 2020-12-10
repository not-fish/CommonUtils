package com.cus.utils;

import java.net.URLConnection;

public interface HttpHelperRequestHandler {
	
	 public void OnPreSend(URLConnection request);
}