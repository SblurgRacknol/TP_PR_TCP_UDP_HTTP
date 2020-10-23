///A Web Server (WebServer.java)
//java -cp ./ http.server.WebServer

package http.server;

import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileWriter;

/**
 * WebServer allowing to use different requests : GET, DELETE, PUT
 * @author Sylvain de Joannis de Verclos and Lucie Clémenceau
 * @version 3.0
 */
public class WebServer {
  /** List of the types of ressources the server can handle*/
  public ArrayList<String> supportedTypes = new ArrayList();
  /** File path of the folder where we put the ressources - to be changed !! */
  public String docPath = "/home/lucie/Documents/Reseaux/PR_TP_chat_system/TP-HTTP-Code /TP-HTTP-Code/doc";
  /** File path of the error 404 page*/
  public File error404File = new File(docPath + "Error404.html");
  
  
  /**
   * WebServer constructor.
   * Starts the server on the port 3000
   * Waits for a connection
   * When a client is connected, waits for requests and responds 
   * Returns a header with the code 400 Bad Request if the request' syntex is incorrect
   */
  protected void start() 
  {

    ServerSocket s;

    System.out.println("Webserver starting up on port 3000");
    System.out.println("(press ctrl-c to exit)");
    
    try 
    {
      // create the main server socket
      s = new ServerSocket(3000);
    } 
    catch (Exception e) 
    {
      System.out.println("Error: " + e);
      return;
    }

    //initialisation
    supportedTypes.add("text/html");

    System.out.println("Waiting for connection");
    for (;;) 
    {
      try 
      {
        // wait for a connection
        Socket remote = s.accept();
        // remote is now the connected socket
        System.out.println("Connection, sending data.");
        BufferedReader in = new BufferedReader(new InputStreamReader(remote.getInputStream()));
        PrintWriter out = new PrintWriter(remote.getOutputStream());
                
        
	// read the HTTP request's head.
        // stop reading once a blank line is hit. This
        // blank line signals the end of the client HTTP
        // headers.
        
        String str = "";
        String dataType = "";        
	boolean end = false;
	String header="";
	String body="";
        while (!end)
        {
        	str = in.readLine();
        	if (str.equals(""))
        	{
        		end = true;
        	}
        	else {
        		header+=str;
        	}
        	if(str.startsWith("Accept:"))
        	{
        		dataType = str.substring(8,str.length()).split(",")[0];
        	}
        }
        // Here we consider the HTTP syntax to be respected
        String requestType = header.split(" ")[0];
        String resourceName = header.split(" ")[1];
        if (requestType.equals("GET"))
        {
        	get(out, resourceName, dataType);
        }
        else if (requestType.equals("DELETE"))
        {
        	delete(out, resourceName);
        }
        else if (requestType.equals("PUT"))
        {
        	//read the body of the request
        	end = false ;
        	str="";
		while (!end)
		{
			str = in.readLine();
			if (str.equals(""))
			{
				end = true;
			}
			else 
			{
				body+=str+"\n";
			
			}
		}
		put(in, out, resourceName, body); 
        }
        else if (requestType.equals("HEAD"))
        {
        	head(out, resourceName, dataType);
        }
        else
        {
        	System.out.println ("Error : can not read request");
        	
  		out.println("HTTP /1.0 400 Bad Request");
  		out.println("Content-Type: text/html");
  		out.println("Server: Bot");
  		out.println("");
        }
        out.flush();
        remote.close();
      } 
      catch (Exception e) 
      {
        System.out.println("Error: " + e);
      }
    }
  }

  /**
   * Start the application.
   * 
   * @param args
   * Command line parameters are not used.
   */
  public static void main(String args[]) {
    WebServer ws = new WebServer();
    ws.start();
  }
  /**
    * Method isSupported
    * Tests if a resource type is supported by the server's implemented requests
    * Returns true if the type can be found amongst the supported types in the ArrayList supportedTypes
    * @param type the type we want to test
    * @return a boolean telling if the type is supported
    */

  public boolean isTypeSupported(String type)
  {
    for(String t : supportedTypes)
    {
      if(t.startsWith(type))
      {
        return true;
      }
    }
    return false;
  }
  
  /**
    * Method get
    * Tries to open a file and send it to the client
    * Returns a header with the code 200 OK if the file was found and successfully sent to the client
    * Returns a header with the code 404 Not Found if the file was not found
    * Returns a header with the code 406 Not Acceptable if the resource's type is not supported
    * @param out stream allowing the server to send information to the client who sent the request
    * @param resourceName name of the resource the client asked for
    * @param dataType type of the resource the client asked for
    */
  private void get (PrintWriter out, String resourceName, String dataType)
  {
  	try
  	{
  		resourceName = docPath + resourceName;
  		Scanner reader = new Scanner(resourceName);
  		File fileToRead = error404File;
  		if (isTypeSupported(dataType))
  		{
  			fileToRead = new File (resourceName);
  			reader = new Scanner (fileToRead);
  			if (fileToRead.exists())
  			{
  				//Send the header
  				out.println("HTTP/1.0 200 OK");
  				out.println("Content-Type: " + dataType);
  				out.println("Server: Bot");
  				out.println("");
  				
  				//Send the HTML page
  				while (reader.hasNextLine())
  				{
  					out.println(reader.nextLine());
  				}
  			}
  			else 
  			{
  				//Send the header
  				out.println("HTTP/1.0 404 Not Found");
  				out.println("Content-Type: text/html");
  				out.println("Server: Bot");
  				out.println("");
  			}
  		}
  		else
  		{
  			System.out.println("type : " + dataType + " not supported.");
  			out.println("HTTP/1.0 406 Not Acceptable");
			out.println("Content-Type: text/html");
			out.println("Server: Bot");
			out.println("");
  		}
  		reader.close();
  	}
  	catch (Exception e)
  	{
  		System.out.println("Error: " + e);
  	}

  }
  
