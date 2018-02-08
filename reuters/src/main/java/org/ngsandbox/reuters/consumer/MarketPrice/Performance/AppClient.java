package org.ngsandbox.reuters.consumer.MarketPrice.Performance;

import com.thomsonreuters.ema.access.*;

class AppClient implements OmmConsumerClient

{
	long updateCount = 0;
	long refreshCount = 0;
	long statusCount = 0;
	Payload payload;

	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		++refreshCount;

		payload = refreshMsg.payload();
		if (DataType.DataTypes.FIELD_LIST == payload.dataType())
			decode(payload.fieldList());
	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		++updateCount;

		payload = updateMsg.payload();
		if (DataType.DataTypes.FIELD_LIST == payload.dataType())
			decode(payload.fieldList());
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		++statusCount;
	}

	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent){}
	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent){}
	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent){}

	void decode(FieldList fieldList)
	{
		try
		{
			for(FieldEntry fieldEntry : fieldList)
			{
				if (Data.DataCode.NO_CODE == fieldEntry.code())
					switch (fieldEntry.loadType())
					{
						case DataType.DataTypes.REAL :
						{
							OmmReal re = fieldEntry.real();
						}
						break;
						case DataType.DataTypes.DATE :
						{
							OmmDate date = fieldEntry.date();
						}
						break;
						case DataType.DataTypes.TIME :
						{
							OmmTime time = fieldEntry.time();
						}
						break;
						case DataType.DataTypes.DATETIME :
						{
							OmmDateTime dateTime = fieldEntry.dateTime();
						}
						break;
						case DataType.DataTypes.INT :
						{
							long value = fieldEntry.intValue();
						}
						break;
						case DataType.DataTypes.UINT :
						{
							long value = fieldEntry.uintValue();
						}
						break;
						case DataType.DataTypes.FLOAT :
						{
							float value = fieldEntry.floatValue();
						}
						break;
						case DataType.DataTypes.DOUBLE :
						{
							double value = fieldEntry.doubleValue();
						}
						break;
						case DataType.DataTypes.QOS :
						{
							OmmQos value = fieldEntry.qos();
						}
						break;
						case DataType.DataTypes.STATE :
						{
							OmmState value = fieldEntry.state();
						}
						break;
						case DataType.DataTypes.ASCII :
						{
							OmmAscii asciiString = fieldEntry.ascii();
						}
						break;
						case DataType.DataTypes.RMTES :
						{
							OmmRmtes rmtesBuffer = fieldEntry.rmtes();
						}
						break;
						case DataType.DataTypes.UTF8 :
						{
							OmmUtf8 utf8Buffer = fieldEntry.utf8();
						}
						break;
						case DataType.DataTypes.BUFFER :
						{
							OmmBuffer value = fieldEntry.buffer();
						}
						break;
						case DataType.DataTypes.ENUM :
						{
							int value = fieldEntry.enumValue();
						}
						break;
						case DataType.DataTypes.ARRAY :
						{
							OmmArray value = fieldEntry.array();
						}
						break;
						case DataType.DataTypes.ERROR :
						{
							OmmError error = fieldEntry.error();
						}
						break;
						default :
						break;
					}
			}
		}
		catch (OmmException excp)
		{
			System.out.println(excp);
		}
	}
}
