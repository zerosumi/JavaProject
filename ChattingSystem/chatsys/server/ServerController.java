package chatsys.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

import chatsys.dao.hibernate.IServiceDao;
import chatsys.entity.*;


public class ServerController {
	private User user;
	private Socket s;
	private IServiceDao dao;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private OnlineUser onlineUser;
	
	public ServerController(Socket s) {
		super();
		dao=ServerMainClass.userDao;
		this.s = s;
	}
	 
	public void handle() throws Exception {
		ois=new ObjectInputStream(s.getInputStream());
		oos=new ObjectOutputStream(s.getOutputStream());
		onlineUser=new OnlineUser(ois,oos);
		while(true){
			Request req=(Request)ois.readObject();
			ois.read();
			RequestType type=req.getType();
			if(type.equals(RequestType.EXIT)){
				exitHandle();
				break;
			}else if(type.equals(RequestType.LOGIN)){
				loginHandle(req);
			}else if(type.equals(RequestType.REGISTER)){
				registerHandle();
			}else if(type.equals(RequestType.OFFLINE)){
				offlineHandle();
				break;
			}else if(type.equals(RequestType.CHANGEINFO)){
				changeInformationHandle();
			}else if(type.equals(RequestType.MODIFYPWD)){
				modifypasswdHandle(req);
			}else if(type.equals(RequestType.SENDMESSAGE)){
				sendMessageHandle(req);
			}else if(type.equals(RequestType.RECVFILE)){
				receiveFileHandle(req);
			}else if(type.equals(RequestType.SENDFILE)){
				sendFileHandle(req);
			}
		}
	}
	private void modifypasswdHandle(Request req) {
		Long id=Long.parseLong(req.getData("id"));
		String oldpwd=req.getData("oldpwd");
		String newpwd=req.getData("newpwd");
		Response res=new Response(RequestType.MODIFYPWD);
		try {
			dao.updatePwd(id, oldpwd, newpwd);
			res.setData(1);
			try {
				oos.writeObject(res);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (RuntimeException e) {
			try {
				oos.writeObject(res);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void changeInformationHandle() {
		try {
			User user=(User)ois.readObject();
			Response res=new Response(RequestType.CHANGEINFO);
			try {
				dao.updateUser(user);
				res.setData(1);
				oos.writeObject(res);
				oos.flush();
			} catch (RuntimeException e) {
				oos.writeObject(res);
				oos.flush();
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void exitHandle() {
		try {
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void sendFileHandle(Request req) {
//		try {
//			User u=(User)ois.readObject();
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
	}
	
	private void receiveFileHandle(Request req) {
		
	}
		
	
	private void sendMessageHandle(Request req) {
		Response res=new Response(RequestType.RECVMESSAGE);
		Message message=null;
//		try {
			message=(Message)req.getData();
//			message=(Message)ois.readObject();
//			message=req.g
			res.setData(message);
//		} catch (IOException e) {
//			e.printStackTrace();
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
		User to=message.getTo();
		if(to==null){
			sendToAllUser(res);
		}else{
			
			Response res1=new Response(RequestType.PRIVATECHAT);
			res1.setData(message);
			ObjectOutputStream o=null;
			Set<User>set=ServerMainClass.userMap.keySet();
			Iterator it=set.iterator();
			while(it.hasNext()){
				User u=(User)it.next();
				if(u.equals(to)){
					o=ServerMainClass.userMap.get(u).getOos();
					break;
				}				
			}
			try {
				o.writeObject(res1);
				o.flush();
			
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void offlineHandle() {
		try {
			ServerMainClass.userMap.remove(user);
			Response res=new Response(RequestType.OFFLINE);
			res.setData(user);
			sendToAllUser(res);
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void registerHandle() {
		User user=dao.addUser();
		try {
			oos.writeObject(user);
			System.out.println(user.getId()+":"+user.getName());
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loginHandle(Request req) {
		Long id=Long.parseLong((String)req.getData("id"));
		String pwd=(String)req.getData("pwd");
		user=dao.getUser(id,pwd);
		Response res;
		try {
			Set<User>users=ServerMainClass.userMap.keySet();
			Iterator iter=users.iterator();
			while(iter.hasNext()){
				User u=(User)iter.next();
				if(u.equals(user)){
					res=new Response(RequestType.ISONLINE);
					oos.writeObject(res);
					oos.flush();
					return;
				}				
			}
			res=new Response(RequestType.ONLINE);
			res.setData(user);
			oos.writeObject(res);
			oos.flush();

			if(user!=null){
				Set<User>set=ServerMainClass.userMap.keySet();
				oos.write(set.size());
				Iterator it=set.iterator();
				while(it.hasNext()){
					oos.writeObject(it.next());
				}
				oos.flush();
				sendToAllUser(res);
				ServerMainClass.userMap.put(user, onlineUser);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sendToAllUser(Response res){
		try {
			 Collection c= ServerMainClass.userMap.values();
			Iterator it=c.iterator();
			while(it.hasNext()){
				ObjectOutputStream o=((OnlineUser)it.next()).getOos();
				o.writeObject(res);
				o.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
