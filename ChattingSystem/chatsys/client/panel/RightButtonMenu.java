package chatsys.client.panel;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import chatsys.client.*;
import chatsys.client.windows.PrivateChatWindow;
import chatsys.entity.*;

public class RightButtonMenu extends JPopupMenu implements ActionListener {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4498094794309111106L;
	private User user;
	private JMenuItem information;
	private JMenuItem individual;
	private JMenuItem sendFile;
	private JMenuItem shield;
	
	public RightButtonMenu(User user) throws HeadlessException {
		super();
		this.user = user;
		this.information = new JMenuItem("show personal info");
		this.individual = new JMenuItem("request private chat");
		this.shield=new JMenuItem("block this user");
		this.sendFile = new JMenuItem("send file to");
		init();
	}

	private void init(){
		this.add(information);
		this.add(individual);
		this.add(shield);
		this.add(sendFile);
		shield.addActionListener(this);
		information.addActionListener(this);
		individual.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==information) {
			new FriendInformation(user).showMe();
		} else if (e.getSource()==individual) {
			PrivateChatWindow indi=new PrivateChatWindow(user);
			ClientMain.privateChat.put(user.getId(), indi.getReceivedmessageArea().getTextPane());
			indi.showMe();
		} else if (e.getSource()==sendFile) {
			sendFile();
		}else{
			ClientMain.blackList.add(user.getId());
		}
	}

	private void sendFile(){
//		ObjectOutputStream oos=ClientMainClass.oos;
//		
//		try {
//			oos.writeObject(new Request(RequestType.sendFile));
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
