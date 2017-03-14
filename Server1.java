package ClientSystem;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



public class Server1 extends Thread {
	private Connection connection;
	private int port;
	private final static Logger offlineLog = Logger.getLogger("membersoffline");
	private final static Logger onlineLog = Logger.getLogger("membersonline");
	private final static Logger messagesLog = Logger.getLogger("sentmessages");
	private FileHandler onlineFile = null;
	private FileHandler offlineFile = null;
	private FileHandler messagesSentFile = null;
	private User user1;
	private String user;
	private ArrayList<User> listUsers;


	public Server1(int port) {
		//super();
		System.out.println("Trying to boot server.");
		this.port = port;
		this.connection = new Connection(user, port);
		this.listUsers = new ArrayList<User>();
		connection.start();
	        try {
	        	onlineFile = new FileHandler("myapp-log.%u.%g.txt");
	        	System.out.println(onlineFile);
	        	offlineFile = new FileHandler("myapp-log.%u.%g.txt");
	        	System.out.println(offlineFile);
	        	messagesSentFile = new FileHandler("myapp-log.%u.%g.txt");
	        	System.out.println(messagesSentFile);
	        	
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        onlineFile.setFormatter(new SimpleFormatter());
	        onlineLog.addHandler(onlineFile);
	        offlineFile.setFormatter(new SimpleFormatter());
	        offlineLog.addHandler(offlineFile);
	        messagesSentFile.setFormatter(new SimpleFormatter());
	        messagesLog.addHandler(messagesSentFile);
	       // membersonline.setUseParentHandlers(false);
	    }
	

	public class Connection extends Thread {
		private int port;
		private ServerSocket serverSocket;
		private User user1;
		private String user;

		public Connection(String user, int port){
			this.user = user;	
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
						onlineLog.info("Client genom port: " + port);
						
					
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
					messagesLog.info("Sent from: "+ port + "Message" + text);
					//user.offline(text);

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
	
	public String list() {
		String test;
		return test  = Arrays.toString(listUsers.toArray());
		
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