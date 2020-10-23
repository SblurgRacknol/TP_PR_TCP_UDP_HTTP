/***
 * Server
 * TCP multi-threaded server
 * Run with: java -cp classes stream.ServerMultiThreaded 8000
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerMultiThreaded  
{
	
	public static ArrayList<Socket> clientSocketList = new ArrayList<Socket>();  
	public static ArrayList<PrintStream> clientSocketOutputSocketStreamList = new ArrayList<PrintStream>();  
	public static ArrayList<String> messagesHistory = new ArrayList<String>();
	 
 	/**
  	* main method
	* @param server port
  	* 
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

  
