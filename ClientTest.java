package gruppuppgift;



import java.io.DataOutputStream;
//import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
//import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientTest {
	private Socket MyClient;

	public ClientTest(Socket myClient, int port) {
		super();
		MyClient = myClient;
	}

	public void start() throws IOException{
		System.out.println("Startar socket");
		boolean connect = true; 
		try{
			//MyClient = new Socket("BENNY-ZENBOOK", 1500);
			MyClient = new Socket("DESKTOP-5TCP05T", 1497);
		}
		catch(Exception e){
			System.out.println("Fan också! Något gick åt helvete: " + e.getMessage());
			connect = false;
		}
		finally{
			if(connect){
				System.out.println("Inget exception");
			}
		}

		ObjectOutputStream outstream = null;
		try {
			outstream = new ObjectOutputStream( MyClient.getOutputStream()); 
			
			String toSend = "Hello server!";
			outstream.writeObject(toSend);

		}
		catch (Exception e) {
			System.out.println(e);
		}

		try {
			
			outstream.close();
			MyClient.close();
		} 
		catch (IOException e) {
			System.out.println(e);
		}

	}

	public static void main(String[] args) {
		Socket socket = new Socket();
		ClientTest client = new ClientTest(socket, 1497);
		try {
			client.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

