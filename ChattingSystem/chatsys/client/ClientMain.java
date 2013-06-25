package chatsys.client;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import chatsys.client.windows.LoginWindow;
import chatsys.entity.User;


public class ClientMain {
	
	
	
	public static User currentUser;
	public static List<User> onlineUsers;
	public static Map<Long,JTextPane> privateChat;
	public static Set<Long> blackList;
	
	public static Socket socket;
	public static ObjectInputStream ois;
	public static ObjectOutputStream oos;
	
	private static void init(){
		Properties p=new Properties();
		try {
			p.load(new FileInputStream("client_config.txt"));
		}  catch (IOException e) {
			System.out.println("IOException!");
			System.exit(0);
		}
		
		String serverIP=p.getProperty("ServerIP");
		int serberPort=Integer.parseInt(p.getProperty("ServerPort"));
		
		try {
			
			onlineUsers=new ArrayList<User>();
			privateChat=new HashMap<Long,JTextPane>();
			blackList=new HashSet<Long>();
			
			socket=new Socket(serverIP,serberPort);
			oos=new ObjectOutputStream(socket.getOutputStream());
			ois=new ObjectInputStream(socket.getInputStream());
			
		} catch (Exception e) {
			System.out.println("SocketError!");
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		ClientMain.init();
		new LoginWindow().showMe();
	}

}
