package org.ngsandbox.reuters.consumer.Custom.Decode;

import com.thomsonreuters.ema.access.*;

class AppClient implements OmmConsumerClient
{
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		System.out.println("Item Handle: " + event.handle() + " Closure: " + event.closure().hashCode());

		decode(refreshMsg);

		System.out.println();
	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		System.out.println("Item Handle: " + event.handle() + " Closure: " + event.closure().hashCode());

		decode(updateMsg);

		System.out.println();
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		System.out.println("Item Name: " + (statusMsg.hasName() ? statusMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>"));

		System.out.println("Item Handle: " + event.handle() + " Closure: " + event.closure().hashCode());

		if (statusMsg.hasState())
			System.out.println("Item State: " +statusMsg.state());

		System.out.println();
	}

	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent){}
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent){}
	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent){}

	void decode(RefreshMsg refreshMsg)
	{
		System.out.println("Item Name: " + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));

		System.out.println("Item State: " + refreshMsg.state());

		decode(refreshMsg.attrib());

		decode(refreshMsg.payload());
	}

	void decode(UpdateMsg updateMsg)
	{
		System.out.println("Item Name: " + (updateMsg.hasName() ? updateMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>"));

		decode(updateMsg.attrib());

		decode(updateMsg.payload());
	}

	void decode(Attrib attrib)
	{
		System.out.println("Attribute");

		switch (attrib.dataType())
		{
		case DataType.DataTypes.FIELD_LIST :
			decode(attrib.fieldList());
			break;
		case DataType.DataTypes.MAP :
			decode(attrib.map());
			break;
		}
	}

	void decode(Payload payload)
	{
		System.out.println("Payload");

		switch (payload.dataType())
		{
		case DataType.DataTypes.FIELD_LIST :
			decode(payload.fieldList());
			break;
		case DataType.DataTypes.MAP :
			decode(payload.map());
			break;
		case DataType.DataTypes.REFRESH_MSG :
			decode(payload.refreshMsg());
			break;
		case DataType.DataTypes.UPDATE_MSG :
			decode(payload.updateMsg());
			break;
		}
	}

	void decode(Map map)
	{
		switch (map.summaryData().dataType())
		{
		case DataType.DataTypes.FIELD_LIST :
			decode(map.summaryData().fieldList());
			break;
		case DataType.DataTypes.MAP :
			decode(map.summaryData().map());
			break;
		case DataType.DataTypes.REFRESH_MSG :
			decode(map.summaryData().refreshMsg());
			break;
		case DataType.DataTypes.UPDATE_MSG :
			decode(map.summaryData().updateMsg());
			break;
		}

		for (MapEntry mapEntry : map)
		{
			switch (mapEntry.key().dataType())
			{
			case DataType.DataTypes.ASCII :
				System.out.println("Action = " + mapEntry.mapActionAsString() + ", key = " + mapEntry.key().ascii());
				break;
			case DataType.DataTypes.BUFFER :
				System.out.println("Action = " + mapEntry.mapActionAsString() + ", key = " + mapEntry.key().buffer());
				break;
			}

			switch (mapEntry.loadType())
			{
			case DataType.DataTypes.FIELD_LIST :
				decode(mapEntry.fieldList());
				break;
			case DataType.DataTypes.MAP :
				decode(mapEntry.map());
				break;
			case DataType.DataTypes.REFRESH_MSG :
				decode(mapEntry.refreshMsg());
				break;
			case DataType.DataTypes.UPDATE_MSG :
				decode(mapEntry.updateMsg());
				break;
			}
		}
	}

	void decode(FieldList fieldList)
	{
		for (FieldEntry fieldEntry : fieldList)
		{
			System.out.print("Fid: " + fieldEntry.fieldId() + " Name = " + fieldEntry.name() + " DataType: " + DataType.asString(fieldEntry.load().dataType()) + " Value: ");

			if (Data.DataCode.BLANK == fieldEntry.code())
				System.out.println(" blank");
			else
				switch (fieldEntry.loadType())
				{
				case DataType.DataTypes.FIELD_LIST :
					System.out.println(",  contains FieldList.");
					decode(fieldEntry.fieldList());
					break;
				case DataType.DataTypes.MAP :
					System.out.println(",  contains map.");
					decode(fieldEntry.map());
					break;
				case DataType.DataTypes.REFRESH_MSG :
					System.out.println(",  contains refresh message.");
					decode(fieldEntry.refreshMsg());
					break;
				case DataType.DataTypes.UPDATE_MSG :
					System.out.println(",  contains update message.");
					decode(fieldEntry.updateMsg());
					break;
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
					System.out.println(fieldEntry.error().errorCode() + " (" + fieldEntry.error().errorCodeAsString() + ")");
					break;
				default :
					System.out.println();
					break;
				}
		}
	}
}
