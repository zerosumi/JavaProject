package chatsys.client.panel;

import javax.swing.tree.DefaultTreeModel;

import chatsys.entity.User;


import java.util.*;

public class OnlineTreeModel extends DefaultTreeModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8248140005111733137L;
	private Object root;
	
	public OnlineTreeModel(List<User> root) {
		super(null);
		this.root=root;
	}
	
	public Object getChild(Object parent, int index) {
		return ((List<?>)parent).get(index);
	}
	
	public int getChildCount(Object parent) {
		return ((List<?>)root).size();
	}
	
	public int getIndexOfChild(Object parent, Object child) {
		return ((List<?>)parent).indexOf(child);
	}

	public Object getRoot() {
		return root;
	}

	public void setRoot(Object root){
		this.root=root;
	}
	
	public boolean isLeaf(Object node) {
		return  node instanceof User;
	}
}
