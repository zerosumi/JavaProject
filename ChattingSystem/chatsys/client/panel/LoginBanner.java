package chatsys.client.panel;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LoginBanner extends JPanel {
	
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 4739517247497535114L;

	public LoginBanner(){
		JLabel jl = new JLabel();
		ImageIcon icon = new ImageIcon("/images/image/Login.gif");
		if(icon != null){
			jl.setIcon(icon);
		}
		this.add(jl);
		this.setSize(353, 46);
	}

}
