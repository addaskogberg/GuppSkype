package clientSystem;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;

public class Message implements Serializable{
	
	private String type;
	private String sender;
	private String message;
	private String recipient;
	private ImageIcon picture;

	public Message(String type, String sender, String recipient, ImageIcon picture){
		this.type = type;
		this.sender = sender;
		this.recipient = recipient;
		this.picture = picture;
	}
	
	public Message(String type, String sender, String recipient, String msg){
		this.type = type;
		this.message = msg;
		this.sender = sender;
		this.recipient = recipient;
	}

	
	public String getType(){
		return type;
	}
	
	public String getRecipient(){
		return recipient;
	}
	
	
	public String getMessage(){
		return getTimeStamp() + "| " + sender + " : " + message;
	}
	
	public ImageIcon getPicture(){
		return picture;
	}
	
	
	
	public String getTimeStamp(){	
		String date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
		String timeStamp = new SimpleDateFormat("HH.mm.ss").format(new Date());
		return "DATE " + date + " TIME " + timeStamp + " ";
	}
	
	public String getSender(){
		return sender;
	}
	
}
