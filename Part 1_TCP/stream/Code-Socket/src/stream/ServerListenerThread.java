/***
 * ServerListenerThread
 * Authors : Lucie Clémenceau and Sylvain de Joannis de Verclos
 * Waits for broadcasted messages from server and displays them
 */

package stream;

import java.io.*;
import java.net.*;

public class ServerListenerThread extends Thread 
{
	/** socket of the client*/
	private Socket socket;

	/**
  	* Constructor of the ServerListenerThread
  	* @param s the client socket
  	**/
	ServerListenerThread(Socket s) 
	{
		this.socket = s;
	}

 	/**
  	* Method run
	* waits for a message from any of the ClientListenerThreads
	* When one of them broadcasts a message, displays it
	* @author Lucie Clémenceau and Sylvain de Joannis de Verclos
  	**/
	public void run() 
	{
		try 
		{
            		BufferedReader socIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));    
			String messageReceived = null;
			String id = "from " + this.socket.getLocalAddress() + " on port " + this.socket.getLocalPort();
			
			while (true) 
			{
				if(socIn.ready())
				{
					messageReceived = socIn.readLine();
					
					if(!messageReceived.startsWith(id))
					{
						System.out.println(messageReceived);
					}
				}
	    		}
		} 
		catch (Exception e) 
		{
        		System.err.println("Error in ServerListenerThread:" + e); 
        	}
	}
  
}

  
