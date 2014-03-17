package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.Player;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.jbomb.R;

@SuppressLint("UseSparseArrays")
public class GameClient {

	private static GameClient instance;
	
	public static int CORRECT_ANSWER = 31;
	public static int INCORRECT_ANSWER = 37;
	
//	public Vector<Integer> adjacentPlayersUIDs = new Vector<Integer>();	
	public Map<Integer, Player> adjacentPlayers = new HashMap<Integer, Player>();

	public static void destroyInstance()
	{
		GameClient.instance = null;
	}
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
	
	public static void printNotification(String notification)
	{
		Log.i(String.format("[%s]", GameClient.getInstance().myPlayer.getName()), notification);
	}
	public int currentPlayers = 0;
	
	public int maxPlayers = 1;
	
	public String myPlayerName;

	public Player myPlayer;

	public Player getMyPlayer() {
		return myPlayer;
	}
	public void setMyPlayer(Player myPlayer) {
		this.myPlayer = myPlayer;
	}
	public Boolean isBombExploded = false;

	private List<Integer> players;
	
	public int getCurrentPlayers() {
		return currentPlayers;
	}
	
	// MUY MUY MAL HACER ESTO, pero hay que sacarlo funcionando ya.
	public int getIdForPlayer(int i)
	{
		switch (i)
		{
		case 0:
			return R.id.PlayerTop;
		case 1:
			return R.id.PlayerRight;
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
	
	public int getMaxPlayers() {
		return maxPlayers;
	}
	
	public List<Integer> getPlayers() {
		return GameClient.instance.players;
	}
	
	public Integer getPlayersCount() {
		return GameClient.instance.players.size();
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
	
	public void setCurrentPlayers(int currentPlayers) {
		this.currentPlayers = currentPlayers;
	}
	
	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
}
