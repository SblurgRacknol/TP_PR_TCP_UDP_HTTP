/***
 * MulticastListenerThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;

public class MulticastListenerThread extends Thread 
{
	private MulticastSocket multicastClientSocket;
	
	MulticastListenerThread(MulticastSocket s) 
	{
		this.multicastClientSocket = s;
	}

 	/**
  	* Wait and receive messages from clients
  	* @param clientSocket the client socket
  	**/
	public void run() 
	{
		try 
		{
			String messageReceived;
			byte[] buf = new byte[1000]; 
			DatagramPacket datagramPacketToReceive = new DatagramPacket(buf, buf.length);

			while (true) 
			{
				
				multicastClientSocket.receive(datagramPacketToReceive);
				messageReceived = new String(datagramPacketToReceive.getData(), 0, datagramPacketToReceive.getLength());
				System.out.println("from " + datagramPacketToReceive.getSocketAddress()+ " : " + messageReceived);
			}
		} 
		catch (Exception e) 
		{
        	System.err.println("Error in multicastClientSocket:" + e); 
		}
	}
}

  
