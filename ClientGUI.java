package gruppu;

import javax.swing.*;
import gruppu.Client;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import gruppu.Server1;

public class ClientGUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JTextField tf;
	private JTextField tfip, tfPort;
	private JButton login, logout,send;
	private JTextArea ta;
	private JTextArea ta2;
	private boolean connected;
	private Client client;

	ClientGUI(String host, String ip, int port) throws IOException {
		super("Gruppuppgift");

		JPanel northPanel = new JPanel(new GridLayout(3,1));
		JPanel ipAndPort = new JPanel(new GridLayout(1,5, 1, 3));
		tfip = new JTextField(ip);
		tfPort = new JTextField("" + port);
		tfPort.setHorizontalAlignment(SwingConstants.RIGHT);

		ipAndPort.add(new JLabel("ip:  "));
		ipAndPort.add(tfip);
		ipAndPort.add(new JLabel("Port:  "));
		ipAndPort.add(tfPort);
		ipAndPort.add(new JLabel(""));
		northPanel.add(ipAndPort);

		label = new JLabel("Ange användarnamn", SwingConstants.CENTER);
		northPanel.add(label);
		tf = new JTextField(" ");
		tf.setBackground(Color.WHITE);
		northPanel.add(tf);
		add(northPanel, BorderLayout.NORTH);

		ta = new JTextArea("Välkommen\n", 80, 80);
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		add(centerPanel, BorderLayout.CENTER);

		login = new JButton("Logga in");
		login.addActionListener(this);
		logout = new JButton("Logga ut");
		logout.addActionListener(this);
		logout.setEnabled(false);		
		send = new JButton("sänd");
		send.addActionListener(this);
		send.setEnabled(false);		

		JPanel southPanel = new JPanel();
		southPanel.add(login);
		southPanel.add(logout);
		southPanel.add(send);

		add(southPanel, BorderLayout.SOUTH);

		JPanel eastPanel = new JPanel(new GridLayout(1,1));
		add(eastPanel, BorderLayout.EAST);

		ta2 = new JTextArea(host, 80, 80);
		ta2 = new JTextArea("Användare online", 80, 80);
		eastPanel.setPreferredSize(new Dimension(150,600));
		eastPanel.add(new JScrollPane(ta2));
		ta2.setEditable(false);
		add(eastPanel, BorderLayout.EAST);

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();
	}

	public void getMessagesWhileConnected(){
		System.out.println("Hej från metoden");
		ta.setText(client.getMessage());
		/*if(client.getMessage().length() > 0 ){
			ta.setText(client.getMessage());			
		}
		else
		{
			System.out.println("Inget från servern");
		}*/
	}

	public void noMethod(){
		System.out.println("Kallar på en metod");
	}
	
	void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}

	void connectionFailed() {
		login.setEnabled(true);
		logout.setEnabled(false);

		label.setText("Ange ditt användarnamn nedan	");
		tf.setText("Anonym");
		tfPort.setText("") ;

		tfip.setEditable(false);
		tfPort.setEditable(false);
		tf.removeActionListener(this);
		connected = false;
	}

	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();

		if(o == send) {
			try 
			{
				//String tt = ta.getText();
				String tt = tf.getText();
				//System.out.println("Meddelande skickas: " + tt);
				client.sendMessage(tt);
				//ta.setText(client.getMessage());
				getMessagesWhileConnected();

			} 
			catch (IOException e1) 
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(connected) {
			tf.setText("");
			return;
		}

		if(o == logout) {
			return;
		}
		if(connected) {
			tf.setText("");
			return;
		}

		if(o == login) {
			String username = tf.getText().trim();
			if(username.length() == 0)
				return;
			String ip = tfip.getText().trim();
			if(ip.length() == 0)
				return;
			String portNumber = tfPort.getText().trim();
			if(portNumber.length() == 0)
				return;
			int port = 0;
			try {
				port = Integer.parseInt(portNumber);
			}
			catch(Exception en) {
				return;
			}
			try {
				client = new Client(username, ip, port);
				client.connect();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			tf.setText("");
			label.setText("Skriv dtt meddelande nedan");
			connected = true;
			login.setEnabled(false);
			logout.setEnabled(true);
			send.setEnabled(true);
			tfip.setEditable(false);
			tfPort.setEditable(false);
			tf.addActionListener(this);
		}
	}
	

	public static void main(String[] args) throws IOException {
		//new ClientGUI("Vedrana", 144);
		int port =  2533; //Ändra till valfri port vid behov
		String ip = "192.168.2.53"; //Ändra till er ip

		Server1 server = new Server1(port);

		//Client client1 = new Client("Vedrana", ip, port);
		//Client client2 = new Client("Adda", ip, port);
		//Client client3 = new Client("Sandra", ip, port);
		//Client client4 = new Client("Evelyn", ip, port);

		//client1.connect();
		//client2.connect();
		//client3.connect();
		//client4.connect();
		

		ClientGUI clientGUI = new ClientGUI("Adda", ip, port);
		ClientGUI clientGUI2 = new ClientGUI("Sandra", ip, port);
		ClientGUI clientGUI3 = new ClientGUI("Vedrana",ip, port);
		ClientGUI clientGUI4 = new ClientGUI("Evelyn", ip, port);
		/*	
		clientGUI.getMessagesWhileConnected();
		clientGUI2.getMessagesWhileConnected();
		clientGUI3.getMessagesWhileConnected();
		clientGUI4.getMessagesWhileConnected();
		*/
		/*
		while(true){
			
			clientGUI.getMessagesWhileConnected();
			clientGUI2.getMessagesWhileConnected();
			clientGUI3.getMessagesWhileConnected();
			clientGUI4.getMessagesWhileConnected();

			clientGUI.ta.setText(server.getListMessages().toString());
			clientGUI2.ta.setText(server.getListMessages().toString());
			clientGUI3.ta.setText(server.getListMessages().toString());
			clientGUI4.ta.setText(server.getListMessages().toString());

			clientGUI.ta.setText(clientGUI.client.getMessage());
			clientGUI2.ta.setText(clientGUI2.client.getMessage());
			clientGUI3.ta.setText(clientGUI3.client.getMessage());
			clientGUI4.ta.setText(clientGUI4.client.getMessage());


			
		}
		 */
		//server.list();
	}
}
