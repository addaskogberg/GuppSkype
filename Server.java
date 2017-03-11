package clientSystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class Server extends Thread {
	private Connection connection;
	private int port;

	public Server(int port) {
		//super();
		System.out.println("Trying to boot server.");
		this.port = port;
		this.connection = new Connection(port);
		connection.start();
	}

	public class Connection extends Thread {
		private int port;
		private ServerSocket serverSocket;

		public Connection(int port){
			this.port = port;
		}

		public void run() {
			Socket socket = null;

			try(ServerSocket serverSocketCreate = new ServerSocket(port)){
				this.serverSocket = serverSocketCreate;
				while(!Thread.interrupted()){
					try{
						socket = serverSocket.accept();
						ClientHandler clientHandler = new ClientHandler(socket);
					} catch (IOException e){
						if(socket!=null)
							socket.close();
						Thread.currentThread().interrupt();

					}
				} 
			} catch (IOException e){
				System.err.println(e);
			}

		}

		public void shutDown() throws IOException {
			serverSocket.close();
		}


	}	

	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		private boolean connectedHandler;

		public ClientHandler(Socket socket) throws IOException {
			//System.out.println("New handler up!");
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			this.connectedHandler = true;
			start();
		}

		public void run() {
			while(connectedHandler) {
				//System.out.println("Trying to read msgs.");
				try {		
					//	System.out.println("Trying to read msgs!!!");
					String text = (String) ois.readObject();
					System.out.println(text);

				} catch (ClassNotFoundException e) {} catch (IOException e) {
					// TODO Auto-generated catch block
					connectedHandler = false;
					try {
						close();
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
					//	e.printStackTrace();
				} 
			} 
		}
		public void close() throws IOException {  
			if (socket != null)  socket.close();
			if (ois != null)  ois.close();
			if (oos != null) oos.close();
		}


	}

	public void disconnect(){
		try {
			connection.shutDown();
		} catch(Exception e2) {
			//			e2.printStackTrace();

		}	
		System.out.println("Server shutdown successfully.");
	}


}	







