package org.ngsandbox.reuters.consumer.TunnelStream;

import com.thomsonreuters.ema.access.*;

class AppClient implements OmmConsumerClient
{
	private OmmConsumer _ommConsumer;
	private long _tunnelStreamHandle;
	private boolean _subItemOpen;

	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		System.out.println("Handle: " + event.handle());
		System.out.println("Parent Handle: " + event.parentHandle());
		System.out.println("Closure: " + event.closure());

		System.out.println(refreshMsg);

		System.out.println();
	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		System.out.println("Handle: " + event.handle());
		System.out.println("Parent Handle: " + event.parentHandle());
		System.out.println("Closure: " + event.closure());

		System.out.println(updateMsg);

		System.out.println();
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		System.out.println("Handle: " + event.handle());
		System.out.println("Parent Handle: " + event.parentHandle());
		System.out.println("Closure: " + event.closure());

		System.out.println(statusMsg);

		if (!_subItemOpen && event.handle() == _tunnelStreamHandle && statusMsg.hasState()
				&& statusMsg.state().streamState() == OmmState.StreamState.OPEN)
		{
			_subItemOpen = true;

			_ommConsumer.registerClient(EmaFactory.createReqMsg().name("TUNNEL_IBM").serviceId(1), this, 1,
					_tunnelStreamHandle);
		}

		System.out.println();
	}

	public void setOmmConsumer(OmmConsumer ommConsumer)
	{
		_ommConsumer = ommConsumer;
	}

	public void setTunnelStreamHandle(long tunnelStreamHandle)
	{
		_tunnelStreamHandle = tunnelStreamHandle;
	}

	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent)
	{
	}

	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent)
	{
	}

	public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent)
	{
	}
}
