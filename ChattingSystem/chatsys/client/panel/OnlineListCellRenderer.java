package chatsys.client.panel;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class OnlineListCellRenderer extends JLabel implements ListCellRenderer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -676078005928362650L;

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (value instanceof String) {
			ImageIcon icon = new ImageIcon("/images/avater/" + value + ".jpeg");
			if (icon != null) {
				this.setIcon(icon);
				this.setDisplayedMnemonic(Integer.parseInt(value.toString()));
			} 
		}
		return this;
	}

}
