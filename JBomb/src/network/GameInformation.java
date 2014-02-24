package network;

import java.io.Serializable;

public class GameInformation  implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String Name;
	private String Mode;
	private String GamePlayersOverMaxGamePlayers;
	
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getMode() {
		return Mode;
	}
	public void setMode(String mode) {
		Mode = mode;
	}
    public String getGamePlayersOverMaxGamePlayers() {
		return GamePlayersOverMaxGamePlayers;
	}
	public void setGamePlayersOverMaxGamePlayers(
			String gamePlayersOverMaxGamePlayers) {
		GamePlayersOverMaxGamePlayers = gamePlayersOverMaxGamePlayers;
	}
	
}
