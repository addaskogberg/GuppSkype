package clientSystem;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Client {
	private int port;
	private String ip;
	private String user;
	private Connection connection;

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
		public ObjectInputStream ois;

		public Connection(String ip, int port){
			this.port = port;
			this.ip = ip;
			this.socket = null;
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
					//System.out.println(user + " failed to connect. Error msg: " + e.getMessage());
				}
				finally{
					if(connected){

						//System.out.println(user + " connected to the server.");
						try {
							this.oos = new ObjectOutputStream( socket.getOutputStream());
							this.ois = new ObjectInputStream( socket.getInputStream());
							oos.writeObject(user);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
						while (connected){
							try{
								Message msg = (Message) ois.readObject();
								
								if(msg.getType().equals("picture")){
			
									JPanel panel1 = new JPanel(new GridLayout(1,1));
									panel1.add(new JLabel(msg.getPicture()));
									JFrame frame = new JFrame("P2Viewer");
									frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
									frame.setLayout(new FlowLayout(FlowLayout.CENTER));
									frame.add(panel1);
									frame.pack();
									frame.setVisible(true);
			
								} else if (msg.getType().equals("message")){
									System.out.println(msg.getMessage());
									System.out.println("I GOT THE FUCKING MSG " + user);
			
								}
							}catch (IOException e) {
								//e.printStackTrace();
							} catch (ClassNotFoundException e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
							}
						}
		}

		public void disconnect(){
			connected = false;
		}

		public ObjectOutputStream getOos(){
			return oos;
		}

		public Socket getSocket(){
			return socket;
		}
	}


	public void sendMessage(String recipient, String msg) throws IOException{
		ObjectOutputStream oos = connection.getOos();

		Message message = new Message("message", user, recipient, msg);
		oos.writeObject(message);
		//oos.writeObject(getTimeStamp() + "| " + user + " : " + msg);
	}


	public void sendIcon(String recipient, String directory) throws IOException{
		ImageIcon picture = new ImageIcon("C:/Users/Vedde/Desktop/17.jpg"); 
		Message pic = new Message("picture", user, recipient, picture); 
		ObjectOutputStream oos = connection.getOos();

		oos.writeObject(pic);

		//oos.writeObject(getTimeStamp() + "| " + user + " : " + msg);
	}




	public void disconnect() throws IOException{
		//connection.interruptThread();
		//connection.disconnect();
		ObjectOutputStream oos = connection.getOos();
		oos.writeObject(getTimeStamp() + user + " disconnected from server." );
		Socket clientsSocket = connection.getSocket();
		try{
			clientsSocket.close();
			//connection.disconnect();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		System.out.println("Client disconnected ( " + user + " ).");
		connection = null;
	}

	public void connect(){
		this.connection = new Connection(ip, port);
		connection.start();
	}
	public String getTimeStamp(){	
		String date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
		String timeStamp = new SimpleDateFormat("HH.mm.ss").format(new Date());
		return "DATE " + date + " TIME " + timeStamp + " ";
	}
}

