package clientSystem;

import java.io.IOException;
import java.net.UnknownHostException;

public class Controller {

	public static void main(String []args) throws UnknownHostException, IOException, InterruptedException{
		int port =  2223; //Ändra till valfri port vid behov
		String ip = "192.168.1.103";//"192.168.1.2"; //Ändra till er ip fe80::8cd6:cf6:5828:32c%13

		Server server = new Server(port);
		
		Client client1 = new Client("Sandra", ip, port);
		Client client2 = new Client("Vedrana", ip, port);
		Client client3 = new Client("Adda", ip, port);
		Client client4 = new Client("Evelyn", ip, port);

	}

}
