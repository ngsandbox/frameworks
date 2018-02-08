///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2017. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.MarketPrice.AdminDomainConfig.DomainRepresentation;

import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmConsumerConfig;
import com.thomsonreuters.ema.access.OmmException;
import com.thomsonreuters.ema.access.ReqMsg;
import com.thomsonreuters.ema.domain.login.Login.LoginReq;
import com.thomsonreuters.ema.rdm.EmaRdm;


public class Consumer
{
	public static void main(String[] args)
	{
		OmmConsumer consumer = null;
		try
		{
			AppClient appClient = new AppClient();
			
			LoginReq loginReq = EmaFactory.Domain.createLoginReq();
			ReqMsg reqMsg = EmaFactory.createReqMsg();

			consumer = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig().operationModel(OmmConsumerConfig.OperationModel.USER_DISPATCH)
					.addAdminMsg(loginReq.name("user").nameType(EmaRdm.USER_NAME).applicationId("127").position("127.0.0.1/net").allowSuspectData(true).message())
					.addAdminMsg(reqMsg.domainType(EmaRdm.MMT_DIRECTORY).filter(EmaRdm.SERVICE_INFO_FILTER  | EmaRdm.SERVICE_STATE_FILTER | EmaRdm.SERVICE_GROUP_FILTER))
					.addAdminMsg(reqMsg.clear().domainType(EmaRdm.MMT_DICTIONARY).filter(EmaRdm.DICTIONARY_VERBOSE).name("RWFFld").serviceId(1))
					.addAdminMsg(reqMsg.clear().domainType(EmaRdm.MMT_DICTIONARY).filter(EmaRdm.DICTIONARY_VERBOSE).name("RWFEnum").serviceId(1)));
		
			consumer.registerClient(reqMsg.clear().serviceName("DIRECT_FEED").name("IBM.N"), appClient, null);
			
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


