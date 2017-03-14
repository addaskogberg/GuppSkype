package clientSystem;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class Server1 extends Thread {
	private Connection connection;
	private int port;
	private final static Logger offlineLog = Logger.getLogger("membersoffline");
	private final static Logger onlineLog = Logger.getLogger("membersonline");
	private final static Logger messagesLog = Logger.getLogger("sentmessages");
	private FileHandler onlineFile = null;
	private FileHandler offlineFile = null;
	private FileHandler messagesSentFile = null;
	private MessageHandler messageHandler;
	private ArrayList<ClientHandler> userList;
	private ArrayList<Message> messageList;


	public Server1(int port) {
		//super();
		System.out.println("Trying to boot server.");
		this.userList = new ArrayList<ClientHandler>();
		this.messageList = new ArrayList<Message>();
		this.port = port;
		this.messageHandler = new MessageHandler();
		this.connection = new Connection(port);
		connection.start();
		messageHandler.start();

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

	private class MessageHandler extends Thread{;
	private Message msg;

	public MessageHandler(){

	}

	public void run(){	


		while(!Thread.interrupted()){

			if ( messageList != null){
				for(int i = 0; i<messageList.size(); i++ ){
					msg = messageList.get(i);
					if(msg != null){


						String recipient = msg.getRecipient();
						//System.out.println("messageList size: " + messageList.size());
						for(int j = 0; j<userList.size(); j++){
							if (recipient.equals(userList.get(j).getUserName()) && userList.size()>0 && recipient != null && userList.get(j).getUserName() != null ){
								try {		
									userList.get(j).sendToRecipient(msg);
									messageList.remove(i);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						}	
					}	}

			}
		}
	}
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
						userList.add(clientHandler);
						onlineLog.info("Client genom port: " + port);
					} catch (IOException e){
						if(socket!=null)
							//offlineLog.info("Client logged off: " + port);
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
		private String user;

		public ClientHandler(Socket socket) throws IOException {
			//System.out.println("New handler up!");
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());


			this.connectedHandler = true;
			start();
		}

		public void run() {

			while(user == null){
				try{
					String user = (String) ois.readObject();
					this.user = user;
				}catch (ClassNotFoundException e) {} catch (IOException e) {}
			}


			while(connectedHandler) {
				//System.out.println("Trying to read msgs.");

				try {		
					Message msg = (Message) ois.readObject();
					if(msg.getType().equals("picture")){
						messageList.add(msg);
						messagesLog.info("Sent from: "+ port + "Message" + " type:" + msg.getType());
					} else if (msg.getType().equals("message")){
						messageList.add(msg);
						messagesLog.info("Sent from: "+ port + "Message" + msg.getMessage() + " type:" + msg.getType());
					}
					
					


				} catch (ClassNotFoundException e) {} catch (IOException e) {
					// TODO Auto-generated catch block
					connectedHandler = false;
					if (userList != null){


						for ( int i = 0;  i < userList.size(); i++){
							if (getUserName() == userList.get(i).getName() ){
								//System.out.println(userList.get(i).getName());
								userList.remove(i);	
							}   
						}
					}
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

		public void sendToRecipient(Message msg) throws IOException{
			oos.writeObject(msg);
		}

		public String getUserName(){
			return user;	
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