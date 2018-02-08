///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright Thomson Reuters 2017. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

package org.ngsandbox.reuters.consumer.MarketPrice.TrepAuthentication;

import java.nio.ByteBuffer;

import com.thomsonreuters.ema.access.OmmConsumerConfig;
import com.thomsonreuters.ema.access.ReqMsg;
import com.thomsonreuters.ema.domain.login.Login.LoginReq;
import com.thomsonreuters.ema.rdm.EmaRdm;
import com.thomsonreuters.ema.access.EmaFactory;
import com.thomsonreuters.ema.access.OmmConsumer;
import com.thomsonreuters.ema.access.OmmException;


public class Consumer
{
	private static String authenticationToken = "";
	private static String appId = "256";
	private static String authenticationExtended = "";
	
	public static void printHelp()
	{
		
		System.out.println("\nOptions:\n"
		+ "  -?                            Shows this usage\n\n"
		+ "  -authntoken <token>           Authentication token to use in login request [default = \"\"]\n"
		+ "  -authnextended <name>         Extended authentication information to use in login request [default = \"\"]\n"
		+ "  -appid <applicationId>        ApplicationId set as login Attribute [default = 256]\n"
		+ "\n" );
	}
	
	public static void printInvalidOption()
	{
		System.out.println("Detected a missing argument. Please verify command line options [-?]");
	}
	
	public static boolean init(String[] argv)
	{
		int count = argv.length;
		int idx = 0;
		
		while ( idx < count )
		{
			if ( 0 == argv[idx].compareTo("-?") )
			{
				printHelp();
				return false;
			}
			else if ( 0 == argv[idx].compareTo("-aid") )
			{
				if ( ++idx >= count )
				{
					printInvalidOption();
					return false;
				}
				appId = argv[idx];
				++idx;
			}
			else if ( 0 == argv[idx].compareTo("-at") )
			{
				if ( ++idx >= count )
				{
					printInvalidOption();
					return false;
				}
				authenticationToken = argv[idx];
				++idx;
			}
			else if ( 0 == argv[idx].compareTo("-ax") )
			{
				if ( ++idx >= count )
				{
					printInvalidOption();
					return false;
				}
				authenticationExtended = argv[idx];
				++idx;
			}
			else
			{
				System.out.println( "Unrecognized option. Please see command line help. [-?]");
				return false;
			}
		}
		
		return true;
	}
	
	private static void printActiveConfig()
	{
		System.out.println("Following options are selected:");
		
		System.out.println("appId = " + appId);
		System.out.println("Authentication Token = " + authenticationToken);
		System.out.println("Authentication Extended = " + authenticationExtended);
	}
	
	public static void main(String[] args)
	{
		OmmConsumer consumer = null;
		try
		{
			if ( !init(args) ) return;
			AppClient appClient = new AppClient();
			AppLoginClient appLoginClient = new AppLoginClient();
			LoginReq loginReq = EmaFactory.Domain.createLoginReq();
			
			printActiveConfig();
			
			OmmConsumerConfig config = EmaFactory.createOmmConsumerConfig();
			
			loginReq.clear().name(authenticationToken).nameType(EmaRdm.USER_AUTH_TOKEN).applicationId(appId);
			
			if(!authenticationExtended.isEmpty())
			{
				System.out.println("setting authnextended\n");
				loginReq.authenticationExtended(ByteBuffer.wrap(authenticationExtended.getBytes()));
			}
							
			config.addAdminMsg(loginReq.message());
			consumer = EmaFactory.createOmmConsumer(config, appLoginClient);
			
			ReqMsg reqMsg = EmaFactory.createReqMsg();
		
			consumer.registerClient(reqMsg.clear().serviceName("DIRECT_FEED").name("TRI.N"), appClient);

			for(int i = 0; i < 60; i++)
			{						
				if(appLoginClient.ttReissue != 0 && appLoginClient.ttReissue <= (System.currentTimeMillis()/1000))
				{
					loginReq.clear().name(authenticationToken).nameType(EmaRdm.USER_AUTH_TOKEN).applicationId(appId);
					
					if(!authenticationExtended.isEmpty())
						loginReq.authenticationExtended(ByteBuffer.wrap(authenticationExtended.getBytes()));
					
					consumer.reissue(loginReq.message(), appLoginClient.handle);
					appLoginClient.ttReissue = 0;
				}
				
				Thread.sleep(1000);
			}
			
		} 
		catch (InterruptedException | OmmException excp)
		{
			System.out.println(excp.getMessage());
		}
		finally 
		{
			if (consumer != null) consumer.uninitialize();
		}
	}
}
