package org.ngsandbox.reuters.consumer.MarketPrice.TrepAuthentication;

import com.thomsonreuters.ema.access.*;
import com.thomsonreuters.ema.domain.login.Login;

class AppLoginClient implements OmmConsumerClient
{
	public long handle = 0;
	public long ttReissue = 0;

	public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Login Refresh Message\n");

		System.out.println( refreshMsg );
		System.out.println();

		/* Get the handle from the event and save it for a future reissue */
		handle = event.handle();
		/* Get the time to reissue from the refresh and save it */
		Login.LoginRefresh loginRefresh = EmaFactory.Domain.createLoginRefresh().message(refreshMsg);

		if(loginRefresh.hasAuthenticationTTReissue())
			ttReissue = loginRefresh.authenticationTTReissue();

	}

	public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Login Update Message\n");

		System.out.println( updateMsg );
		System.out.println();
	}

	public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
	{
		System.out.println("Received Login Status Message\n");

		System.out.println( statusMsg );
		System.out.println();
	}

	public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent event) {}
	public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent event){}
	public void onAllMsg(Msg msg, OmmConsumerEvent event){}
}
