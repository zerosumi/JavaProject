package chatsys.client.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class PublicAnnoPanel extends JPanel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6015364676328769254L;
	private JTextPane textpane;
	
	public PublicAnnoPanel(){
		Border line = BorderFactory.createLineBorder(new Color(74,133, 213));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel panelTitle = new JPanel();
		panelTitle.setBackground(new Color(244, 249, 254));
		panelTitle.setLayout(new FlowLayout(FlowLayout.LEADING,2,0));
		ImageIcon icon=new ImageIcon("images/image/message.gif");
		JLabel label = new JLabel("           Announcement         ",icon,SwingConstants .CENTER);
		panelTitle.add(label);
		panelTitle.setBorder(line);
		panelTitle.setPreferredSize(new Dimension(200,25));
		
		textpane = new JTextPane();
		textpane.setText("\nBeta Version\n For test only!");
		textpane.setEditable(false);
		textpane.setBorder(line);
		this.setPreferredSize(new Dimension(200,150));
		this.add(panelTitle);
		this.add(textpane);
	}

	public JTextPane getTextpane() {
		return textpane;
	}

}
