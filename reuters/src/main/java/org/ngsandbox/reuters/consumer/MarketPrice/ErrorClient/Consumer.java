///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.MarketPrice.ErrorClient;

import com.thomsonreuters.ema.access.Msg;
import com.thomsonreuters.ema.access.AckMsg;
import com.thomsonreuters.ema.access.GenericMsg;
import com.thomsonreuters.ema.access.OmmConsumerErrorClient;
import com.thomsonreuters.ema.access.RefreshMsg;
import com.thomsonreuters.ema.access.StatusMsg;
import com.thomsonreuters.ema.access.UpdateMsg;
import com.thomsonreuters.ema.access.Data;
import com.thomsonreuters.ema.access.DataType;
import com.thomsonreuters.ema.access.DataType.DataTypes;
import com.thomsonreuters.ema.access.OmmConsumerConfig.OperationModel;
import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.FieldEntry;
import com.thomsonreuters.ema.access.FieldList;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmConsumerClient;
import com.thomsonreuters.ema.access.OmmConsumerEvent;


public class Consumer
{
	public static void main(String[] args)
	{
		AppClient appClient = new AppClient();
			
		AppErrorClient appErrorClient = new AppErrorClient();
			
		OmmConsumer consumer = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig()
															.operationModel(OperationModel.USER_DISPATCH)
															.username("user"), appErrorClient);
			
		long invalidHandle = 0;
			
		consumer.reissue(EmaFactory.createReqMsg(), invalidHandle);
			
		consumer.submit(EmaFactory.createGenericMsg(), invalidHandle);
			
		consumer.submit(EmaFactory.createPostMsg(), invalidHandle);
			
		consumer.registerClient(EmaFactory.createReqMsg().serviceName("DIRECT_FEED").name("IBM.N"), appClient);
			
		long startTime = System.currentTimeMillis();
		while (startTime + 60000 > System.currentTimeMillis())
			consumer.dispatch(10);		// calls to onRefreshMsg(), onUpdateMsg(), or onStatusMsg() execute on this thread

		consumer.uninitialize();
	}
}
