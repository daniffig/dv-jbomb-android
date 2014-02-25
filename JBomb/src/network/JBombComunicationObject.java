package network;

import java.io.Serializable;
import java.util.Vector;

import reference.JBombRequestResponse;

public class JBombComunicationObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//Define el proposito de la comunicación, a partir de este se infiere que atributos se tienen que tomar en cuenta y cuales no
	private JBombRequestResponse Type;

	private Vector<GameInformation> AvailableGames = new Vector<GameInformation>();
	private Vector<String> Players = new Vector<String>();
	private String BombOwner;
	private String Loser;
	private String QuizQuestion;
	private Vector<String> QuizAnswers;
	private String SelectedQuizAnswer;
	
	private Integer MyPlayerId;
	private Integer RequestedGameId;
	private GamePlayInformation GamePlayInformation = null;
	
	public JBombComunicationObject(){
		super();
	}
	
	public JBombComunicationObject(JBombRequestResponse jbrr){
		super();
		this.Type = jbrr;
	}
	
	public JBombRequestResponse getType() {
		return Type;
	}
	public void setType(JBombRequestResponse type) {
		Type = type;
	}
	public Vector<String> getPlayers() {
		return Players;
	}
	public void setPlayers(Vector<String> players) {
		Players = players;
	}
	public String getBombOwner() {
		return BombOwner;
	}
	public void setBombOwner(String bombOwner) {
		BombOwner = bombOwner;
	}
	public String getLoser() {
		return Loser;
	}
	public void setLoser(String loser) {
		Loser = loser;
	}
	public String getQuizQuestion() {
		return QuizQuestion;
	}
	public void setQuizQuestion(String quizQuestion) {
		QuizQuestion = quizQuestion;
	}
	public Vector<String> getQuizAnswers() {
		return QuizAnswers;
	}
	public void setQuizAnswers(Vector<String> quizAnswers) {
		QuizAnswers = quizAnswers;
	}
	public String getSelectedQuizAnswer() {
		return SelectedQuizAnswer;
	}
	public void setSelectedQuizAnswer(String selectedQuizAnswer) {
		SelectedQuizAnswer = selectedQuizAnswer;
	}
	public Integer getMyPlayerId() {
		return MyPlayerId;
	}

	public void setMyPlayerId(Integer myPlayerId) {
		MyPlayerId = myPlayerId;
	}

	public Integer getRequestedGameId() {
		return RequestedGameId;
	}

	public void setRequestedGameId(Integer requestedGameId) {
		RequestedGameId = requestedGameId;
	}

	public Vector<GameInformation> getAvailableGames() {
		return AvailableGames;
	}
	public void setAvailableGames(Vector<GameInformation> availableGames) {
		AvailableGames = availableGames;
	}
	
	public void addGameInformation(GameInformation gi)
	{
		this.AvailableGames.add(gi);
	}
	
	public GamePlayInformation getGamePlayInformation() {
		return GamePlayInformation;
	}

	public void setGamePlayInformation(GamePlayInformation gamePlayInformation) {
		GamePlayInformation = gamePlayInformation;
	}

	public boolean hasGamePlayInformation()
	{
		return GamePlayInformation.equals(null);
	}
}

