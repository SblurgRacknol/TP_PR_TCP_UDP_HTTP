/***
 * Client
 * A TCP client 
 * Authors : Lucie Clémenceau and Sylvain de Joannis de Verclos
 * java -cp ./ stream.Client localhost 8000
 */
package stream;

import java.io.*;
import java.net.*;

public class Client { 
  /**
  * main method
  * Tries to connect to the ServerMultiThreaded on the specified port
  * When connected, creates a ServerListenerThread and starts it
  * Waits for an input from the keyboard
  * When something is entered on the keyboard, sends it to the ClientListenerThread
  * @author Lucie Clémenceau and Sylvain de Joannis de Verclos
  * @param args the server address and port
  * @version 1.0
  */
    public static void main(String[] args) throws IOException {

        Socket socket = null;
        PrintStream socOut = null;
        BufferedReader stdIn = null;
        BufferedReader socIn = null;

        if (args.length != 2) {
          System.out.println("Usage: java Client <Server host> <Server port>");
          System.exit(1);
        }

        try 
        {
      	    // socket creation ===> connection
            socket = new Socket(args[0],new Integer(args[1]).intValue());
            ServerListenerThread slt = new ServerListenerThread(socket);
            slt.start(); 
            socOut= new PrintStream(socket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } 
        catch (UnknownHostException e) 
        {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } 
        catch (IOException e) 
        {
            System.err.println("Couldn't get I/O for " + "the connection to:"+ args[0]);
            System.exit(1);
        }
                             
        String line;
        while (true) 
        {
          //read keyboard
          line=stdIn.readLine();
          if (line.equals(".")) 
          {
            break;
          }
          //write in socket
          socOut.println("from " + socket.getLocalAddress() +
					" on port " + socket.getLocalPort() + ": " +line);         
        }

      socOut.close();
      stdIn.close();
      socket.close();
    }
}


