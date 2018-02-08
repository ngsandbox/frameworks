package org.ngsandbox.reuters.consumer.MarketPrice.ErrorClient;

import com.thomsonreuters.ema.access.OmmConsumerErrorClient;

class AppErrorClient implements OmmConsumerErrorClient
{
	public void onInvalidHandle(long handle, String text)
	{
		System.out.println("onInvalidHandle callback function" + "\nInvalid handle: " + handle + "\nError text: " + text);
	}

	public void onInvalidUsage(String text) {
		System.out.println("onInvalidUsage callback function" + "\nError text: " + text);

	}
}
