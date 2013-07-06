package chatsys.client.windows;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.Element;

import chatsys.client.ClientMain;
import chatsys.client.ClientThread;
import chatsys.client.panel.*;
import chatsys.entity.*;


public class ClientMainWindow extends JFrame implements ActionListener{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5284939423170036396L;
	private UserInfoPanel userInfoPanel;
	private MessageReceivedArea receivedmessageArea;
	private ToolsPanel toolsPanel;
	private MessageWritingArea sendingMessageArea;
	private SendButtonPanel sendButtonPanel;
	private PublicAnnoPanel publicInfoPanel;
	private UserListPanel userListPanel;
	private MessageHistoryArea recordArea;
	
	public ClientMainWindow() {
		super("chatting system");
		this.userInfoPanel = new UserInfoPanel(ClientMain.currentUser);
		this.receivedmessageArea = new MessageReceivedArea();
		this.toolsPanel = new ToolsPanel();
		this.sendingMessageArea=new MessageWritingArea();
		this.sendButtonPanel = new SendButtonPanel();
		this.publicInfoPanel = new PublicAnnoPanel();
		this.userListPanel=new UserListPanel();
		this.recordArea=new MessageHistoryArea();
		new ClientThread(receivedmessageArea.getTextPane(),recordArea.getTextPane(),
										publicInfoPanel.getTextpane(),userListPanel)
										.start();
		init();
		addHanderListener();
		sendingMessageArea.getTextPane().requestFocusInWindow();
	}

	public void init(){
		Container container = this.getContentPane();
		JPanel leftPanel = new JPanel();  
		JPanel rightPanel = new JPanel();    
		
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); 
		rightPanel.setLayout(new BorderLayout()); 
		
		leftPanel.add(userInfoPanel);
		leftPanel.add(receivedmessageArea);
		leftPanel.add(toolsPanel);
		leftPanel.add(sendingMessageArea);
		leftPanel.add(sendButtonPanel);
 		
		rightPanel.add(publicInfoPanel, BorderLayout.NORTH);
		rightPanel.add(userListPanel, BorderLayout.CENTER);
		
		container.add(leftPanel, BorderLayout.CENTER);
		container.add(rightPanel, BorderLayout.EAST);
	}
	
	public void addHanderListener(){
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow(); 
			}
		});
		userInfoPanel.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1){
					new UserManage();
				}
			}
		});
		toolsPanel.getFontButton().addActionListener(this);
		toolsPanel.getColorButton().addActionListener(this);
		toolsPanel.getEmojiButton().addActionListener(this);
		sendButtonPanel.getRecordButton().addActionListener(this);
		sendButtonPanel.getSentButton().addActionListener(this);
		sendButtonPanel.getCloseButton().addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("History")){
			recordArea.setVisible(true);
			recordArea.setLocation(this.getX(),this.getY()+540);
		}else if(e.getActionCommand().equals("Send")){
			Document doc = sendingMessageArea.getTextPane().getDocument();
			Element root = doc.getDefaultRootElement();
			Message message=new Message(sendingMessageArea.getTextPane().getParagraphAttributes());
			message.setMessage(root);
			message.setFrom(ClientMain.currentUser);
			try {
				Request req = new Request(RequestType.SENDMESSAGE);
				req.setData(message);
				ClientMain.oos.writeObject(req);
				ClientMain.oos.flush();
				//ClientMain.oos.writeObject(new Request(RequestType.SENDMESSAGE));
				//ClientMain.oos.writeObject(message);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			sendingMessageArea.getTextPane().setText("");
		}else if(e.getActionCommand().equals("Close")){
			closeWindow();
		}else if(e.getActionCommand().equals("Font")){
			Font   font  =   FontChooserDialog.showDialog(this,null,new Font("Select font",Font.BOLD,12));
			sendingMessageArea.getTextPane().setFont(font);
			sendingMessageArea.getTextPane().requestFocusInWindow();
		}else if(e.getActionCommand().equals("Color")){
			Color c=JColorChooser.showDialog(this,"select color",Color.BLACK);
			sendingMessageArea.getTextPane().setForeground(c);
			sendingMessageArea.getTextPane().requestFocusInWindow();
		}else if(e.getActionCommand().equals("Emoji")){
			new FacePanel(sendingMessageArea.getTextPane()).setLocation(this.getX()+20,this.getY()+200);
			sendingMessageArea.getTextPane().requestFocusInWindow();
		}
	}
	
	public void showMe(){
		this.setSize(600,540);
		this.setLocation(250,100);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	private void closeWindow(){
		int select=JOptionPane.showConfirmDialog(ClientMainWindow.this,"Exit?",
				"Exit?",JOptionPane.YES_NO_OPTION);
		if(select==JOptionPane.YES_OPTION){
			try {
				Request req=new Request(RequestType.OFFLINE);
				ClientMain.oos.writeObject(req);
				ClientMain.oos.flush();
   				System.exit(0);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
