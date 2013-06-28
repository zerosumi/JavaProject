package chatsys.server;

import java.net.*;
import java.io.*;
import java.util.*;

import chatsys.dao.hibernate.IServiceDao;
import chatsys.dao.hibernate.ServiceDaoImHbn;
import chatsys.entity.OnlineUser;
import chatsys.entity.User;

public class ServerMainClass {
	public static IServiceDao userDao;
	public static Map<User, OnlineUser> userMap;
	public static Properties prop;

	public static void init() {
		prop = new Properties();
		try {
			prop.load(new FileInputStream("server_info.txt"));
		} catch (IOException e) {
			System.out.println("IOException!");
			e.printStackTrace();
		}
		userDao = new ServiceDaoImHbn();
		userMap = new HashMap<User, OnlineUser>();
	}

	public static void main(String[] args) {
		init();
		ServerSocket ss = null;
		Socket s = null;
		try {
			ss = new ServerSocket(Integer.parseInt(prop
					.getProperty("ServerPort")));
			while (true) {
				s = ss.accept();
				new ServerThread(s).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
