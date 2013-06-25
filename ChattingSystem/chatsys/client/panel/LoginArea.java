package chatsys.client.panel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class LoginArea extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4179381696327126471L;
	private JTextField name;
	private JPasswordField pwd;
	
	public LoginArea(){
		Border line = BorderFactory.createLineBorder(Color.BLACK);
		Border line2 = BorderFactory.createEmptyBorder(1,1,1,1);
		Border empty = BorderFactory.createEmptyBorder(5,5,5,5);
		Border inOut = BorderFactory.createCompoundBorder(line2, empty);
		inOut = BorderFactory.createCompoundBorder(inOut, line);
		
		JLabel userName = new JLabel("username");
		JLabel userPwd =  new JLabel("password");
		
		name = new JTextField(12);
		pwd = new JPasswordField(12);
		
		JPanel namePanel = new JPanel();
		JPanel pwdPanel = new JPanel();
	
		namePanel.add(userName);
		namePanel.add(name);
		
		pwdPanel.add(userPwd);
		pwdPanel.add(pwd);
		
		this.setBorder(inOut);
		this.add(namePanel);
		this.add(pwdPanel);
//		this.setSize(355, 106);
		this.setPreferredSize(new Dimension(355, 106));
	}

	public JTextField getNameField() {
		return name;
	}

	public JPasswordField getPwdField() {
		return pwd;
	}
	
}
