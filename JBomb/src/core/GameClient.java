package core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import network.GamePlayInformation;
import network.Player;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.jbomb.R;

@SuppressLint("UseSparseArrays")
public class GameClient {

	private static GameClient instance;
	
	public List<Player> getOrderedPlayersByGeneralPoints() {
		return orderedPlayersByGeneralPoints;
	}

	public void setOrderedPlayersByGeneralPoints(
			List<Player> orderedPlayersByGeneralPoints) {
		this.orderedPlayersByGeneralPoints = orderedPlayersByGeneralPoints;
	}

	private List<Player> orderedPlayersByGeneralPoints;
	
	public void orderPlayersByGeneralPoints()
	{
		this.orderedPlayersByGeneralPoints = new ArrayList<Player>();
		
		for (Player p : instance.getPlayers())
		{
			orderedPlayersByGeneralPoints.add(p);
		}
		
		Collections.sort(this.orderedPlayersByGeneralPoints, new Comparator<Player>()
		{
			@Override
			public int compare(Player arg0, Player arg1) {
				if (arg0.getGeneralPoints() > arg1.getGeneralPoints())
					return 1;
				if (arg0.getGeneralPoints() < arg1.getGeneralPoints())
					return -1;
				
				return 0;
			}			
		});
	}
	
	public Boolean isGameOver()
	{
		return this.getGamePlayInformation().getCurrentRound().equals(this.getGamePlayInformation().getMaxRounds());
	}
	
	public Boolean isWinner()
	{		
		
		return this.orderedPlayersByGeneralPoints.get(0).equals(this.getMyPlayer());
	}
	
	public Boolean appStarted = false;
	
	public static int CORRECT_ANSWER = 31;
	public static int INCORRECT_ANSWER = 37;
	
//	public Vector<Integer> adjacentPlayersUIDs = new Vector<Integer>();	
	public Map<Integer, Player> adjacentPlayers = new HashMap<Integer, Player>();
	
	private Vector<Integer> playerNameIDs = new Vector<Integer>();
	private Vector<Integer> playerImageIDs = new Vector<Integer>();
	
	private GamePlayInformation gamePlayInformation;
	private Collection<Player> players;

	public static void destroyInstance()
	{
		GameClient.instance = null;
	}
	public static GameClient getInstance() {
		
		if (GameClient.instance == null)
		{
			GameClient.instance = new GameClient();
			
			instance.playerNameIDs.add(R.id.PlayerTop);
			instance.playerNameIDs.add(R.id.PlayerRight);
			instance.playerNameIDs.add(R.id.PlayerRight);
			instance.playerNameIDs.add(R.id.PlayerLeft);

			instance.playerImageIDs.add(R.id.PlayerTopImage);
			instance.playerImageIDs.add(R.id.PlayerRightImage);
			instance.playerImageIDs.add(R.id.PlayerRightImage);
			instance.playerImageIDs.add(R.id.PlayerLeftImage);
		}
		
		return GameClient.instance;
	}
	
	public Vector<Integer> getPlayerNameIDs() {
		return playerNameIDs;
	}
	public void setPlayerNameIDs(Vector<Integer> playerNameIDs) {
		this.playerNameIDs = playerNameIDs;
	}
	public Vector<Integer> getPlayerImageIDs() {
		return playerImageIDs;
	}
	public void setPlayerImageIDs(Vector<Integer> playerImageIDs) {
		this.playerImageIDs = playerImageIDs;
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
	public Boolean isLoser = false;
	
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
	public GamePlayInformation getGamePlayInformation() {
		return gamePlayInformation;
	}
	public void setGamePlayInformation(GamePlayInformation gamePlayInformation) {
		this.gamePlayInformation = gamePlayInformation;
	}
	public Collection<Player> getPlayers() {
		return players;
	}
	public void setPlayers(Collection<Player> players) {
		this.players = players;
	}
}
