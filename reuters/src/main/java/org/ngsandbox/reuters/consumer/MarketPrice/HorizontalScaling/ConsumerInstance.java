package org.ngsandbox.reuters.consumer.MarketPrice.HorizontalScaling;

import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.ReqMsg;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class ConsumerInstance implements Runnable
{
	AppClient _appClient;
	OmmConsumer _consumer;
	ReqMsg _reqMsg;
	ExecutorService _executor;

	public ConsumerInstance(String host, String username)
	{
		_appClient = new AppClient();
		_reqMsg = EmaFactory.createReqMsg();

		_consumer = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig().host(host).username(username));

		_executor = Executors.newSingleThreadExecutor();

		_executor.execute(this);
	}

	public void openItem(String item, String serviceName)
	{
		_reqMsg.clear().name(item).serviceName(serviceName);

		_consumer.registerClient(_reqMsg, _appClient);
	}

	public void run()
	{
		try
		{
			Thread.sleep(60000);

			_executor.shutdown();
			_consumer.uninitialize();
			_executor.awaitTermination(5, TimeUnit.SECONDS);
		}
		catch (InterruptedException excp)
		{
			System.out.println(excp.getMessage());
		}
	}
}
