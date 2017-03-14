package ClientSystem;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {
	private int port;
	private String ip;
	private String user;
	private Connection connection;
	private User user1;

	public Client(String user, String ip, int port) throws UnknownHostException, IOException {
		super();
		this.port = port;
		this.ip = ip;
		this.user = user;
		
	}

	public class Connection extends Thread {
		private Socket socket;
		private String ip;
		private int port;
		boolean connected = false;
		public ObjectOutputStream oos;
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
							this.oos = new ObjectOutputStream( socket.getOutputStream());
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		public ObjectOutputStream getOos(){
			return oos;
		}

		public Socket getSocket(){
			return socket;
		}
	}


	public void sendMessage(String msg) throws IOException{
		ObjectOutputStream oos = connection.getOos();
		//String date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
		//String timeStamp = new SimpleDateFormat("HH.mm").format(new Date());
		oos.writeObject(" | " + user + " : " + msg);
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

	public void connect(){
		this.connection = new Connection("user1", ip, port);
		connection.start();
		
	}

}