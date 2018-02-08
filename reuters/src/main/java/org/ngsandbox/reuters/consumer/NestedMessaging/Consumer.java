///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.NestedMessaging;

import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmException;


public class Consumer
{
	public static void main(String[] args)
	{
		OmmConsumer consumer = null;
		try
		{
			AppClient appClient = new AppClient();
			
			consumer  = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig().username("user"));
			
			appClient.setOmmConsumer(consumer);
			
			long handle = consumer.registerClient(EmaFactory.createReqMsg().domainType(200).serviceName("DIRECT_FEED")
																			.name("IBM.XYZ")
																			.privateStream(true), appClient, (Integer)1);

			Thread.sleep(60000);			// API calls onRefreshMsg(), onUpdateMsg() and onStatusMsg()
		}
		catch (InterruptedException | OmmException excp)
		{
			System.out.println(excp);
		}
		finally 
		{
			if (consumer != null) consumer.uninitialize();
		}
	}
}
