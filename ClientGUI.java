package ClientSystem;
import javax.swing.*;


import gruppuppgift.Client;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.UnknownHostException;
import gruppuppgift.Server1;


public class ClientGUI extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JTextField tf;
	private JTextField tfip, tfPort;
	private JButton login, send;
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
		tf = new JTextField(host);
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
		send = new JButton("sänd");
		send.addActionListener(this);
		send.setEnabled(false);		
	
		JPanel southPanel = new JPanel();
		southPanel.add(login);
		southPanel.add(send);
		
		add(southPanel, BorderLayout.SOUTH);

	
		JPanel eastPanel = new JPanel(new GridLayout(1,1));
		add(eastPanel, BorderLayout.EAST);
		
		ta2 = new JTextArea(host, 80, 80);
		eastPanel.setPreferredSize(new Dimension(150,600));
		eastPanel.add(new JScrollPane(ta2));
		ta2.setEditable(false);
		add(eastPanel, BorderLayout.EAST);
		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();
	}
	

	void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}
	
	void connectionFailed() {
		login.setEnabled(true);
		send.setEnabled(false);

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

		try {
			
			String tt = ta.getText();
			client.sendMessage( tt);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
				} catch (IOException e1) {
					e1.printStackTrace();
				}
	
				tf.setText("");
				label.setText("Skriv ditt meddelande nedan");
				connected = true;
				login.setEnabled(false);
				send.setEnabled(true);
				tfip.setEditable(false);
				tfPort.setEditable(false);
				tf.addActionListener(this);
			}
		
		
		}
			

	 

	public static void main(String[] args) throws IOException {
		
		
		//new ClientGUI("Vedrana", 144);
		int port =  2543; //Ändra till valfri port vid behov
		String ip = "Sandras-MacBook-Pro.local"; //Ändra till er ip
		
		Server1 server = new Server1(port);

		Client client1 = new Client("Vedrana", ip, port);
		Client client2 = new Client("Adda", ip, port);
		Client client3 = new Client("Sandra", ip, port);
		//Client client4 = new Client("Evelyn", ip, port);

		client1.connect();
		client2.connect();
		client3.connect();
		//client4.connect();
		new ClientGUI("Vedrana",ip, port);
		new ClientGUI("Adda", ip, port);
		new ClientGUI("Sandra", ip, port);
		server.list();
	}

}