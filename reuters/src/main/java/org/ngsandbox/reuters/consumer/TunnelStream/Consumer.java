///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.TunnelStream;

import com.thomsonreuters.ema.access.ClassOfService;
import com.thomsonreuters.ema.access.CosAuthentication;
import com.thomsonreuters.ema.access.CosDataIntegrity;
import com.thomsonreuters.ema.access.CosFlowControl;
import com.thomsonreuters.ema.access.CosGuarantee;
import com.thomsonreuters.ema.access.TunnelStreamRequest;
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

			consumer = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig().username("user"));

			appClient.setOmmConsumer(consumer);

			ClassOfService cos = EmaFactory.createClassOfService().
					authentication(EmaFactory.createCosAuthentication().type(CosAuthentication.CosAuthenticationType.OMM_LOGIN))
					.dataIntegrity(EmaFactory.createCosDataIntegrity().type(CosDataIntegrity.CosDataIntegrityType.RELIABLE))
					.flowControl(EmaFactory.createCosFlowControl().type(CosFlowControl.CosFlowControlType.BIDIRECTIONAL).recvWindowSize(1200))
					.guarantee(EmaFactory.createCosGuarantee().type(CosGuarantee.CosGuaranteeType.NONE));

			TunnelStreamRequest tsr = EmaFactory.createTunnelStreamRequest().classOfService(cos)
					.domainType(EmaRdm.MMT_SYSTEM).name("TUNNEL").serviceName("DIRECT_FEED");

			appClient.setTunnelStreamHandle(consumer.registerClient(tsr, appClient));

			Thread.sleep(60000); // API calls onRefreshMsg(), onUpdateMsg() and
									// onStatusMsg()
		} catch (InterruptedException | OmmException excp)
		{
			System.out.println(excp.getMessage());
		} finally
		{
			if (consumer != null)
				consumer.uninitialize();
		}
	}
}
