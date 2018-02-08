///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.Streaming;

import com.thomsonreuters.ema.access.ReqMsg;
import com.thomsonreuters.ema.rdm.EmaRdm;
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

			 long fldHandle = consumer.registerClient(reqMsg.domainType(EmaRdm.MMT_DICTIONARY).name("RWFFld").
					 filter(EmaRdm.DICTIONARY_NORMAL), appClient);

			long enumHandle = consumer.registerClient(reqMsg.clear().domainType(EmaRdm.MMT_DICTIONARY).name("RWFEnum").
					filter(EmaRdm.DICTIONARY_NORMAL),appClient);

			long handle = consumer.registerClient(reqMsg.clear().serviceName("DIRECT_FEED").name("IBM.N"), appClient);

			Thread.sleep(60000); // API calls onRefreshMsg(), onUpdateMsg() and
									// onStatusMsg()
		} catch (InterruptedException | OmmException excp)
		{
			System.out.println(excp.getMessage());
		}
		finally 
		{
			if (consumer != null) consumer.uninitialize();
		}
	}
}
