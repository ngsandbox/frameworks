package org.ngsandbox.reuters.consumer.SwitchEntryAction;

import com.thomsonreuters.ema.access.*;

class AppClient implements OmmConsumerClient
{
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		System.out.println("Item Name: " + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));

		System.out.println("Item State: " + refreshMsg.state());

		if (DataType.DataTypes.MAP == refreshMsg.payload().dataType())
			decode(refreshMsg.payload().map());

		System.out.println();
	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		System.out.println("Item Name: " + (updateMsg.hasName() ? updateMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>"));

		if (DataType.DataTypes.MAP == updateMsg.payload().dataType())
			decode(updateMsg.payload().map());

		System.out.println();
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		System.out.println("Item Name: " + (statusMsg.hasName() ? statusMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>"));

		if (statusMsg.hasState())
			System.out.println("Item State: " +statusMsg.state());

		System.out.println();
	}

	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent){}
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent){}
	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent){}

	void decode(Map map)
	{
		if (DataType.DataTypes.FIELD_LIST == map.summaryData().dataType())
		{
			System.out.println("Map Summary data:");
			decode(map.summaryData().fieldList());
			System.out.println();
		}

		for (MapEntry mapEntry : map)
		{
			if (DataType.DataTypes.BUFFER != mapEntry.key().dataType())
				return;

			int dType = mapEntry.loadType();

			switch (mapEntry.action())		// MapEntry processing based on MapEntry action
			{
				case MapEntry.MapAction.ADD :
				{
					if (dType != DataType.DataTypes.FIELD_LIST)
						return;
					decode(mapEntry.fieldList());
				}
				break;
				case MapEntry.MapAction.DELETE :
				{
					if (dType != DataType.DataTypes.NO_DATA)
						return;
					break;
				}
				case MapEntry.MapAction.UPDATE :
				{
					if (dType != DataType.DataTypes.FIELD_LIST)
						return;
					decode(mapEntry.fieldList());
				}
				break;
			}
		}
	}

	void decode(FieldList fieldList)
	{
		for(FieldEntry fieldEntry : fieldList)
		{
			System.out.print("Fid: " + fieldEntry.fieldId() + " Name = " + fieldEntry.name() + " DataType: " + DataType.asString(fieldEntry.load().dataType()) + " Value: ");

			if (Data.DataCode.BLANK == fieldEntry.code())
				System.out.println(" blank");
			else
				switch (fieldEntry.loadType())
				{
				case DataType.DataTypes.REAL :
					System.out.println(fieldEntry.real().asDouble());
					break;
				case DataType.DataTypes.DATE :
					System.out.println(fieldEntry.date().day() + " / " + fieldEntry.date().month() + " / " + fieldEntry.date().year());
					break;
				case DataType.DataTypes.TIME :
					System.out.println(fieldEntry.time().hour() + ":" + fieldEntry.time().minute() + ":" + fieldEntry.time().second() + ":" + fieldEntry.time().millisecond());
					break;
				case DataType.DataTypes.INT :
					System.out.println(fieldEntry.intValue());
					break;
				case DataType.DataTypes.UINT :
					System.out.println(fieldEntry.uintValue());
					break;
				case DataType.DataTypes.ASCII :
					System.out.println(fieldEntry.ascii());
					break;
				case DataType.DataTypes.ENUM :
					System.out.println(fieldEntry.hasEnumDisplay() ? fieldEntry.enumDisplay() : fieldEntry.enumValue());
					break;
				case DataType.DataTypes.ERROR :
					System.out.println("(" + fieldEntry.error().errorCodeAsString() + ")");
					break;
				default :
					System.out.println();
					break;
				}
		}
	}
}
