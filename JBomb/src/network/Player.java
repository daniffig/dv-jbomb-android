package network;

import java.io.Serializable;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer UID;
	private String Name;
	private Integer Points;
	
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
	
	public String getName() {
		return Name;
	}
	public Integer getPoints() {
		return Points;
	}
	public Integer getUID() {
		return UID;
	}
	public void setName(String name) {
		Name = name;
	}

	public void setPoints(Integer points) {
		Points = points;
	}

	public void setUID(Integer uID) {
		UID = uID;
	}
	
	
}
