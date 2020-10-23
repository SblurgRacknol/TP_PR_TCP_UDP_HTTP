/***
 * Server
 * TCP multi-threaded server
 * Authors : Lucie Clémenceau and Sylvain de Joannis de Verclos
 * Run with: java -cp classes stream.ServerMultiThreaded 8000
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerMultiThreaded  
{
	/** list of the client sockets*/
	public static ArrayList<Socket> clientSocketList = new ArrayList<Socket>();  
	/** list of the client sockets' output streams*/
	public static ArrayList<PrintStream> clientSocketOutputSocketStreamList = new ArrayList<PrintStream>();  
	/** history of the messages*/
	public static ArrayList<String> messagesHistory = new ArrayList<String>();
	 
 	/**
  	* main method
	* Starts the server on the specified port 
	* Waits for a connection
	* When a client is connected, creates a ClientListenerThread and starts it
	* @param args the server port
        * @author Lucie Clémenceau and Sylvain de Joannis de Verclos 
  	**/
	public static void main(String args[])
	{ 
		ServerSocket listenSocket;
			
		if (args.length != 1) 
		{
			System.out.println("Usage: java ServerMultiThreaded <Server port>");
			System.exit(1);
		}
		try 
		{
			listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
			System.out.println("Server ready..."); 
			while (true) 
			{
				Socket clientSocket = listenSocket.accept();

				clientSocketList.add(clientSocket);

				System.out.println("Connexion from:" + clientSocket.getInetAddress());

				ClientListenerThread ct = new ClientListenerThread(clientSocket);
				ct.start();
			}
		} 
		catch (Exception e) 
		{
			System.err.println("Error in ServerMultiThreaded :" + e);
		}
	}
	
}

  
