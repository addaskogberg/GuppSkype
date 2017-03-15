package clientSystem;

import javax.swing.*;



import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



public class ClientGUI extends JFrame implements ActionListener {

	private JLabel label;
	private JTextField tf;
	private JTextField tf2;
	private JTextField tfip, tfPort;
	private JButton login, logOut,send;
	private JTextArea ta;
	private GridLayout onlineList;
	private JPanel eastPanel;
	private boolean connected;
	private Client client;
	private String ipConnect;
	private int portConnect;
	private String host;
	private String sendTo;
	


	ClientGUI(String host, String ip, int port, Client client) throws IOException {
		super("Gruppuppgift");
		this.client = client;
		this.ipConnect = ip;
		this.host = host;
		this.portConnect = port;
		
		JPanel northPanel = new JPanel(new GridLayout(3,1));
		JPanel ipAndPort = new JPanel(new GridLayout(1,5, 1, 3));
		tfip = new JTextField(ipConnect);
		tfPort = new JTextField("" + port);
		tfPort.setHorizontalAlignment(SwingConstants.LEFT);

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
		tf2 = new JTextField("");
		tf2.setBackground(Color.WHITE);
		northPanel.add(tf2);
		northPanel.add(tf);
		add(northPanel, BorderLayout.NORTH);

		ta = new JTextArea("Välkommen\n", 80, 80);
		JPanel centerPanel = new JPanel(new GridLayout(1,1));
		centerPanel.add(new JScrollPane(ta));
		ta.setEditable(false);
		add(centerPanel, BorderLayout.CENTER);

		login = new JButton("Logga in");
		login.addActionListener(this);
		logOut = new JButton("Logga ut");
		logOut.addActionListener(this);
		logOut.setEnabled(false);		
		send = new JButton("sänd");
		send.addActionListener(this);
		send.setEnabled(true);		

		JPanel southPanel = new JPanel();
		southPanel.add(login);
		southPanel.add(logOut);
		southPanel.add(send);
		

		add(southPanel, BorderLayout.SOUTH);

		this.eastPanel = new JPanel(new GridLayout(0,1));
		add(eastPanel, BorderLayout.EAST);
		//getOnlineList
		//onlineList = new JTextArea(host, 80, 80);
		//onlineList = new JTextArea("Användare online", 80, 80);
		//onlineList = new GridLayout(10, 1);
		//add(onlineList, BorderLayout.EAST);

		//onlineList.setLayout(new GridLayout(rows, cols));

		eastPanel.setPreferredSize(new Dimension(150,600));
		eastPanel.add(new JScrollPane());
		//onlineList.setEditable(false);
		add(eastPanel, BorderLayout.EAST);


		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 600);
		setVisible(true);
		tf.requestFocus();

		//this.client = new Client(host, ip, port);
	}

	void append(String str) {
		ta.append(str);
		ta.setCaretPosition(ta.getText().length() - 1);
	}

	void connectionFailed() {
		login.setEnabled(true);
		logOut.setEnabled(true);

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
		System.out.println(e);
		String buttonText = e.getActionCommand();
		
		if(o == send) {
			///System.out.println("SEEEEEEEEEEEEEEEND");
			try {

				String tt = tf2.getText();
				client.sendMessage(sendTo, tt);
				
				setChat(getTimeStamp() + " :> " + sendTo + " : " + tt);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
//		if(connected) {
//			tf.setText("");
//			return;
//		}
		if(buttonText != null && buttonText != "Logga ut" &&  buttonText != "sänd" && buttonText != "Logga in") {
			setSendTo(buttonText);
		}
		
		if(o == logOut) {
			System.out.println("LOGGING OUT");
			try {
				client.disconnect();
				eastPanel.removeAll();
				eastPanel.revalidate();
				eastPanel.repaint();
				connected = false;
				login.setEnabled(true);
				logOut.setEnabled(false);
				send.setEnabled(false);
				tfip.setEditable(true);
				tfPort.setEditable(true);
				tf.setEnabled(true);

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}catch (NullPointerException b){
				
			}
		}
//		if(connected) {
//			tf.setText("");
//			return;
//		}

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
//			try {
//				client = new Client(username, ip, port);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
			System.out.println("Försöker LOGGA IN");
			client.setIp(ip);
			client.setPort(port);
			client.setUser(username);
			client.connect();

		
			label.setText("Skriv dtt meddelande nedan");
			connected = true;
			login.setEnabled(false);
			logOut.setEnabled(true);
			send.setEnabled(true);
			tf.setEnabled(false);
			tfip.setEditable(false);
			tfPort.setEditable(false);
			tf.setEditable(false);
			tf.addActionListener(this);
			logOut.addActionListener(this);
			ta.repaint();
		}

	}

	public void setOnlineList(ArrayList<String> onlineList){
		try{
			eastPanel.removeAll();
			for (int i = 0; i < onlineList.size(); i++) {
				JButton button = new JButton(onlineList.get(i));
				eastPanel.add(button);
				button.addActionListener(this);
			}}  catch (NullPointerException e){
		
			}
		System.out.println(onlineList.size() + " " + host);
		eastPanel.revalidate();
	}

	public void setSendTo(String sendTo){
		this.sendTo = sendTo;
	}
	
	public void setChat(String newRow){
		
		ta.setText(ta.getText() + "\n" + newRow);
	}
	
	
	public String getTimeStamp(){	
		String date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
		String timeStamp = new SimpleDateFormat("HH.mm.ss").format(new Date());
		return "DATE " + date + " TIME " + timeStamp + " ";
	}
}
