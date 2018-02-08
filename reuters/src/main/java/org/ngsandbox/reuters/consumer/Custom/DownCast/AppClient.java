package org.ngsandbox.reuters.consumer.Custom.DownCast;

import com.thomsonreuters.ema.access.*;

class AppClient implements OmmConsumerClient
{
	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Refresh. Item Handle: " + event.handle() + " Closure: " + event.closure().hashCode());

		decodeRefreshMsg(refreshMsg);

		System.out.println();
	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Update. Item Handle: " + event.handle() + " Closure: " + event.closure().hashCode());

		decodeUpdateMsg(updateMsg);

		System.out.println();
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Status. Item Handle: " + event.handle() + " Closure: " + event.closure().hashCode());

		decodeStatusMsg(statusMsg);

		System.out.println();
	}

	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent){}
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent){}
	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent){}

	void decodeRefreshMsg(RefreshMsg refreshMsg)
	{
		System.out.println("Item Name: " + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));

		System.out.println("Item State: " + refreshMsg.state());

		System.out.println("Attribute");
		decode(refreshMsg.attrib().data());

		System.out.println("Payload");
		decode(refreshMsg.payload().data());
	}

	void decodeUpdateMsg(UpdateMsg updateMsg)
	{
		System.out.println("Item Name: " + (updateMsg.hasName() ? updateMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>"));

		System.out.println("Attribute");
		decode(updateMsg.attrib().data());

		System.out.println("Payload");
		decode(updateMsg.payload().data());
	}

	void decodeStatusMsg(StatusMsg statusMsg)
	{
		System.out.println("Item Name: " + (statusMsg.hasName() ? statusMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>"));

		if (statusMsg.hasState())
			System.out.println("Item State: " + statusMsg.state());
	}

	void decode(Data data)
	{
		if (Data.DataCode.BLANK == data.code())
			System.out.println("Blank data");
		else
			switch (data.dataType())
			{
			case DataType.DataTypes.REFRESH_MSG :
				decodeRefreshMsg((RefreshMsg)data);
				break;
			case DataType.DataTypes.UPDATE_MSG :
				decodeUpdateMsg((UpdateMsg)data);
				break;
			case DataType.DataTypes.STATUS_MSG :
				decodeStatusMsg((StatusMsg)data);
				break;
			case DataType.DataTypes.FIELD_LIST :
				decodeFieldList((FieldList)data);
				break;
			case DataType.DataTypes.MAP :
				decodeMap((Map)data);
				break;
			case DataType.DataTypes.NO_DATA :
				System.out.println("NoData");
				break;
			case DataType.DataTypes.TIME :
				System.out.println("OmmTime: " + ((OmmTime)data).toString());
				break;
			case DataType.DataTypes.DATE :
				System.out.println("OmmDate: " + ((OmmDate)data).toString());
				break;
			case DataType.DataTypes.REAL :
				System.out.println("OmmReal::asDouble: " + ((OmmReal)data).asDouble());
				break;
			case DataType.DataTypes.INT :
				System.out.println("OmmInt: " + ((OmmInt)data).intValue());
				break;
			case DataType.DataTypes.UINT :
				System.out.println("OmmUInt: " + ((OmmUInt)data).longValue());
				break;
			case DataType.DataTypes.ENUM :
				System.out.println("OmmEnum: " + ((OmmEnum)data).enumValue());
				break;
			case DataType.DataTypes.ASCII :
				System.out.println("OmmAscii: " + ((OmmAscii)data).ascii());
				break;
			case DataType.DataTypes.ERROR :
				System.out.println("Decoding error: " + ((OmmError)data).errorCodeAsString());
				break;
			default :
				break;
			}
	}

	void decodeMap(Map map)
	{
		System.out.println("Map Summary");
		decode(map.summaryData().data());

		for (MapEntry mapEntry : map)
		{
			System.out.println("Action = " + mapEntry.mapActionAsString());

			System.out.println("Key");
			decode(mapEntry.key().data());

			System.out.println("Load");
			decode(mapEntry.load());
		}
	}

	void decodeFieldList(FieldList fieldList)
	{
		if (fieldList.hasInfo())
			System.out.println("FieldListNum: " + fieldList.infoFieldListNum() + " DictionaryId: " + fieldList.infoDictionaryId());

		for (FieldEntry fieldEntry : fieldList)
		{
			System.out.println("Load");
			decode(fieldEntry.load());
		}
	}
}
