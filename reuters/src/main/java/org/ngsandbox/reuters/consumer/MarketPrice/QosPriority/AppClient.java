package org.ngsandbox.reuters.consumer.MarketPrice.QosPriority;

import com.thomsonreuters.ema.access.*;

class AppClient implements OmmConsumerClient
{
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		System.out.println(refreshMsg);
	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		System.out.println(updateMsg);
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		System.out.println(statusMsg);
	}

	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent){}
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent){}
	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent){}
}
