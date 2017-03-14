package clientSystem;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Controller {


	public static void main(String []args) throws UnknownHostException, IOException, InterruptedException{
		int port =  2533; //Ändra till valfri port vid behov
		String ip = "192.168.1.103";//"192.168.1.2"; //Ändra till er ip fe80::8cd6:cf6:5828:32c%13

		Server1 server = new Server1(port);
		Client client1 = new Client("Sandra", ip, port);

		Client client2 = new Client("Vedrana", ip, port);
		client2.connect();
		
		Thread.sleep(2000);
		System.out.println("Sending now...");

		client2.sendMessage("Sandra", "Hej Sandra");
		client2.sendMessage("Sandra", "Fick du detta? Eller ej?");
		client2.sendIcon("Sandra", " ");
		Thread.sleep(2000);
		client1.connect();
		Thread.sleep(2000);

	}

}
