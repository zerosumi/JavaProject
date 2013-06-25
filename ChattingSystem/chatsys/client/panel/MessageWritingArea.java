package chatsys.client.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;

public class MessageWritingArea extends JPanel {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5025934911109743207L;
	private JTextPane textPane;
	
	public MessageWritingArea(){
		Border line = BorderFactory.createLineBorder(new Color(74,133, 213));
		this.setLayout(new BorderLayout());
		this.setBorder(line);
		textPane = new JTextPane();
		JScrollPane scro = new JScrollPane(textPane);
		this.add(scro, BorderLayout.CENTER);	
		this.setPreferredSize(new Dimension(400,100));
	}

	public JTextPane getTextPane() {
		return textPane;
	}
	
}
