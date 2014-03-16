package network;

import java.io.Serializable;

public class GameInformation  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer UID;
	private String  Name;
	private String  Mode;
	private Integer TotalPlayers;
	private Integer MaxPlayers;
	
	
	public Integer getMaxPlayers() {
		return MaxPlayers;
	}
	public String getMode() {
		return Mode;
	}
	public String getName() {
		return Name;
	}
	public Integer getTotalPlayers() {
		return TotalPlayers;
	}
	public Integer getUID() {
		return UID;
	}
	public void setMaxPlayers(Integer maxPlayers) {
		MaxPlayers = maxPlayers;
	}
	public void setMode(String mode) {
		Mode = mode;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setTotalPlayers(Integer totalPlayers) {
		TotalPlayers = totalPlayers;
	}
	public void setUID(Integer uID) {
		UID = uID;
	}

	
}
