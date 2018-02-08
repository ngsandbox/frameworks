///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.MarketPrice.PauseAll;

import com.thomsonreuters.ema.access.ReqMsg;
import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmException;
import com.thomsonreuters.ema.rdm.EmaRdm;


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

			long loginHandle = consumer.registerClient(reqMsg.domainType( EmaRdm.MMT_LOGIN ), appClient);

			appClient.setOmmConsumer(consumer, loginHandle);
			
			consumer.registerClient(reqMsg.clear().serviceName("DIRECT_FEED").name("IBM.N"), appClient, (Integer)1);
			
			consumer.registerClient(reqMsg.clear().serviceName("DIRECT_FEED").name("TRI.N"), appClient, (Integer)2);

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
