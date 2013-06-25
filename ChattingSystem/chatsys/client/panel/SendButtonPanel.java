package chatsys.client.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.*;

public class SendButtonPanel extends JPanel {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3264781582032175372L;
	JButton record;
	JButton sent;
	JButton close;
	
	public SendButtonPanel() {
		this.setBackground(new Color(74,133, 213));
		this.setLayout(new FlowLayout(FlowLayout.LEADING,2,3));
		record=new JButton("History");
		sent=new JButton("Send");
		close=new JButton("Close");
		JLabel label = new JLabel();
		label.setText("                                          ");
		this.add(record);
		this.add(label);
		this.add(sent);
		this.add(close);
		this.setPreferredSize(new Dimension(Short.MAX_VALUE,35));
	}

	public JButton getCloseButton() {
		return close;
	}

	public JButton getRecordButton() {
		return record;
	}

	public JButton getSentButton() {
		return sent;
	}
	
}
