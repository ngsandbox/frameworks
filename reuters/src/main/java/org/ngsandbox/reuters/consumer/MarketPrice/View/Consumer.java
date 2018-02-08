///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.MarketPrice.View;

import com.thomsonreuters.ema.access.OmmArray;
import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmException;
import com.thomsonreuters.ema.access.ElementList;
import com.thomsonreuters.ema.rdm.EmaRdm;


public class Consumer
{
	public static void main(String[] args)
	{
		OmmConsumer consumer = null;
		try
		{
			AppClient appClient = new AppClient();
			
			consumer  = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig().host("localhost:14002").username("user"));
			
			ElementList view = EmaFactory.createElementList();
			OmmArray array = EmaFactory.createOmmArray();
			
			array.fixedWidth(2);
			array.add(EmaFactory.createOmmArrayEntry().intValue(22));
			array.add(EmaFactory.createOmmArrayEntry().intValue(25));

			view.add(EmaFactory.createElementEntry().uintValue(EmaRdm.ENAME_VIEW_TYPE, 1));
			view.add(EmaFactory.createElementEntry().array(EmaRdm.ENAME_VIEW_DATA, array));
			
			consumer.registerClient(EmaFactory.createReqMsg().serviceName("DIRECT_FEED").name("IBM.N").payload(view), appClient);
			
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


