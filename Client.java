package clientSystem;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Client  {
	private int port;
	private String ip;
	private String user;
	private Connection connection;
	private ClientGUI GUI;

	public Client(String user, String ip, int port) throws UnknownHostException, IOException {
		//super();
		this.port = port;
		this.ip = ip;
		this.user = user;
		this.GUI = new ClientGUI(user, ip, port, Client.this);
	}

	public class Connection extends Thread {
		private Socket socket;
		private String ip;
		private int port;
		boolean connected = false;
		public ObjectOutputStream oos;
		public ObjectInputStream ois;
		private ArrayList<String> onlineList;

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
						try {
							this.oos = new ObjectOutputStream( socket.getOutputStream());
							this.ois = new ObjectInputStream( socket.getInputStream());
							System.out.println("SOCKET : " + socket);
							oos.writeObject(user);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			while (connected){
				try{

					Object recievedObject = ois.readObject();
					if (recievedObject instanceof Message){
						Message msg = (Message) recievedObject;
						if(msg.getType().equals("picture")){

							JPanel panel1 = new JPanel(new GridLayout(1,1));
							panel1.add(new JLabel(msg.getPicture()));
							JFrame frame = new JFrame("Picture");
							frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
							frame.setLayout(new FlowLayout(FlowLayout.CENTER));
							frame.add(panel1);
							frame.pack();
							frame.setVisible(true);


						} else if (msg.getType().equals("message")){
							System.out.println(msg.getMessage());
							Client.this.setChat(msg.getMessage());
						}
					} else {
						ArrayList<String> newList = (ArrayList<String>) recievedObject;
						this.onlineList = newList;
						Thread.sleep(1000);
						Client.this.setOnlineList(newList);
						System.out.println("NEW LIST : " + newList);

					}

					//Message msg = (Message) ois.readObject();

				}catch (IOException e) {
					//e.printStackTrace();
				}catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}catch (NullPointerException e){

				}catch (ClassCastException e){

				}catch (IndexOutOfBoundsException e){

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				} catch (IllegalStateException e){
					
				}

			}
			if(!connected){
				try {
					getOos().close();
					getOis().close();
					getSocket().close();
					System.out.println("DC");
					System.out.println("is socket closed? " + getSocket().isClosed());
					//this.connected = false;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e){

				}
				;
			}
		}

		public ArrayList<String> getOnlineList(){
			return onlineList;
		}


		public void disconnect() {
			this.connected = false;
			System.out.println("SOCKET DISCONNECT  -  " + socket + " " + connected);

			try {
				//Thread.currentThread().interrupt();
				if(socket != null){
					socket.close();
					//Thread.currentThread().interrupt();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			socket = null;
		}

		public ObjectOutputStream getOos(){
			return oos;
		}
		
		public ObjectInputStream getOis(){
			return ois;
		}

		public Socket getSocket(){
			return socket;
		}
	}

	public void sendMessage(String recipient, String msg) throws IOException{
		ObjectOutputStream oos = connection.getOos();
		Message message = new Message("message", user, recipient, msg);
		oos.writeObject(message);
	}

	public void sendIcon(String recipient, String directory) throws IOException{
		//Tryck in er lokala directory.
		ImageIcon picture = new ImageIcon("C:/pic.jpg"); 
		Message pic = new Message("picture", user, recipient, picture); 
		ObjectOutputStream oos = connection.getOos();
		oos.writeObject(pic);
	}

	public void disconnect() throws IOException{
		ObjectOutputStream oos = connection.getOos();
		ObjectInputStream ois = connection.getOis();
		Socket clientsSocket = connection.getSocket();
		oos.close();
		ois.close();
		clientsSocket.close();
		connection.disconnect();
		setOnlineList(new ArrayList<String>());
		if( oos != null){
			oos.close();
		}

		if( ois != null){
			ois.close();
		}

		if( clientsSocket != null){
			clientsSocket.close();
		}
		System.out.println("Client disconnected ( " + user + " ).");

		//connection = null;
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

	public void setIp(String newIp){
		this.ip = newIp;
	}

	public void setPort(int newPort){
		this.port = newPort;
	}

	public void setUser(String user){
		this.user = user;
	}

	public ArrayList<String> getOnlineList(){
		return connection.getOnlineList();
	}

	public void setOnlineList(ArrayList<String> list){
		GUI.setOnlineList(list);
	}

	public void setChat(String newRow){
		GUI.setChat(newRow);
	}
}

