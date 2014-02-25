package network;

import java.io.Serializable;

public class GamePlayInformation implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Integer Id;
	private String  Name;
	private Integer CurrentRound;
	private Integer MaxRounds;
	private String  GamePlayersOverMaxGamePlayers;
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
	public String getGamePlayersOverMaxGamePlayers() {
		return GamePlayersOverMaxGamePlayers;
	}
	public void setGamePlayersOverMaxGamePlayers(
			String gamePlayersOverMaxGamePlayers) {
		GamePlayersOverMaxGamePlayers = gamePlayersOverMaxGamePlayers;
	}


}
