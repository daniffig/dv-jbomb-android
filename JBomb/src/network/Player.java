package network;

import java.io.Serializable;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer UID;
	private String Name;
	
	public Player()
	{
		super();
	}
	
	public Player(Integer uid, String n)
	{
		super();
		this.UID = uid;
		this.Name = n;
	}
	
	public Integer getUID() {
		return UID;
	}
	public void setUID(Integer uID) {
		UID = uID;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	
	
}
