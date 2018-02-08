///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.MarketPrice.HorizontalScaling;

import com.thomsonreuters.ema.access.OmmException;


public class Consumer
{
	public static void main(String[] args)
	{
		try 
		{
			ConsumerInstance consumer1 = new ConsumerInstance("localhost:14002", "user1");
			ConsumerInstance consumer2 = new ConsumerInstance("localhost:14002", "user2");
			
			consumer1.openItem("IBM.N", "DIRECT_FEED");
			consumer2.openItem("TRI.N",  "DIRECT_FEED");
			
			Thread.sleep(60000);
		}
		catch (InterruptedException | OmmException excp)
		{
			System.out.println(excp);
		}
	}
}
