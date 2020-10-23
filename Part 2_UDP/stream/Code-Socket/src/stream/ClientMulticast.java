/***
 * ClientMulticast
 * UDP client
 * Authors : Lucie Clémenceau and Sylvain de Joannis de Verclos 
 * run: java -cp ./ stream.ClientMulticast 228.5.6.7 6789 
 */
package stream;

import java.io.*;
import java.net.*;
import java.lang.Integer; 

public class ClientMulticast {

 
    /**
    * main method
    * creates a multicast socket on the group port and joins it to the group on the specified address
    * creates a MulticastListenerThread
    * Waits for a message to be entered on the keyboard
    * When a message is entered, sends it to the group with the multicast socket
    * @param args the INET Address and the group port
    * @author Lucie Clémenceau and Sylvain de Joannis de Verclos 
    **/
    public static void main(String[] args) throws IOException {

      if(args.length != 2)
      {
        System.out.println("Usage: java EchoClient missing args");
        System.exit(1);
      }

      InetAddress groupAddr = InetAddress.getByName(args[0]);
      MulticastSocket multicastClientSocket= null;
      int groupPort = Integer.parseInt(args[1]);
      BufferedReader stdIn = null;

      try 
      {
          //try to join the group
          multicastClientSocket = new MulticastSocket(groupPort);
          multicastClientSocket.joinGroup(groupAddr);

          stdIn = new BufferedReader(new InputStreamReader(System.in));
      } 
      catch (Exception e) 
      {
          System.err.println("Cannot join the group with InetAdress " + args[0] + "and group port: " + args[1]);
          System.exit(1);
      } 

      try
      {
        //send a message
        String messageToSend;
        MulticastListenerThread mlt = new MulticastListenerThread(multicastClientSocket);
        mlt.start();

        while (true) 
        {
          messageToSend = stdIn.readLine();
          if (messageToSend.equals(".")) 
          {
            mlt.stop();
            break;
          }
          
          DatagramPacket datagramPacketToSend = new DatagramPacket(messageToSend.getBytes(), messageToSend.length(), groupAddr, groupPort);
          multicastClientSocket.send(datagramPacketToSend);
        }
      }
      catch(Exception e)
      {
        System.err.println("ClientMulticast error");
          System.exit(1);
      }
      
      stdIn.close();
      multicastClientSocket.leaveGroup(groupAddr);
      multicastClientSocket.close();
    }
}


