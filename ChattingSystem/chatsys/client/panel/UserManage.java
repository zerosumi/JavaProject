package chatsys.client.panel;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import chatsys.client.ClientMain;
import chatsys.entity.Request;
import chatsys.entity.RequestType;
import chatsys.entity.User;


public class UserManage extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 989307564159163655L;
	private UserInformationPanel info;
	private ModifyPWDPanel pwd;
	
	public UserManage() {
		Container container = this.getContentPane();
		info=new UserInformationPanel(ClientMain.currentUser);
		pwd=new ModifyPWDPanel();
		info.setPreferredSize(new Dimension(400,300));
		JTabbedPane pane = new JTabbedPane(JTabbedPane.TOP);
		pane.addTab("personal info", info);
		pane.addTab("password", pwd);
		container.add(pane);
		this.setSize(400, 300);
		this.setLocation(200, 120);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		info.getCancel().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		info.getSure().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				User user=ClientMain.currentUser;
				user.setName(info.getUserName().getText().trim());
				if(info.getMale().isSelected()){
					user.setSex('M');
				}else{
					user.setSex('F');
				}
				user.setIcon(String.valueOf(info.getFace().getSelectedIndex())+1);
				user.setMemo(info.getMemo().getText().trim());
				Request req=new Request(RequestType.CHANGEINFO);
				try {
					ClientMain.oos.writeObject(req);
					ClientMain.oos.writeObject(user);
					ClientMain.oos.flush();
					dispose();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		pwd.getCancer().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		pwd.getSure().addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(new String(pwd.getOldpwd().getPassword()).trim().equals("")){
					JOptionPane.showMessageDialog(null,"input old password");
					pwd.getOldpwd().requestFocusInWindow();
				}if(new String(pwd.getNewpwd().getPassword()).trim().equals("")){
					JOptionPane.showMessageDialog(null,"new password");
					pwd.getNewpwd().requestFocusInWindow();
				}else if(new String(pwd.getAgainpwd().getPassword()).trim().equals("")){
					JOptionPane.showMessageDialog(null,"confirm");
					pwd.getAgainpwd().requestFocusInWindow();
				}else{
					if(new String(pwd.getNewpwd().getPassword()).equals(new String(pwd.getAgainpwd().getPassword()))){
						Request req=new Request(RequestType.MODIFYPWD);
						req.setData("id",String.valueOf(ClientMain.currentUser.getId()));
						req.setData("oldpwd", new String(pwd.getOldpwd().getPassword()));
						req.setData("newpwd", new String(pwd.getNewpwd().getPassword()));
						try {
							ClientMain.oos.writeObject(req);
							ClientMain.oos.write(1);
							ClientMain.oos.flush();
							dispose();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}else{
						JOptionPane.showMessageDialog(null, "mismatch");
						pwd.getNewpwd().requestFocusInWindow();
					}	
				}
			}
		});
	}
}
