/***
 * MulticastListenerThread
 * Authors : Lucie Clémenceau and Sylvain de Joannis de Verclos 
 */

package stream;

import java.io.*;
import java.net.*;

public class MulticastListenerThread extends Thread 
{
	/** the multicastClient's multicast socket*/
	private MulticastSocket multicastClientSocket;
	/**
  	* Constructor of the MulticastListenerThread
	* @param args the multicastClient's multicast socket
        * @author Lucie Clémenceau and Sylvain de Joannis de Verclos 
  	**/
	MulticastListenerThread(MulticastSocket s) 
	{
		this.multicastClientSocket = s;
	}

 	/**
  	* Method run
	* Waits for a message to be sent on the output stream of MulticastSocket
	* When a message is sent, displays it on the standard output
        * @author Lucie Clémenceau and Sylvain de Joannis de Verclos 
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

  
