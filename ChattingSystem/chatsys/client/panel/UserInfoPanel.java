package chatsys.client.panel;

import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.BorderFactory;

import chatsys.entity.*;


public class UserInfoPanel extends JPanel{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1064985747455206939L;
	private User user;
	public UserInfoPanel(User user){
		this.user=user;
		Border line = BorderFactory.createLineBorder(new Color(74,133, 213));
		this.setBackground(new Color(244, 249, 254));
		this.setLayout(new FlowLayout(FlowLayout.LEADING,0,10));
		this.setBorder(line);
		this.add(getLabel());
	}
	
	private JLabel getLabel(){
		ImageIcon icon = user.getSmallImageIcon();
		String memo="";
		if(user.getMemo()!=null){
			memo=user.getMemo();
		}
		String str=user.getName()+" ("+user.getId()+")"+" : "+memo;
		JLabel label=new JLabel(str,icon,SwingConstants.LEFT);
		label.setForeground(Color.RED);
		return label;
	}
}
