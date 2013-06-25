package chatsys.entity;
import java.io.Serializable;
import java.util.Properties;


public class Request implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3273417675566862766L;
	private RequestType type;
	private Properties parameters;
	private Serializable value;
	
	public Request(RequestType type){
		parameters=new Properties();
		this.type=type;
	}
	public void setData(String key,String value){
		this.parameters.setProperty(key,value);
	}
	public String getData(String key){
		return this.parameters.getProperty(key);
	}
	public Properties getAllParameters(){
		return this.parameters;
	}
	public RequestType getType(){
		return type;
	}
	public void setData(Serializable value){
		this.value=value;
	}
	public Serializable getData(){
		return value;
	}
}
