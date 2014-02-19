package network;

import java.io.Serializable;
import java.util.Vector;

import reference.JBombRequestResponse;

public class JBombComunicationObject implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	//Define el proposito de la comunicación, a partir de este se infiere que atributos se tienen que tomar en cuenta y cuales no
	private JBombRequestResponse Type;

	private Vector<String> Players;
	private String BombOwner;
	private String Loser;
	private String QuizQuestion;
	private Vector<String> QuizAnswers;
	private String SelectedQuizAnswer;
	
	private String  GameName;
	private Integer CurrentRound;
	private Integer MaxRounds;
	private String  GamePlayersOverMaxGamePlayers;	
	
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
	public String getGameName() {
		return GameName;
	}
	public void setGameName(String gameName) {
		GameName = gameName;
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
