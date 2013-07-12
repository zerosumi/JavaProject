package chatsys.client;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

import chatsys.client.windows.LoginWindow;
import chatsys.entity.User;

public class ClientMain {

	//private Socket client;
	private static ClientThread clientThread;
	//private String ip;
	//private int port;

//	public boolean start() {
//		try {
//			client = new Socket();
//			// client.connect(new InetSocketAddress(Constants.SERVER_IP,
//			// Constants.SERVER_PORT), 3000);
//			client.connect(new InetSocketAddress(ip, port), 3000);
//			if (client.isConnected()) {
//				// System.out.println("Connected..");
//				clientThread = new ClientThread(client);
//				clientThread.start();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}


	public static User currentUser;
	public static List<User> onlineUsers;
	public static Map<Long, JTextPane> privateChat;
	public static Set<Long> blackList;

	public static Socket socket;
	public static ObjectInputStream ois;
	public static ObjectOutputStream oos;

	private static void init() {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream("client_info.txt"));
		} catch (IOException e) {
			System.out.println("IOException!");
			System.exit(0);
		}

		String serverIP = p.getProperty("ServerIP");
		int serberPort = Integer.parseInt(p.getProperty("ServerPort"));

		try {

			onlineUsers = new ArrayList<User>();
			privateChat = new HashMap<Long, JTextPane>();
			blackList = new HashSet<Long>();

			socket = new Socket(serverIP, serberPort);
			clientThread = new ClientThread(socket);
			clientThread.start();
			//oos = new ObjectOutputStream(socket.getOutputStream());
			//ois = new ObjectInputStream(socket.getInputStream());

		} catch (Exception e) {
			System.out.println("SocketError!");
			System.exit(0);
		}
	}

	// 直接通过client得到读线程
	public static ClientInputThread getClientInputThread() {
		return clientThread.getIn();
	}

	// 直接通过client得到写线程
	public static ClientOutputThread getClientOutputThread() {
		return clientThread.getOut();
	}

	// 直接通过client停止读写消息
	public void setIsStart(boolean isStart) {
		clientThread.getIn().setStart(isStart);
		clientThread.getOut().setStart(isStart);
	}

	public static void main(String[] args) {
		ClientMain.init();
		new LoginWindow().showMe();
	}

}
