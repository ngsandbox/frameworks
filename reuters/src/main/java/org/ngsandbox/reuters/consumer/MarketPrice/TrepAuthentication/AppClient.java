package org.ngsandbox.reuters.consumer.MarketPrice.TrepAuthentication;

import com.thomsonreuters.ema.access.*;

class AppClient implements OmmConsumerClient
{
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Refresh Message\n");

		System.out.println( refreshMsg );
		System.out.println();
	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Update Message\n");

		System.out.println( updateMsg );
		System.out.println();
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Status Message\n");

		System.out.println( statusMsg );
		System.out.println();
	}

	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent event) {}
	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent event){}
	public void onAllMsg(Msg msg, OmmConsumerEvent event){}
}
