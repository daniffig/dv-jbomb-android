package network;

import java.io.Serializable;

public class GamePlayInformation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer Id;
	private String  Name;
	private Integer CurrentRound;
	private Integer MaxRounds;
	private Integer TotalPlayers;
	private Integer MaxPlayers;
	
	public Integer getCurrentRound() {
		return CurrentRound;
	}
	public Integer getId() {
		return Id;
	}
	public Integer getMaxPlayers() {
		return MaxPlayers;
	}
	public Integer getMaxRounds() {
		return MaxRounds;
	}
	public String getName() {
		return Name;
	}
	public Integer getTotalPlayers() {
		return TotalPlayers;
	}
	public void setCurrentRound(Integer currentRound) {
		CurrentRound = currentRound;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public void setMaxPlayers(Integer maxPlayers) {
		MaxPlayers = maxPlayers;
	}
	public void setMaxRounds(Integer maxRounds) {
		MaxRounds = maxRounds;
	}
	public void setName(String name) {
		Name = name;
	}
	public void setTotalPlayers(Integer totalPlayers) {
		TotalPlayers = totalPlayers;
	}



}
