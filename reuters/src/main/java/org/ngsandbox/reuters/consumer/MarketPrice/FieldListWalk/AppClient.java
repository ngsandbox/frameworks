package org.ngsandbox.reuters.consumer.MarketPrice.FieldListWalk;

import com.thomsonreuters.ema.access.*;

import java.util.Iterator;

class AppClient implements OmmConsumerClient
{
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		if (refreshMsg.hasName())
			System.out.println("Item Name: " + refreshMsg.name());

		if (refreshMsg.hasServiceName())
			System.out.println("Service Name: " + refreshMsg.serviceName());

		System.out.println("Item State: " + refreshMsg.state());

		if (DataType.DataTypes.FIELD_LIST == refreshMsg.payload().dataType())
			decode(refreshMsg.payload().fieldList());

		System.out.println("\n");
	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		if (updateMsg.hasName())
			System.out.println("Item Name: " + updateMsg.name());

		if (updateMsg.hasServiceName())
			System.out.println("Service Name: " + updateMsg.serviceName());

		if (DataType.DataTypes.FIELD_LIST == updateMsg.payload().dataType())
			decode(updateMsg.payload().fieldList());

		System.out.println("\n");
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		if (statusMsg.hasName())
			System.out.println("Item Name: " + statusMsg.name());

		if (statusMsg.hasServiceName())
			System.out.println("Service Name: " + statusMsg.serviceName());

		if (statusMsg.hasState())
			System.out.println("Service State: " + statusMsg.state());

		System.out.println("\n");
	}


	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent){}
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent){}
	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent){}


	void decode(FieldList fieldList)
	{
		Iterator<FieldEntry> iter = fieldList.iterator();
		FieldEntry fieldEntry;
		while (iter.hasNext())
		{
			fieldEntry = iter.next();
			System.out.println("Fid: " + fieldEntry.fieldId() + " Name: " + fieldEntry.name() + " value: " + fieldEntry.load());
		}
	}
}
