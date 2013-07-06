package chatsys.server;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import chatsys.dao.hibernate.IServiceDao;
import chatsys.dao.hibernate.ServiceDaoImHbn;
import chatsys.entity.OnlineUser;
import chatsys.entity.User;

public class ServerMain {
	public static IServiceDao userDao;
	public static Map<User, OnlineUser> userMap;
	public static Properties prop;
	private ExecutorService executorService;
	private ServerSocket serverSocket = null;
	private Socket socket = null;
	
	public ServerMain() {
		userDao = new ServiceDaoImHbn();
		userMap = new HashMap<User, OnlineUser>();
		try {  
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime()  
                    .availableProcessors() * 50);
            prop = new Properties();
    		try {
    			prop.load(new FileInputStream("server_info.txt"));
    		} catch (IOException e) {
    			System.out.println("IOException!");
    			e.printStackTrace();
    		}
            serverSocket = new ServerSocket(Integer.parseInt(prop
					.getProperty("ServerPort")));  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	public void start() {
		try {
			while (true) {
				socket = serverSocket.accept();
				if (socket.isConnected())  
                    executorService.execute(new SocketTask(socket));
				//new ServerThread(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private final class SocketTask implements Runnable {  
        private Socket socket = null;  
        private InputThread in;  
        private OutputThread out;  
        private OutputThreadMap map;  
  
        public SocketTask(Socket socket) {  
            this.socket = socket;  
            map = OutputThreadMap.getInstance();  
        }  
  
        @Override  
        public void run() {  
            out = new OutputThread(socket, map);//  
            // 先实例化写消息线程,（把对应用户的写线程存入map缓存器中）  
            in = new InputThread(socket, out, map);// 再实例化读消息线程  
            out.setStart(true);  
            in.setStart(true);  
            in.start();  
            out.start();  
        }  
    }
	public static void main(String[] args) {
		new ServerMain().start();
	}
}
