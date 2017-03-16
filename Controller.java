package gruppu;

import java.io.IOException;
import java.net.UnknownHostException;

public class Controller {
	public static void main(String []args) throws UnknownHostException, IOException, InterruptedException{
		int port =  2533; //Ändra till valfri port vid behov
		String ip = "192.168.2.53"; //Ändra till er ip
		Server1 server = new Server1(port);

	//	Client client1 = new Client("Vedrana", ip, port);
		Client client2 = new Client("Adda", ip, port);
	//	Client client3 = new Client("Sandra", ip, port);
		Client client4 = new Client("Evelyn", ip, port);

		//client1.connect();
		client2.connect();
		//client3.connect();
		client4.connect();

		Thread.sleep(350);
		client2.sendMessage("Bara");
/*		Thread.sleep(350);
		client2.sendMessage("Så");
		Thread.sleep(350);
		client2.sendMessage("Ni");
		Thread.sleep(350);
		client2.sendMessage("Vet");
		client4.sendMessage("Så funkar det!");
		client4.disconnect();
		Thread.sleep(350);
		client4.connect();
		Thread.sleep(350);
		client4.sendMessage("JAG ÄR TILLBAKA!!!");
		Thread.sleep(350);
*/

		client2.getMessage();
		
//		client2.disconnect();
		client4.disconnect();

		Thread.sleep(350);

		client2.connect();
		client4.connect();
		Thread.sleep(350);

		client2.sendMessage("Tja! Någon här?");
		Thread.sleep(350);

		client4.sendMessage("Jag är här!?");

		Thread.sleep(350);

		server.disconnect();
	}

}
