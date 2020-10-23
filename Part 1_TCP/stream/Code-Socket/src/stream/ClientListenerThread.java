/***
 * ClientThread
 * Authors : Lucie Clémenceau and Sylvain de Joannis de Verclos
 */

package stream;

import java.io.*;
import java.net.*;


public class ClientListenerThread extends Thread 
{
	/** socket of the server*/
	private Socket clientSocket;

	/**
  	* Constructor of the ClientListenerThread
  	* @param s the server socket
  	**/
	ClientListenerThread(Socket s) 
	{
		this.clientSocket = s;
	}

 	/**
  	* Method run
	* waits for a message from the client it's linked to 
	* When the client sends a message, broadcasts it to all the ServerListenerThreads
	* @author Lucie Clémenceau and Sylvain de Joannis de Verclos
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

  
