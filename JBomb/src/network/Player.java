package network;

import java.io.Serializable;
import java.util.Vector;

public class Player implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer UID;
	private String Name;
	private Vector<Integer> RoundPoints = new Vector<Integer>();
	private Integer GeneralPoints = 0;
	
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
	
	public Player(Integer uid, String Name, Vector<Integer> RoundPoints, Integer GeneralPoints)
	{
		super();
		this.setUID(uid);
		this.setName(Name);
		this.setRoundPoints(RoundPoints);
		this.setGeneralPoints(GeneralPoints);
	}
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Integer getUID() {
		return UID;
	}
	public void setUID(Integer uID) {
		UID = uID;
	}

	public Vector<Integer> getRoundPoints() {
		return RoundPoints;
	}

	public void setRoundPoints(Vector<Integer> roundPoints) {
		RoundPoints = roundPoints;
	}

	public Integer getGeneralPoints() {
		return GeneralPoints;
	}

	public void setGeneralPoints(Integer generalPoints) {
		GeneralPoints = generalPoints;
	}
	
	public Integer getCurrentRoundPoints(){
		return RoundPoints.lastElement();
	}
	
	public Integer getPointsFromRound(Integer Round){
		return RoundPoints.get(Round);
	}
	
	
}
