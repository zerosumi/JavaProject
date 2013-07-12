package chatsys.client;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import chatsys.client.windows.ClientMainWindow;
import chatsys.client.windows.PrivateChatWindow;
import chatsys.entity.Message;
import chatsys.entity.Response;
import chatsys.entity.ResponseType;
import chatsys.entity.User;

public class ClientInputThread extends Thread {
	private Socket socket;
	private Response res;
	private boolean isStart = true;
	private ObjectInputStream ois;
	private ClientMainWindow mainWindow;
	private SimpleAttributeSet fontAttr;
	
//	private MessageListener messageListener;// 消息监听接口对象

	public ClientInputThread(Socket socket) {
		fontAttr=new SimpleAttributeSet();
		StyleConstants.setFontSize(fontAttr, 16);
		StyleConstants.setFontFamily(fontAttr,"Consolas");
		StyleConstants.setForeground(fontAttr, new Color(0,139,139));
		this.socket = socket;
		try {
			ois = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


//	public void setMessageListener(MessageListener messageListener) {
//		this.messageListener = messageListener;
//	}

	public void setMainWindow(ClientMainWindow main) {
		this.mainWindow = main;
	}
	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	@Override
	public void run() {
		try {
			while (isStart) {
				res = (Response) ois.readObject();
				// 每收到一条消息，就调用接口的方法，并传入该消息对象，外部在实现接口的方法时，就可以及时处理传入的消息对象了
				// 我不知道我有说明白没有
//				messageListener.Message(response);
				if(res!=null){
					ResponseType type=res.getType();
					if(type.equals(ResponseType.ONLINE)){
						onlineHandle(res);
					}else if(type.equals(ResponseType.OFFLINE)){
						offlineHandle(res);
					}else if(type.equals(ResponseType.CHANGEINFO)){
						changeInfoHandle(res);
					}else if(type.equals(ResponseType.MODIFYPWD)){
						modifyPWDHandle(res);
					}else if(type.equals(ResponseType.PUBLICMESSAGE)){
						receiveMessageHandle(res);
					}else if(type.equals(ResponseType.PRIVATEMESSAGE)){
						privateChatHandle(res);
					}else if(type.equals(ResponseType.RECVFILE)){
						receiveFileHandle(res);
					}else if(type.equals(ResponseType.PUBLICANNO)){
						publicAnnoHandle(res);
					}
				}
				
				
			}
			ois.close();
			if (socket != null)
				socket.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void modifyPWDHandle(Response res) {
		if(res.getData()!=null){
			JOptionPane.showMessageDialog(null, "Change Password Success");
		}else{
			JOptionPane.showMessageDialog(null, "Error! Retry Later");
		}
	}

	private void changeInfoHandle(Response res) {
		if(res.getData()!=null){
			JOptionPane.showMessageDialog(null, "Change Information Success");
		}else{
			JOptionPane.showMessageDialog(null, "Error! Retry Later");
		}
	}

	private void publicAnnoHandle(Response res) {
		String str=(String)res.getData();
		mainWindow.getPublicAnno().setText(str);
	}

	private void receiveFileHandle(Response res) {
		//not complete
	}

	private void privateChatHandle(Response res) {
		
		Message message=(Message)res.getData();
		User user=message.getFrom();
		if(!ClientMain.privateChat.containsKey(user.getId())){
			int flag=JOptionPane.showConfirmDialog(null,user.getName()+" want to have private chat with you, accept?","",JOptionPane.YES_NO_OPTION);
			if(flag==JOptionPane.NO_OPTION){
				return;
			}
			PrivateChatWindow prvt=new PrivateChatWindow(user);
			ClientMain.privateChat.put(user.getId(), prvt.getReceivedmessageArea().getTextPane());
			prvt.showMe();
		}
		
		JTextPane jtp=ClientMain.privateChat.get(user.getId());
		try {
			jtp.getDocument().insertString(jtp.getDocument().getLength(),
																	user.getName()+" "+message.getTime()+"\n",
																	fontAttr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		message.analysisMessage(jtp);
	}

	private void receiveMessageHandle(Response res) {
		Message message=(Message)res.getData();
		if(ClientMain.blackList.contains(message.getFrom().getId())){
			return;
		}
		try {
			mainWindow.getTextArea().getDocument().insertString(mainWindow.getTextArea().getDocument().getLength(),
																	message.getFrom().getName()+" "+message.getTime()+"\n",
																	fontAttr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		message.analysisMessage(mainWindow.getTextArea());
	}

	private void offlineHandle(Response res) {
		User user=(User)res.getData();
		ClientMain.onlineUsers.remove(user);
		mainWindow.getUserList().freash(ClientMain.onlineUsers);
		Calendar c = Calendar.getInstance();
		String time=c.get(Calendar.MONTH)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR)+" "+
				(c.get(Calendar.HOUR_OF_DAY)-5)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
		try {
			mainWindow.getTextArea().getDocument().insertString(mainWindow.getTextArea().getDocument().getLength(),
					user.getName()+" "+time+" logout\n",
					fontAttr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void onlineHandle(Response res) {
		User user=(User)res.getData();
		ClientMain.onlineUsers.add(user);
		mainWindow.getUserList().freash(ClientMain.onlineUsers);
		Calendar c = Calendar.getInstance();
		String time=c.get(Calendar.MONTH)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR)+" "+
				(c.get(Calendar.HOUR_OF_DAY)-5)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
		try {
			mainWindow.getTextArea().getDocument().insertString(mainWindow.getTextArea().getDocument().getLength(),
					user.getName()+" "+time+" login\n",
					fontAttr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	//
	// public interface MessageListener {
	// public void Message(Response res);
	// }
}
