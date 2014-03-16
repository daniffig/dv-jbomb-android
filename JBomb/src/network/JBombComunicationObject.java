package network;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Vector;

import reference.JBombRequestResponse;

public class JBombComunicationObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//Define el proposito de la comunicaci�n, a partir de este se infiere que atributos se tienen que tomar en cuenta y cuales no
	private JBombRequestResponse Type;
	
	private String Flash;
	
	private Vector<GameInformation> AvailableGames = new Vector<GameInformation>();
	private Vector<Player> Players = new Vector<Player>();
	private GameSettingsInformation gameSettingsInformation;
	private GameSettings gameSettings;
	private Player BombOwner;
	private Player Loser;
	private String QuizQuestion;
	private Vector<String> QuizAnswers;
	private String SelectedQuizAnswer;
	private Boolean CorrectAnswer;
	
	private Player  MyPlayer;
	private Player  BombTargetPlayer;
	private Integer RequestedGameId;
	private GamePlayInformation GamePlayInformation = null;
	
	private HashMap<String, Integer> LastRoundScores;
	private HashMap<String, Integer> GeneralScores;
	
	public JBombComunicationObject(){
		super();
	}
	
	public JBombComunicationObject(JBombRequestResponse jbrr){
		super();
		this.Type = jbrr;
	}
	
	public void addGameInformation(GameInformation gi)
	{
		this.AvailableGames.add(gi);
	}
	public void addPlayer(Player p){
		Players.add(p);
	}
	
	public Vector<GameInformation> getAvailableGames() {
		return AvailableGames;
	}

	public Player getBombOwner() {
		return BombOwner;
	}

	public Player getBombTargetPlayer() {
		return BombTargetPlayer;
	}
	public Boolean getCorrectAnswer() {
		return CorrectAnswer;
	}
	public String getFlash() {
		return Flash;
	}
	
	public GamePlayInformation getGamePlayInformation() {
		return GamePlayInformation;
	}
	public GameSettings getGameSettings() {
		return gameSettings;
	}
	public GameSettingsInformation getGameSettingsInformation() {
		return gameSettingsInformation;
	}
	public HashMap<String, Integer> getGeneralScores() {
		return GeneralScores;
	}
	public HashMap<String, Integer> getLastRoundScores() {
		return LastRoundScores;
	}
	public Player getLoser() {
		return Loser;
	}
	public Player getMyPlayer() {
		return MyPlayer;
	}
	public Vector<Player> getPlayers() {
		return Players;
	}
	public Vector<String> getQuizAnswers() {
		return QuizAnswers;
	}
	public String getQuizQuestion() {
		return QuizQuestion;
	}
	public Integer getRequestedGameId() {
		return RequestedGameId;
	}

	public String getSelectedQuizAnswer() {
		return SelectedQuizAnswer;
	}

	public JBombRequestResponse getType() {
		return Type;
	}

	public boolean hasGamePlayInformation()
	{
		return GamePlayInformation.equals(null);
	}

	public void setAvailableGames(Vector<GameInformation> availableGames) {
		AvailableGames = availableGames;
	}

	public void setBombOwner(Player bombOwner) {
		BombOwner = bombOwner;
	}

	
	public void setBombTargetPlayer(Player bombTargetPlayer) {
		BombTargetPlayer = bombTargetPlayer;
	}

	public void setCorrectAnswer(Boolean correctAnswer) {
		CorrectAnswer = correctAnswer;
	}

	public void setFlash(String flash) {
		Flash = flash;
	}
	public void setGamePlayInformation(GamePlayInformation gamePlayInformation) {
		GamePlayInformation = gamePlayInformation;
	}
	
	public void setGameSettings(GameSettings gameSettings) {
		this.gameSettings = gameSettings;
	}
	
	public void setGameSettingsInformation(GameSettingsInformation gameSettingsInformation) {
		this.gameSettingsInformation = gameSettingsInformation;
	}

	public void setGeneralScores(HashMap<String, Integer> generalScores) {
		GeneralScores = generalScores;
	}

	public void setLastRoundScores(HashMap<String, Integer> lastRoundScores) {
		LastRoundScores = lastRoundScores;
	}

	public void setLoser(Player loser) {
		Loser = loser;
	}

	public void setMyPlayer(Player myPlayer) {
		MyPlayer = myPlayer;
	}

	public void setPlayers(Vector<Player> players) {
		Players = players;
	}

	public void setQuizAnswers(Vector<String> quizAnswers) {
		QuizAnswers = quizAnswers;
	}

	public void setQuizQuestion(String quizQuestion) {
		QuizQuestion = quizQuestion;
	}

	public void setRequestedGameId(Integer requestedGameId) {
		RequestedGameId = requestedGameId;
	}

	public void setSelectedQuizAnswer(String selectedQuizAnswer) {
		SelectedQuizAnswer = selectedQuizAnswer;
	}

	public void setType(JBombRequestResponse type) {
		Type = type;
	}
	
	
}