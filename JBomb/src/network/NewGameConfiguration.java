package network;

import java.io.Serializable;
import java.util.HashMap;

public class NewGameConfiguration implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private HashMap<String, Integer> GameModes;
	private HashMap<String, Integer> LinkageStrategies;
	private HashMap<String, Integer> Quizes;
	
	private Integer SelectedGameMode;
	private Integer SelectedLinkageStrategy;
	private Integer SelectedQuiz;
	private Integer SelectedTotalPlayers;
	
}
