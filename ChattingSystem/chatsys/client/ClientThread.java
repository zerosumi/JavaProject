package chatsys.client;

import java.awt.Color;
import java.io.EOFException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import chatsys.client.panel.UserListPanel;
import chatsys.client.windows.PrivateChatWindow;
import chatsys.entity.Message;
import chatsys.entity.RequestType;
import chatsys.entity.Response;
import chatsys.entity.ResponseType;
import chatsys.entity.User;



public class ClientThread extends Thread{
	

	private JTextPane textArea;
	private JTextPane history;
	private JTextPane publicAnno;
	private UserListPanel userList;
	private SimpleAttributeSet fontAttr;
	
	private Socket s;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ClientThread(JTextPane textArea, JTextPane history, JTextPane publicAnno, UserListPanel userList) {
		this.textArea = textArea;
		this.history = history;
		this.publicAnno = publicAnno;
		this.userList = userList;
		
		s=ClientMain.socket;
		ois=ClientMain.ois;
		oos=ClientMain.oos;
		
		fontAttr=new SimpleAttributeSet();
		StyleConstants.setFontSize(fontAttr, 16);
		StyleConstants.setFontFamily(fontAttr,"Consolas");
		StyleConstants.setForeground(fontAttr, new Color(0,139,139));
	}

	@Override
	public void run() {
		while(s.isConnected()){
			try {
				//Thread.sleep(1000);
				Response res=(Response)ois.readObject();
				//ois.read();
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
					}else if(type.equals(ResponseType.RECVMESSAGE)){
						receiveMessageHandle(res);
					}else if(type.equals(ResponseType.PRIVATECHAT)){
						privateChatHandle(res);
					}else if(type.equals(ResponseType.RECVFILE)){
						receiveFileHandle(res);
					}else if(type.equals(ResponseType.PUBLICANNO)){
						publicAnnoHandle(res);
					}
				}
			} catch (EOFException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
		publicAnno.setText(str);
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
			textArea.getDocument().insertString(textArea.getDocument().getLength(),
																	message.getFrom().getName()+" "+message.getTime()+"\n",
																	fontAttr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		message.analysisMessage(textArea);
	}

	private void offlineHandle(Response res) {
		User user=(User)res.getData();
		ClientMain.onlineUsers.remove(user);
		userList.freash(ClientMain.onlineUsers);
		Calendar c = Calendar.getInstance();
		String time=c.get(Calendar.MONTH)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR)+" "+
				(c.get(Calendar.HOUR_OF_DAY)-5)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
		try {
			textArea.getDocument().insertString(textArea.getDocument().getLength(),
					user.getName()+" "+time+" logout\n",
					fontAttr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void onlineHandle(Response res) {
		User user=(User)res.getData();
		ClientMain.onlineUsers.add(user);
		userList.freash(ClientMain.onlineUsers);
		Calendar c = Calendar.getInstance();
		String time=c.get(Calendar.MONTH)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR)+" "+
				(c.get(Calendar.HOUR_OF_DAY)-5)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
		try {
			textArea.getDocument().insertString(textArea.getDocument().getLength(),
					user.getName()+" "+time+" login\n",
					fontAttr);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
}
