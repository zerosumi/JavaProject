package chatsys.entity;


import java.io.*;
import javax.swing.ImageIcon;

public class User implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4770382134970806508L;
	private Long id;
	private String name;
	private char sex;
	private String icon;
	private String memo;
	private String pwd;
	
	public User( String name, char sex, String icon, String memo, String pwd) {
		this.name = name;
		this.sex = sex;
		this.icon=icon;
		this.memo = memo;
		this.pwd = pwd;
	}
	
	public User(String name,String pwd){
		this.name=name;
		this.pwd=pwd;
	}
	
	public ImageIcon getImageIcon(){
		ImageIcon image=null;
		image=new ImageIcon("/images/avater/"+icon+".jpeg");
		return image;
	}
	
	public ImageIcon getSmallImageIcon(){
		ImageIcon image=null;
		image=new ImageIcon("/images/avater/small/"+icon+".gif");
		return image;
	}
	
	public User() {
		super();
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Long getId() {
		return id;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public char getSex() {
		return sex;
	}

	public void setSex(char sex) {
		this.sex = sex;
	}


	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}

