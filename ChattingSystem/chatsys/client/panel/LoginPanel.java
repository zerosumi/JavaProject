package chatsys.client.panel;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoginPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8246755301503944008L;
	private JButton btnRegister;
	private JButton btnLoad;
	private JButton btnCancer;
	
	public LoginPanel(){
		btnRegister = new JButton("Reg");
		btnLoad = new JButton("Login");
		btnCancer = new JButton("Cancel");
		JLabel label = new JLabel("                         ");
	
		this.setLayout(new FlowLayout(FlowLayout.LEADING, 10 ,10));
		this.add(btnRegister);
		this.add(label);
		this.add(btnLoad);
		this.add(btnCancer);
	}

	public JButton getBtnCancer() {
		return btnCancer;
	}

	public JButton getBtnLoad() {
		return btnLoad;
	}

	public JButton getBtnRegister() {
		return btnRegister;
	}

}
