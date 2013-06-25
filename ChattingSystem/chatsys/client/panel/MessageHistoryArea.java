package chatsys.client.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.Border;

public class MessageHistoryArea extends JDialog{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7774288380310786185L;
	private JTextPane textPane;

	public MessageHistoryArea(){
		Container container = this.getContentPane();
		Border line = BorderFactory.createLineBorder(new Color(74,133, 213));
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		pan.setBorder(line);
		textPane = new JTextPane();
		textPane.setEditable(false);
		JScrollPane scro = new JScrollPane(textPane);
		pan.add(scro, BorderLayout.CENTER);	
		container.add(pan);
		this.setSize(400, 200);
		this.setVisible(false);
	}
	
	public JTextPane getTextPane() {
		return textPane;
	}
}
