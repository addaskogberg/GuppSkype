package clientSystem;

import java.io.IOException;
import java.net.UnknownHostException;

public class Controller {
	public static void main(String []args) throws UnknownHostException, IOException, InterruptedException{
		int port =  2535; //Ändra till valfri port vid behov
		String ip = "192.168.1.2";//"192.168.1.2"; //Ändra till er ip fe80::8cd6:cf6:5828:32c%13

		Server server = new Server(port);
		Client client1 = new Client("Sandra", ip, port);
		Client client2 = new Client("Vedrana", ip, port);
		client1.connect();
		client2.connect();
		
		Thread.sleep(2000);
		System.out.println("Sending now...");

		client2.sendMessage("Sandra", "Hej Sandra");
		client2.sendMessage("Sandra", "Fick du detta? Eller ej?");
		//client2.sendIcon("Sandra", " ");
		Thread.sleep(7000);
		client2.disconnect(); //disconnect iz dampig, därav ingen loggning av offline heller
	}
}
