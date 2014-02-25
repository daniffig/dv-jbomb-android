package core;

import java.util.ArrayList;
import java.util.List;

import com.example.jbomb.R;

public class GameClient {

	private static GameClient instance;
	
	public static int CORRECT_ANSWER = 31;
	public static int INCORRECT_ANSWER = 37;
	
	private List<Integer> players;
	
	public static GameClient getInstance() {
		
		if (GameClient.instance == null)
		{
			GameClient.instance = new GameClient();
			GameClient.instance.players = new ArrayList<Integer>();
			
			
			for (int i = (int)System.currentTimeMillis() % 4; i >= 0; i--) {
				
				System.out.println(i);
				GameClient.instance.players.add(i);
			}
		}
		
		return GameClient.instance;
	}
	
	public static void destroyInstance()
	{
		GameClient.instance = null;
	}
	
	public Integer getPlayersCount() {
		return GameClient.instance.players.size();
	}
	
	public List<Integer> getPlayers() {
		return GameClient.instance.players;
	}
	
	private Boolean hasTheBomb() {
		return true;
	}
	
	public String getQuizQuestion()
	{
		return "¿Quién es la mejor persona del planeta?";
	}
	
	public List<String> getQuizQuestionAnswers()
	{
		List<String> answers = new ArrayList<String>();
		
		answers.add("Federico Almendra");
		answers.add("Andrés Cimadamore");
		answers.add("Duilio Gervasio Ray");
		
		return answers;
	}
	
	public Boolean isCorrectQuizQuestionAnswer(int pos)
	{
		return pos == 0;
	}
	
	// MUY MUY MAL HACER ESTO, pero hay que sacarlo funcionando ya.
	public int getIdForPlayer(int i)
	{
		switch (i)
		{
		case 0:
			return R.id.InetPortText;
		case 1:
			return R.id.InetIPAddressText;
		case 2:
			return R.id.PlayerBottom;
		case 3:
			return R.id.PlayerLeft;
		}
		
		return -1;
	}
	
	public int getImageForPlayer(int i) {

		switch (i)
		{
		case 0:
			return R.id.PlayerTopImage;
		case 1:
			return R.id.PlayerRightImage;
		case 2:
			return R.id.PlayerBottomImage;
		case 3:
			return R.id.PlayerLeftImage;
		}
		
		return -1;
	}
}
