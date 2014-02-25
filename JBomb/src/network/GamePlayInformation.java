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
	
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public Integer getCurrentRound() {
		return CurrentRound;
	}
	public void setCurrentRound(Integer currentRound) {
		CurrentRound = currentRound;
	}
	public Integer getMaxRounds() {
		return MaxRounds;
	}
	public void setMaxRounds(Integer maxRounds) {
		MaxRounds = maxRounds;
	}
	public Integer getTotalPlayers() {
		return TotalPlayers;
	}
	public void setTotalPlayers(Integer totalPlayers) {
		TotalPlayers = totalPlayers;
	}
	public Integer getMaxPlayers() {
		return MaxPlayers;
	}
	public void setMaxPlayers(Integer maxPlayers) {
		MaxPlayers = maxPlayers;
	}



}
