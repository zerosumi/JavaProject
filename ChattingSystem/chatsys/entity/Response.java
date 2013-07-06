package chatsys.entity;

import java.io.*;

public class Response implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7864926537127631729L;
	private ResponseType type;
	private Serializable value;
	
	public Response(ResponseType type){
		this.type=type;
	}
	public void setData(Serializable value){
		this.value=value;
	}
	public Serializable getData(){
		return value;
	}
	public ResponseType getType(){
		return type;
	}
}
