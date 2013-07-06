package chatsys.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import chatsys.dao.hibernate.IServiceDao;
import chatsys.entity.Message;
import chatsys.entity.OnlineUser;
import chatsys.entity.Request;
import chatsys.entity.RequestType;
import chatsys.entity.Response;
import chatsys.entity.ResponseType;
import chatsys.entity.User;

public class InputThread extends Thread { 
	private User user;
	private OnlineUser onlineUser;
    private Socket socket;
    private OutputThread out;
    private OutputThreadMap map;
    private ObjectInputStream ois;
    private IServiceDao dao;
    private boolean isStart = true;
  
    public InputThread(Socket socket, OutputThread out, OutputThreadMap map) {  
        this.socket = socket;  
        this.out = out;  
        this.map = map;
        dao=ServerMain.userDao;
        try {  
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    public void setStart(boolean isStart) { 
        this.isStart = isStart;  
    }
    @Override  
    public void run() {  
        try {
            while (isStart) {  
                // 读取消息  
                readMessage();  
            }  
            if (ois != null)  
                ois.close();  
            if (socket != null)  
                socket.close();  
        } catch (ClassNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
  
    }
    public void readMessage() throws IOException, ClassNotFoundException {  
        Object readObject = ois.readObject();// 从流中读取对象  
        //UserDao dao = UserDaoFactory.getInstance();// 通过dao模式管理后台  
        if (readObject != null && readObject instanceof Request) {  
            Request request = (Request) readObject;// 转换成传输对象  
            switch (request.getType()) {
            case EXIT:
				exitHandle();
				break;
            case LOGIN:
				loginHandle(request);
				break;
            case REGISTER:
				registerHandle();
				break;
            case OFFLINE:
				offlineHandle();
				break;
            case CHANGEINFO:
				changeInformationHandle();
				break;
            case MODIFYPWD:
				modifypasswdHandle(request);
				break;
            case SENDMESSAGE:
				sendMessageHandle(request);
				break;
            case RECVFILE:
				receiveFileHandle(request);
				break;
            case SENDFILE:
				sendFileHandle(request);
				break;
            }
//            case REGISTER:// 如果用户是注册  
////                User registerUser = (User) request.getObject();  
////                int registerResult = dao.register(registerUser);  
////                System.out.println(MyDate.getDateCN() + " 新用户注册:"  
////                        + registerResult);  
////                // 给用户回复消息  
////                TranObject<User> register2TranObject = new TranObject<User>(  
////                        TranObjectType.REGISTER);  
////                User register2user = new User();  
////                register2user.setId(registerResult);  
////                register2TranObject.setObject(register2user);  
////                out.setMessage(register2TranObject);  
////                break;
//                //
//        		User user=dao.addUser();
//        		try {
//        			out.setMessage(user);
////        			oos.writeObject(user);
////        			System.out.println(user.getId()+":"+user.getName());
////        			oos.flush();
//        		} catch (IOException e) {
//        			e.printStackTrace();
//        		}
//            case LOGIN:  
//                User loginUser = (User) request.getObject();  
//                ArrayList<User> list = dao.login(loginUser);  
//                TranObject<ArrayList<User>> login2Object = new TranObject<ArrayList<User>>(  
//                        TranObjectType.LOGIN);  
//                if (list != null) {// 如果登录成功  
//                    TranObject<User> onObject = new TranObject<User>(  
//                            TranObjectType.LOGIN);  
//                    User login2User = new User();  
//                    login2User.setId(loginUser.getId());  
//                    onObject.setObject(login2User);  
//                    for (OutputThread onOut : map.getAll()) {  
//                        onOut.setMessage(onObject);// 广播一下用户上线  
//                    }  
//                    map.add(loginUser.getId(), out);// 先广播，再把对应用户id的写线程存入map中，以便转发消息时调用  
//                    login2Object.setObject(list);// 把好友列表加入回复的对象中  
//                } else {  
//                    login2Object.setObject(null);  
//                }  
//                out.setMessage(login2Object);// 同时把登录信息回复给用户  
//  
//                System.out.println(MyDate.getDateCN() + " 用户："  
//                        + loginUser.getId() + " 上线了");  
//                break;  
//            case LOGOUT:// 如果是退出，更新数据库在线状态，同时群发告诉所有在线用户  
//                User logoutUser = (User) request.getObject();  
//                int offId = logoutUser.getId();  
//                System.out  
//                        .println(MyDate.getDateCN() + " 用户：" + offId + " 下线了");  
//                dao.logout(offId);  
//                isStart = false;// 结束自己的读循环  
//                map.remove(offId);// 从缓存的线程中移除  
//                out.setMessage(null);// 先要设置一个空消息去唤醒写线程  
//                out.setStart(false);// 再结束写线程循环  
//  
//                TranObject<User> offObject = new TranObject<User>(  
//                        TranObjectType.LOGOUT);  
//                User logout2User = new User();  
//                logout2User.setId(logoutUser.getId());  
//                offObject.setObject(logout2User);  
//                for (OutputThread offOut : map.getAll()) {// 广播用户下线消息  
//                    offOut.setMessage(offObject);  
//                }  
//                break;  
//            case MESSAGE:// 如果是转发消息（可添加群发）  
//                // 获取消息中要转发的对象id，然后获取缓存的该对象的写线程  
//                int id2 = request.getToUser();  
//                OutputThread toOut = map.getById(id2);  
//                if (toOut != null) {// 如果用户在线  
//                    toOut.setMessage(request);  
//                } else {// 如果为空，说明用户已经下线,回复用户  
//                    TextMessage text = new TextMessage();  
//                    text.setMessage("亲！对方不在线哦，您的消息将暂时保存在服务器");  
//                    TranObject<TextMessage> offText = new TranObject<TextMessage>(  
//                            TranObjectType.MESSAGE);  
//                    offText.setObject(text);  
//                    offText.setFromUser(0);  
//                    out.setMessage(offText);  
//                }  
//                break;  
//            case REFRESH:  
//                List<User> refreshList = dao.refresh(request  
//                        .getFromUser());  
//                TranObject<List<User>> refreshO = new TranObject<List<User>>(  
//                        TranObjectType.REFRESH);  
//                refreshO.setObject(refreshList);  
//                out.setMessage(refreshO);  
//                break;  
//            default:  
//                break;  
//            }  
        }  
    }
	private void modifypasswdHandle(Request req) throws IOException {
		Long id=Long.parseLong(req.getData("id"));
		String oldpwd=req.getData("oldpwd");
		String newpwd=req.getData("newpwd");
		Response res=new Response(ResponseType.MODIFYPWD);
		try {
			dao.updatePwd(id, oldpwd, newpwd);
			res.setData(1);
			out.setMessage(res);
			//oos.writeObject(res);
		} catch (RuntimeException e) {
			out.setMessage(res);
			//oos.writeObject(res);
		}
	}

	private void changeInformationHandle() {
		try {
			User user=(User)ois.readObject();
			Response res=new Response(ResponseType.CHANGEINFO);
			try {
				dao.updateUser(user);
				res.setData(1);
				out.setMessage(res);
				//oos.writeObject(res);
				//oos.flush();
			} catch (RuntimeException e) {
				out.setMessage(res);
				//oos.writeObject(res);
				//oos.flush();
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
			socket.close();
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
		Response res=new Response(ResponseType.RECVMESSAGE);
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
			
			Response res1=new Response(ResponseType.PRIVATECHAT);
			res1.setData(message);
			ObjectOutputStream o=null;
			Set<User>set=ServerMain.userMap.keySet();
			Iterator it=set.iterator();
			while(it.hasNext()){
				User u=(User)it.next();
				if(u.equals(to)){
					o=ServerMain.userMap.get(u).getOos();
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
			ServerMain.userMap.remove(user);
			map.remove(user);
			Response res=new Response(ResponseType.OFFLINE);
			res.setData(user);
			sendToAllUser(res);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void registerHandle() throws IOException {
		//will improve later
		User user=dao.addUser();
		Response res = new Response(ResponseType.REGISTERSUCCESS);
		res.setData(user);
		out.setMessage(res);
		//oos.writeObject(user);
		//System.out.println(user.getId()+":"+user.getName());
		//oos.flush();
	}
	
	private void loginHandle(Request req) throws IOException {
		Long id=Long.parseLong((String)req.getData("id"));
		String pwd=(String)req.getData("pwd");
		user=dao.getUser(id,pwd);
		Response res;
		Set<User>users=ServerMain.userMap.keySet();
		Iterator iter=users.iterator();
		while(iter.hasNext()){
			User u=(User)iter.next();
			if(u.equals(user)){
				res=new Response(ResponseType.ISONLINE);
				out.setMessage(res);
				//oos.writeObject(res);
				//oos.flush();
				return;
			}				
		}
		res=new Response(ResponseType.ONLINE);
		res.setData(user);
		out.setMessage(res);
		//oos.writeObject(res);
		//oos.flush();

		if(user!=null){
			Set<User>set=ServerMain.userMap.keySet();
			//oos.write(set.size());
			Iterator<User> it=set.iterator();
			while(it.hasNext()){
				Response res1=new Response(ResponseType.OFFLINE);
				out.setMessage(res1);
				//oos.writeObject(it.next());
			}
			//oos.flush();
			sendToAllUser(res);
			ServerMain.userMap.put(user,onlineUser);
			map.add(user, out);
		}
	}

	private void sendToAllUser(Response res){
		try {
			Collection c= ServerMain.userMap.values();
			Iterator it=c.iterator();
			while(it.hasNext()){
				ObjectOutputStream o=((OnlineUser)it.next()).getOos();
				o.writeObject(res);
				o.flush();
				o.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
