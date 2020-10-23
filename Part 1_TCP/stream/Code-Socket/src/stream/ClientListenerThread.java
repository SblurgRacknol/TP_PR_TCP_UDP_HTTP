/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;

public class ClientListenerThread extends Thread 
{
	private Socket clientSocket;
	
	ClientListenerThread(Socket s) 
	{
		this.clientSocket = s;
	}

 	/**
  	* receives a message from the client it is linked to then sends it to all the clients
  	* @param clientSocket the client socket
  	**/
	public void run() 
	{
		try 
		{
			BufferedReader socIn = null;
			PrintStream socOut = null;

    		socIn = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));    
			socOut = new PrintStream(clientSocket.getOutputStream());

			ServerMultiThreaded.clientSocketOutputSocketStreamList.add(socOut);
			
			//send message history
			for(String message : ServerMultiThreaded.messagesHistory)
			{
				socOut.println(message);
			}

			while (true) 
			{
				//waiting for messages from the client socket 
				String line = socIn.readLine();
				ServerMultiThreaded.messagesHistory.add(line);

				//send received message to all the clients 
				for (PrintStream socOutElment : ServerMultiThreaded.clientSocketOutputSocketStreamList) 
				{
					socOutElment.println(line);
				}
    		}
		} 
		catch (Exception e) 
		{
        	System.err.println("Error in ClientListenerThread:" + e); 
        }
    }
  
}

  
