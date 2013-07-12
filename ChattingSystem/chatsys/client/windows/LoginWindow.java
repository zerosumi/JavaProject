package chatsys.client.windows;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.EOFException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import chatsys.client.*;
import chatsys.client.panel.*;
import chatsys.entity.*;

public class LoginWindow extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3222664881804200200L;
	private LoginBanner top;
	private LoginArea loginArea;
	private LoginPanel login;
	
	public LoginWindow(){
		super("chatting system");
		Container container = this.getContentPane();
		container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		
		top=new LoginBanner();
		loginArea=new LoginArea();
		login=new LoginPanel();
		
		container.add(top);
		container.add(loginArea);
		container.add(login);
		addHanderListener();
	}
	
	private void addHanderListener(){
		login.getBtnCancel().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				loginArea.getNameField().setText("");
				loginArea.getPwdField().setText("");
			}
		});
		
		login.getBtnLogin().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				login(loginArea.getNameField().getText().trim(),new String(loginArea.getPwdField().getPassword()).trim());
			}
		});
		
		login.getBtnRegister().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Request req=new Request(RequestType.REGISTER);
				try {
					ClientMain.oos.writeObject(req);
					//ClientMain.oos.write(1);
					ClientMain.oos.flush();
					User user=(User)ClientMain.ois.readObject();
					if(user!=null){
						JOptionPane.showMessageDialog(null,"Register Success\nYour id : "+user.getId()+"\nYour Pwd : "+user.getPwd());
					}
					loginArea.getNameField().requestFocusInWindow();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				try {
					Request req=new Request(RequestType.EXIT);
					ClientMain.oos.writeObject(req);
					ClientMain.oos.flush();
					System.exit(0);		
				}catch (EOFException e2) {
					
				}catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}
	
	private void login(String id,String pwd){
		if(id.equals("")){
			JOptionPane.showMessageDialog(null,"Please input username");
			loginArea.getNameField().requestFocusInWindow();
		}else if(pwd.equals("") ){
			JOptionPane.showMessageDialog(null,"Please input password");
			loginArea.getPwdField().requestFocusInWindow();
		}else{	
			try {
				Request req=new Request(RequestType.LOGIN);
				req.setData("id",id);
				req.setData("pwd",pwd);
				ClientMain.oos.writeObject(req);
				//ClientMain.oos.write(1);
				ClientMain.oos.flush();
				Response res=(Response)ClientMain.ois.readObject();
				User user=(User)res.getData();
				if(res.getType().equals(ResponseType.ISONLINE)){
					JOptionPane.showMessageDialog(this,"This user is online!");
				}else if(user!=null&&res.getType().equals(ResponseType.ONLINE)){
					ClientMain.currentUser=user;
//					int n=ClientMain.ois.read();
//					for(int i=0;i<n;i++){
//						ClientMain.onlineUsers.add((User)ClientMain.ois.readObject());
//					}
					this.dispose();
					ClientMainWindow mainWindow = new ClientMainWindow();
					mainWindow.showMe();
					ClientMain.getClientInputThread().setMainWindow(mainWindow);
				}else{
					JOptionPane.showMessageDialog(this,"Incorrect password!");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void showMe(){
		this.setSize(new Dimension(360,230));
		this.setLocation(300,200);				
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
