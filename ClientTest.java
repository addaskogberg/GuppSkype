package gruppuppgift;



//import java.io.DataOutputStream;
import java.io.IOException;
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
			MyClient = new Socket("DESKTOP-5TCP05T", 1498);
		}
		catch(Exception e){
			System.out.println("Något gick åt helvete: " + e.getMessage());
			connect = false;
		}
		finally{
			if(connect){
				System.out.println("Inget exception");
			}
		}

//		PrintStream output = null;;
		java.io.OutputStream outstream = null;
		try {
			outstream = MyClient.getOutputStream(); 
			PrintWriter out = new PrintWriter(outstream);
			String toSend = "Hello World!";
			out.print(toSend);

//			output = new PrintStream (MyClient.getOutputStream());
//			output.print("Hello World");
//			System.out.println(output.toString());
		}
		catch (Exception e) {
			System.out.println(e);
		}

		try {
			//output.close();
			outstream.close();
			//input.close();
			MyClient.close();
		} 
		catch (IOException e) {
			System.out.println(e);
		}

	}

	public static void main(String[] args) {
		Socket socket = new Socket();
		ClientTest client = new ClientTest(socket, 1500);
		try {
			client.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

