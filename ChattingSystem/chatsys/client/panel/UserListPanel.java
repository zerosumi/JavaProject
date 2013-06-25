package chatsys.client.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.tree.TreePath;

import chatsys.client.ClientMain;
import chatsys.entity.User;


public class UserListPanel extends JPanel {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 260296348773860878L;
	private JTree tree ;
	private OnlineTreeModel m;
	
	public UserListPanel() {
		Border line = BorderFactory.createLineBorder(new Color(74,133, 213));
		this.setLayout(new BorderLayout());
		this.setBorder(line);
		m=new OnlineTreeModel(ClientMain.onlineUsers);
		tree= new JTree(m);
		tree.setCellRenderer(new OnlineTreeCellRenderer());
		tree.addMouseListener(new MouseAdapter(){
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(e.getButton()==MouseEvent.BUTTON3){
					TreePath pt=tree.getPathForLocation(e.getX(), e.getY());
					if(pt!=null){
						Object obj=pt.getLastPathComponent();
						if (obj instanceof User) {
							User user = (User) obj;
							new RightButtonMenu(user).show(tree, e.getX(), e.getY());
						}			
					}
				}
			}
		});
		JScrollPane jsp=new JScrollPane(tree); 
		this.add(jsp);
	}

	public void freash(List<User> onlineUsers){
		m.setRoot(onlineUsers);
		SwingUtilities.updateComponentTreeUI(tree);
		
	}
}
