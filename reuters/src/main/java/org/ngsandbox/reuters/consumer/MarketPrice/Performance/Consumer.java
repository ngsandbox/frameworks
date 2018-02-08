///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.MarketPrice.Performance;

import com.thomsonreuters.ema.access.ReqMsg;
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
			
			consumer = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig().host("localhost:14002").username("user"));
			
			ReqMsg reqMsg = EmaFactory.createReqMsg();
			
			String itemPreName = "RTR";
			StringBuilder itemName = new StringBuilder();
			for (int idx = 0; idx < 1000; ++idx)
			{
				itemName.append(itemPreName).append(idx).append(".N");
				reqMsg.clear().serviceName("DIRECT_FEED").name(itemName.toString());
				consumer.registerClient(reqMsg, appClient);
				itemName.setLength(0);
			}

			StringBuilder display = new StringBuilder();
			for (int idx = 0; idx < 300; ++idx)
			{
				Thread.sleep(1000);
				
				display.append("total refresh count: " ).append(appClient.refreshCount).append("\ttotal status count: ").append(appClient.statusCount)
							.append("\tupdate rate (per sec): ").append(appClient.updateCount);
				System.out.println(display.toString());
				
				display.setLength(0);
				appClient.updateCount = 0;
			}
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
