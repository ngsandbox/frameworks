package org.ngsandbox.reuters.consumer.Streaming;

import com.thomsonreuters.ema.access.*;
import com.thomsonreuters.ema.rdm.DataDictionary;
import com.thomsonreuters.ema.rdm.EmaRdm;

class AppClient implements OmmConsumerClient
{
	private DataDictionary dataDictionary = EmaFactory.createDataDictionary();
	private boolean fldDictComplete = false;
	private boolean enumTypeComplete = false;

	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Refresh. Item Handle: " + event.handle() + " Closure: " + event.closure());

		System.out.println("Item Name: " + (refreshMsg.hasName() ? refreshMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (refreshMsg.hasServiceName() ? refreshMsg.serviceName() : "<not set>"));

		System.out.println("Item State: " + refreshMsg.state());

		decode(refreshMsg, refreshMsg.complete());

		System.out.println();
	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Update. Item Handle: " + event.handle() + " Closure: " + event.closure());

		System.out.println("Item Name: " + (updateMsg.hasName() ? updateMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (updateMsg.hasServiceName() ? updateMsg.serviceName() : "<not set>"));

		decode(updateMsg, false);

		System.out.println();
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Status. Item Handle: " + event.handle() + " Closure: " + event.closure());

		System.out.println("Item Name: " + (statusMsg.hasName() ? statusMsg.name() : "<not set>"));
		System.out.println("Service Name: " + (statusMsg.hasServiceName() ? statusMsg.serviceName() : "<not set>"));

		if (statusMsg.hasState())
			System.out.println("Item State: " + statusMsg.state());

		System.out.println();
	}

	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent event){}
	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent event){}
	public void onAllMsg(Msg msg, OmmConsumerEvent event){}

	void decode(Msg msg, boolean complete)
	{
		switch (msg.payload().dataType())
		{
		case DataType.DataTypes.SERIES:

			if ( msg.name().equals("RWFFld") )
			{
				dataDictionary.decodeFieldDictionary(msg.payload().series(), EmaRdm.DICTIONARY_NORMAL);

				if ( complete )
				{
					fldDictComplete = true;
				}
			}
			else if ( msg.name().equals("RWFEnum") )
			{
				dataDictionary.decodeEnumTypeDictionary(msg.payload().series(), EmaRdm.DICTIONARY_NORMAL);

				if ( complete )
				{
					enumTypeComplete = true;
				}
			}

			if ( fldDictComplete && enumTypeComplete )
			{
				System.out.println(dataDictionary);
			}

			break;
		case DataType.DataTypes.FIELD_LIST:
			decode(msg.payload().fieldList());
			break;
		}
	}

	void decode(FieldList fieldList)
	{
		for (FieldEntry fieldEntry : fieldList)
		{
			System.out.print("Fid: " + fieldEntry.fieldId() + " Name = " + fieldEntry.name() + " DataType: "
					+ DataType.asString(fieldEntry.load().dataType()) + " Value: ");

			if (Data.DataCode.BLANK == fieldEntry.code())
				System.out.println(" blank");
			else
				switch (fieldEntry.loadType())
				{
				case DataType.DataTypes.REAL:
					System.out.println(fieldEntry.real().asDouble());
					break;
				case DataType.DataTypes.DATE:
					System.out.println(fieldEntry.date().day() + " / " + fieldEntry.date().month() + " / "
							+ fieldEntry.date().year());
					break;
				case DataType.DataTypes.TIME:
					System.out.println(fieldEntry.time().hour() + ":" + fieldEntry.time().minute() + ":"
							+ fieldEntry.time().second() + ":" + fieldEntry.time().millisecond());
					break;
				case DataType.DataTypes.INT:
					System.out.println(fieldEntry.intValue());
					break;
				case DataType.DataTypes.UINT:
					System.out.println(fieldEntry.uintValue());
					break;
				case DataType.DataTypes.ASCII:
					System.out.println(fieldEntry.ascii());
					break;
				case DataType.DataTypes.ENUM:
					System.out.println(fieldEntry.hasEnumDisplay() ? fieldEntry.enumDisplay() : fieldEntry.enumValue());
					break;
				case DataType.DataTypes.RMTES:
					System.out.println(fieldEntry.rmtes().toString());
					break;
				case DataType.DataTypes.ERROR:
					System.out.println(
							fieldEntry.error().errorCode() + " (" + fieldEntry.error().errorCodeAsString() + ")");
					break;
				default:
					System.out.println();
					break;
				}
		}
	}
}
