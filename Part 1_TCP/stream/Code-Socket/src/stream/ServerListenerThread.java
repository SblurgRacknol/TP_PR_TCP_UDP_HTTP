/***
 * ServerListenerThread
 * Waits for broadcasted messages from server and displays them
 */

package stream;

import java.io.*;
import java.net.*;

public class ServerListenerThread extends Thread 
{
	private Socket socket;
	
	ServerListenerThread(Socket s) 
	{
		this.socket = s;
	}

 	/**
  	* receives a message from the server and displays it
  	* @param clientSocket the client socket
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

  