  /**
    * Method delete
    * Tries to open a file and delete it
    * Returns a header with the code 200 OK if the file was found and successfully deleted
    * Returns a header with the code 404 Not Found if the file was not found
    * Returns a header with the code 403 Forbidden if the file was found but could not be deleted
    * @param out stream allowing the server to send information to the client who sent the request
    * @param resourceName name of the resource the client asked to delete
    */
  private void delete (PrintWriter out, String resourceName)
  {
  
  	try
  	{
  		resourceName = docPath + resourceName;
  		File fileToDelete = new File (resourceName);
  		boolean exists = fileToDelete.exists() ;
  		boolean isDeleted = fileToDelete.delete();
  		if (exists && isDeleted)
  		{
  			out.println("HTTP/1.0 200 OK");
  			System.out.println("File was deleted");
  		}
  		else if (exists && !isDeleted)
  		{
  			out.println("HTTP/1.0 403 Forbidden");
  			System.out.println("File was found but not deleted"); // the client has no right to delete the resource.
  		}
  		else
  		{
  			out.println("HTTP/1.0 404 Not Found");
  			System.out.println("File was not deleted");
  		}
  		//send the end of the header
  		out.println("Content-Type: text/html");
  		out.println("Server: Bot");
  		out.println("");
  	}
  	catch (Exception e)
  	{
  		System.out.println("Error: " + e);
  	}
  }
  /**
    * Method PUT
    * Tries to create a file with the named chosen by the client in the request's header
    * Tries to write in the new file the content of the client's request's body
    * Returns a header with the code 200 OK if the file was successfully created and filled
    * Returns a header with the code 206 Partial Content if the file was created but the server couldn't write in it
    * Returns a header with the code 409 Conflict if the file could not be created 
    * @param out stream allowing the server to send information to the client who sent the request
    * @param in stream allowing the server to receive information from the client who sent the request
    * @param resourceName name of the resource the client 
    * @param body content of the resource the client wants to put on the server
    */
  private void put (BufferedReader in, PrintWriter out, String resourceName, String  body)
  {
  	try
  	{
  		resourceName = docPath + resourceName;
  		File fileToCreate = new File (resourceName);
  		if (fileToCreate.createNewFile())
  		{
  			if (fileToCreate.canWrite())
  			{
  				FileWriter myWriter = new FileWriter (resourceName);
  				myWriter.write(body);
  				myWriter.close();
  				out.println("HTTP/1.0 200 OK");
  			}
  			else
  			{
  				System.out.println("can not write in the new file");
  				out.println("HTTP/1.0 206 Partial Content"); // the request is partially completed : the file is created but it's empty
  			}
  		
  		}
  		else
		{
			System.out.println("file was not created");
			out.println("HTTP/1.0 409 Conflict"); // The file can't be created because it already exists : there's a conflict
		}
		
  		//send the end of the headers
  		out.println("Content-Type: text/html");
  		out.println("Server: Bot");
  		out.println("");
	
  	}
  	catch (Exception e)
  	{
  		System.out.println("Error : " + e);
  	}
  }
  
  
  /**
    * Method HEAD
    * Is identical to the GET method but the response it sends to the client has no body (only headers)
    
    * Returns a header with the code 200 OK if the file was found and successfully sent to the client
    * Returns a header with the code 404 Not Found if the file was not found
    * Returns a header with the code 406 Not Acceptable if the resource's type is not supported
    * @param out stream allowing the server to send information to the client who sent the request
    * @param resourceName name of the resource the client asked for
    * @param dataType type of the resource the client asked for
    */
  private void head (PrintWriter out, String resourceName, String dataType)
  {
  	try
  	{
  		resourceName = docPath + resourceName;
  		File fileToRead = error404File;
  		if (isTypeSupported(dataType))
  		{
  			fileToRead = new File (resourceName);
  			if (fileToRead.exists())
  			{
  				out.println("HTTP/1.0 200 OK");
  				out.println("Content-Type: " + dataType);
  				out.println("Server: Bot");
  				out.println("");
  				
  			}
  			else 
  			{
  				out.println("HTTP/1.0 404 Not Found");
  				out.println("Content-Type: text/html");
  				out.println("Server: Bot");
  				out.println("");
  			}
  		}
  		else
  		{
  			System.out.println("type : " + dataType + " not supported.");
  			
			out.println("HTTP/1.0 406 Not Acceptable");
			out.println("Content-Type: text/html");
			out.println("Server: Bot");
			out.println("");
		}

  	}
  	catch (Exception e)
  	{
  		System.out.println("Error: " + e);
  	}
  }
}















