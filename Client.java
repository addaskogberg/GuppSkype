package gruppu;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private int port;
	private String ip;
	private String user;
	private Connection connection;
	//private User user1;

	public Client(String user, String ip, int port) throws UnknownHostException, IOException {
		super();
		this.port = port;
		this.ip = ip;
		this.user = user;		
	}

	public void sendMessage(String msg) throws IOException{
		if(msg != null){
			//System.out.println("sendMessage(String msg) " + msg);
			ObjectOutputStream objectOutputStream = connection.getObjectOutputStream();
			objectOutputStream.writeObject(" | " + user + " : " + msg);
		}
	}
	
	public String getMessage(){
		ObjectInputStream objectInputStream = connection.getObjectInputStream();
		String text = "";
		try {
			text = (String) objectInputStream.readObject();
			System.out.println("Från servern: " + text);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
		
		return text;
	}

	public void connect(){
		this.connection = new Connection("user1", ip, port);
		connection.start();

	}

	public void disconnect(){
		Socket clientsSocket = connection.getSocket();
		try{
			clientsSocket.close();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		System.out.println("Client disconnected ( " + user + " ).");
		connection = null;
	}

	public class Connection extends Thread {
		private Socket socket;
		private String ip;
		private int port;
		boolean connected = false;
		public ObjectOutputStream objectOutputStream;
		public ObjectInputStream objectInputStream;
		private String user1;

		public Connection(String user1, String ip, int port){
			this.port = port;
			this.ip = ip;
			this.socket = null;
			this.user1 = user1;
		}

		public void run(){
			while (!connected){
				try{
					//System.out.println(user + " trying to connect... " + ip);
					this.socket = new Socket(ip, port);
					if(null!= socket){			
						connected = true;
					}		
				}catch(Exception e){
					System.out.println("Failed to " + user + " connect. Error msg: " + e.getMessage());
				}
				finally{
					if(connected){

						System.out.println(user + " connected to the server.");
						try {
							this.objectOutputStream = new ObjectOutputStream( socket.getOutputStream());
							this.objectInputStream = new ObjectInputStream( socket.getInputStream());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		public ObjectOutputStream getObjectOutputStream(){
			return objectOutputStream;
		}

		public ObjectInputStream getObjectInputStream(){
			return objectInputStream;
		}

		public Socket getSocket(){
			return socket;
		}
	}	
}
