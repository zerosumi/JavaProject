package chatsys.client.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;

public class MessageReceivedArea extends JPanel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4271694060399838624L;
	private JTextPane textPane;

	public MessageReceivedArea(){
		Border line = BorderFactory.createLineBorder(new Color(74,133, 213));
		this.setLayout(new BorderLayout());
		this.setBorder(line);
		textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane scro = new JScrollPane(textPane);
		this.add(scro, BorderLayout.CENTER);	
		this.setPreferredSize(new Dimension(400,290));
	}

	public JTextPane getTextPane() {
		return textPane;
	}

}
