package org.ngsandbox.reuters.consumer.MarketPrice.Rmtes;

import com.thomsonreuters.ema.access.*;

class AppClient implements OmmConsumerClient
{
	private RmtesBuffer rmtesBuffer = EmaFactory.createRmtesBuffer();

	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Refresh. Item Handle: " + event.handle() + " Closure: " + event.closure());

		System.out.println("Item Name: " + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));

		System.out.println("Item State: " + refreshMsg.state());

		if (DataType.DataTypes.FIELD_LIST == refreshMsg.payload().dataType())
			decode(refreshMsg.payload().fieldList());

		System.out.println();
	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Update. Item Handle: " + event.handle() + " Closure: " + event.closure());

		System.out.println("Item Name: " + (updateMsg.hasName() ? updateMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>"));

		if (DataType.DataTypes.FIELD_LIST == updateMsg.payload().dataType())
			decode(updateMsg.payload().fieldList());

		System.out.println();
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Status. Item Handle: " + event.handle() + " Closure: " + event.closure());

		System.out.println("Item Name: " + (statusMsg.hasName() ? statusMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>"));

		if (statusMsg.hasState())
			System.out.println("Item State: " +statusMsg.state());

		System.out.println();
	}

	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent){}
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent){}
	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent){}

	void decode(FieldList fieldList)
	{
		for (FieldEntry fieldEntry : fieldList)
		{
			if (fieldEntry.name().equals("BCAST_TEXT"))
			{
				System.out.print("Fid: " + fieldEntry.fieldId() + " Name = " + fieldEntry.name() + " DataType: " + DataType.asString(fieldEntry.load().dataType()) + " Value: ");
				System.out.println((rmtesBuffer.apply(fieldEntry.rmtes())).toString());
			}
		}
	}
}
