package chatsys.client.panel;


import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

public class FacePanel extends JDialog {
	
	private static final long serialVersionUID = 2161303778528731803L;
	private JTextPane textPane;

	public FacePanel(JTextPane textPane){		
		this.textPane = textPane;
		init();
	}
	
	private void init(){
		Border line= BorderFactory.createLineBorder(new Color(239, 250, 255));
		Border empty = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		Border border = BorderFactory.createCompoundBorder(empty, line);
		
		Border in = BorderFactory.createLineBorder(Color.BLACK);
		Border out = BorderFactory.createEmptyBorder(5, 5, 5, 5);
		Border title = BorderFactory.createCompoundBorder(in, out);
		
		this.setUndecorated(true);
		this.setBackground(Color.WHITE);
		
		Container container = this.getContentPane();
		JPanel emojiPanel = new JPanel();
		emojiPanel.setLayout(new GridLayout(8,16));
		
		
		emojiPanel.setBorder(title);
		JButton[] btn = new JButton[100];
		Listener listener = new Listener(this);
		for(int i = 0; i < 100; i++){
			btn[i] = new JButton();
			ImageIcon icon=new ImageIcon("/images/emoji/"+i+".gif");
			if(icon != null){
				btn[i].setIcon(icon);
				btn[i].addActionListener(listener);
				btn[i].setMargin(new Insets(1,1,1,1));
				btn[i].setBorder(border);
				emojiPanel.add(btn[i]);
			}
		}
		container.add(emojiPanel);
		pack();
		
		this.setVisible(true);
		this.addWindowFocusListener(new MyWindowFocusListener(this));
	}

	public class Listener implements ActionListener{
		private JDialog  dialog;
		
		public Listener(JDialog  dialog){
			this.dialog = dialog;
		}
		public void actionPerformed(ActionEvent e) {
			textPane.insertIcon(((JButton)e.getSource()).getIcon());
			dialog.dispose();
		}
	}

	public class MyWindowFocusListener extends WindowAdapter{
		private JDialog  dialog;
		
		public MyWindowFocusListener(JDialog  dialog){
			this.dialog = dialog;
		}
	
		public void windowLostFocus(WindowEvent e) {  //
			dialog.dispose();
		}
	}
	
}
