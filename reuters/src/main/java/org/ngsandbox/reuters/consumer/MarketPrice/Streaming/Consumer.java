///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2016. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.MarketPrice.Streaming;

import com.thomsonreuters.ema.access.*;

public class Consumer {
    public static void main(String[] args) {
        OmmConsumer consumer = null;
        try {
            AppClient appClient = new AppClient();

            OmmConsumerConfig config = EmaFactory.createOmmConsumerConfig();

            consumer = EmaFactory.createOmmConsumer(config.host("localhost:14002").username("user"));

            ReqMsg reqMsg = EmaFactory.createReqMsg();

            consumer.registerClient(reqMsg.serviceName("DIRECT_FEED").name("IBM.N"), appClient);

            Thread.sleep(60000);            // API calls onRefreshMsg(), onUpdateMsg() and onStatusMsg()
        } catch (InterruptedException | OmmException excp) {
            System.out.println(excp.getMessage());
        } finally {
            if (consumer != null) consumer.uninitialize();
        }
    }
}




