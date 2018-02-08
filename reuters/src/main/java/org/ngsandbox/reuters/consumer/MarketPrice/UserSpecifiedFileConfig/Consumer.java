///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.MarketPrice.UserSpecifiedFileConfig;


import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmException;

public class Consumer
{
	public static void main(String[] args)
	{
		String configFile = "";
		if ( args.length == 1 )
			configFile = args[0];
		
		OmmConsumer consumer = null;
		try
		{
			AppClient appClient = new AppClient();
			
			consumer  = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig(configFile).consumerName("Consumer_2"));
			
			consumer.registerClient( EmaFactory.createReqMsg().serviceName("DIRECT_FEED").name("IBM.N"), appClient, 0);
			
			Thread.sleep(60000);			// API calls onRefreshMsg(), onUpdateMsg() and onStatusMsg()
		}
		catch (InterruptedException | OmmException excp)
		{
			System.out.println(excp.getMessage());
		}
		finally 
		{
			if (consumer != null) consumer.uninitialize();
		}
	}
}


