package gruppuppgift;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest {


	ServerSocket MyService;
	private Socket serviceSocket;

	public ServerTest() {
		super();
	}

	
	public void start (){
		while(true){
			try {
				MyService = new ServerSocket(1498);
			}
			catch (IOException e) {
				System.out.println(e);
			}


			try {
				serviceSocket = MyService.accept();
			}
			catch (IOException e) {
				System.out.println(e);
			} 

		    DataInputStream input = null;
		    try {
		       input = new DataInputStream(serviceSocket.getInputStream());
		       System.out.println(input.toString());
		    }
		    catch (IOException e) {
		       System.out.println(e);
		    }
		    
	    
		    
		    PrintStream output;
		    try {
		       output = new PrintStream(serviceSocket.getOutputStream());
		       System.out.println(output.toString());
		    }
		    catch (IOException e) {
		       System.out.println(e);
		    }
		
			
		    try {
		       // output.close();
		        input.close();
		        serviceSocket.close();
		        MyService.close();
		     } 
		     catch (IOException e) {
		        System.out.println(e);
		     }


		}


	}



	public static void main (String []args){
		ServerTest ServerTest = new ServerTest();
		ServerTest.start();

	}
}	







