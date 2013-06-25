package chatsys.entity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class OnlineUser {
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	public ObjectInputStream getOis() {
		return ois;
	}
	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}
	public ObjectOutputStream getOos() {
		return oos;
	}
	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}
	public OnlineUser(ObjectInputStream ois, ObjectOutputStream oos) {
		super();
		this.ois = ois;
		this.oos = oos;
	}
	public OnlineUser() {
		super();
	}
}
