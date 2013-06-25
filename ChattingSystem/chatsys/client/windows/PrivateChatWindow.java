package chatsys.client.windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import chatsys.client.ClientMain;
import chatsys.client.panel.FacePanel;
import chatsys.client.panel.FriendInformation;
import chatsys.client.panel.MessageReceivedArea;
import chatsys.client.panel.MessageHistoryArea;
import chatsys.client.panel.MessageWritingArea;
import chatsys.client.panel.FontChooserDialog;
import chatsys.client.panel.SendButtonPanel;
import chatsys.client.panel.ToolsPanel;
import chatsys.client.panel.UserInfoPanel;
import chatsys.entity.Message;
import chatsys.entity.Request;
import chatsys.entity.RequestType;
import chatsys.entity.User;


public class PrivateChatWindow extends JFrame implements ActionListener{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5245137911601983309L;
	private UserInfoPanel userInfoPanel;
	private MessageReceivedArea receivedmessageArea;
	private ToolsPanel toolsPanel;
	private MessageWritingArea sendingMessageArea;
	private SendButtonPanel sendButtonPanel;
	private MessageHistoryArea historyArea;
	private User user;
	
	public PrivateChatWindow(User user) {
		super("chatting with"+user.getName());
		this.user=user;
		this.userInfoPanel = new UserInfoPanel(user);
		this.receivedmessageArea = new MessageReceivedArea();
		this.toolsPanel = new ToolsPanel();
		this.sendingMessageArea=new MessageWritingArea();
		this.sendButtonPanel = new SendButtonPanel();
		this.historyArea=new MessageHistoryArea();
		init();
		addHanderListener();
		sendingMessageArea.getTextPane().requestFocusInWindow();
	}

	public void init(){
		Container container = this.getContentPane();
		JPanel leftPanel = new JPanel();  
		leftPanel.setBorder(BorderFactory.createLineBorder(new Color(216,239, 254),5));
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); 
		
		leftPanel.add(userInfoPanel);
		leftPanel.add(receivedmessageArea);
		leftPanel.add(toolsPanel);
		leftPanel.add(sendingMessageArea);
		leftPanel.add(sendButtonPanel);
 		
		container.add(leftPanel, BorderLayout.CENTER);
	}
	
	public void addHanderListener(){
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		userInfoPanel.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getButton()==MouseEvent.BUTTON1){
					new FriendInformation(user).showMe();
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
			historyArea.setVisible(true);
			historyArea.setLocation(this.getX(),this.getY()+540);
		}else if(e.getActionCommand().equals("Send")){
			Document doc = sendingMessageArea.getTextPane().getDocument();
			Element root = doc.getDefaultRootElement();
			Message message=new Message(sendingMessageArea.getTextPane().getParagraphAttributes());
			message.setMessage(root);
			message.setFrom(ClientMain.currentUser);
			message.setTo(user);
			try {
				ClientMain.oos.writeObject(new Request(RequestType.SENDMESSAGE));
				ClientMain.oos.writeObject(message);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			sendingMessageArea.getTextPane().setText("");

			SimpleAttributeSet set=new SimpleAttributeSet();
			StyleConstants.setFontSize(set, 16);
			StyleConstants.setFontFamily(set,"Consolas");
			StyleConstants.setForeground(set, new Color(0,139,139));
			JTextPane jtp=receivedmessageArea.getTextPane();
			Calendar c = Calendar.getInstance();
			String time=c.get(Calendar.MONTH)+"/"+c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.YEAR)+" "+
					(c.get(Calendar.HOUR_OF_DAY)-5)+":"+c.get(Calendar.MINUTE)+":"+c.get(Calendar.SECOND);
			try {
				jtp.getDocument().insertString(jtp.getDocument().getLength(),
												ClientMain.currentUser.getName()+" "+time+"\n",
												set);

				message.analysisMessage(jtp);
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			
		}else if(e.getActionCommand().equals("Close")){
			dispose();
		}else if(e.getActionCommand().equals("Font")){
			Font   font  =   FontChooserDialog.showDialog(this,null,new Font("Select Font",Font.BOLD,12));
			sendingMessageArea.getTextPane().setFont(font);
			sendingMessageArea.getTextPane().requestFocusInWindow();
		}else if(e.getActionCommand().equals("Color")){
			Color c=JColorChooser.showDialog(this,"Select color",Color.BLACK);
			sendingMessageArea.getTextPane().setForeground(c);
			sendingMessageArea.getTextPane().requestFocusInWindow();
		}else if(e.getActionCommand().equals("Emoji")){
			new FacePanel(sendingMessageArea.getTextPane()).setLocation(this.getX()+20,this.getY()+200);
			sendingMessageArea.getTextPane().requestFocusInWindow();
		}
	}
	
	public void showMe(){
		this.setSize(400,540);
		this.setLocation(250,100);
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}

	public MessageReceivedArea getReceivedmessageArea() {
		return receivedmessageArea;
	}

	public void setReceivedmessageArea(MessageReceivedArea receivedmessageArea) {
		this.receivedmessageArea = receivedmessageArea;
	}
}
