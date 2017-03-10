package gruppuppgift;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerTest extends Thread {
	private ServerSocket MyService;
	public Socket serviceSocket;
	private int port = 1497;

	public ServerTest() {
		super();
	}

	public void start (){
		while(true){
			try {
				MyService = new ServerSocket(port);
			}
			catch (IOException e) {
				System.out.println(e);
			}


			try {
				serviceSocket = MyService.accept();
				new ClientHandler(serviceSocket);
			}
			catch (IOException e) {
				System.out.println(e);
			} 

		}
	}

	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		public ClientHandler(Socket socket) throws IOException {
			this.socket = socket;
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			start();
		}
		public void run() {
			ClientTest CT;
			try {
				while(true) {
					try {
						String text = (String) ois.readObject();
						System.out.println(text);
						
					} catch (ClassNotFoundException e) {}
				}
			} catch(IOException e) {
				try {
					socket.close();
				} catch(Exception e2) {}
			}
			System.out.println("Klient nerkopplad");
		}
	}



	public static void main (String []args){
		ServerTest ServerTest = new ServerTest();
		ServerTest.start();
		
	}
}	







