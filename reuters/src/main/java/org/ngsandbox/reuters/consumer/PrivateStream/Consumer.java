///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.PrivateStream;

import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmConsumerConfig.OperationModel;
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
			
			consumer = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig()
													.operationModel(OperationModel.USER_DISPATCH)
													.host("localhost:14002").username("user"));
			
			consumer.registerClient(EmaFactory.createReqMsg().domainType(EmaRdm.MMT_MARKET_BY_PRICE)
															.serviceName("DIRECT_FEED").name("BBH.ITC")
															.privateStream(true), appClient);
			
			long startTime = System.currentTimeMillis();
			while (startTime + 60000 > System.currentTimeMillis())
				consumer.dispatch(10);		// calls to onRefreshMsg(), onUpdateMsg(), or onStatusMsg() execute on this thread
		}
		catch (OmmException excp)
		{
			System.out.println(excp);
		}
		finally 
		{
			if (consumer != null) consumer.uninitialize();
		}
	}
}
