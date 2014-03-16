package network;

import java.io.Serializable;

public class GameSettings implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Integer topologyId;
	private Integer quizId;
	private Integer modeId;
	private Integer maxPlayers;
	private Integer maxRounds;
	private Integer roundDurationId;
	
	public Integer getMaxPlayers() {
		return maxPlayers;
	}
	public Integer getMaxRounds() {
		return maxRounds;
	}
	public Integer getModeId() {
		return modeId;
	}
	public String getName() {
		return name;
	}
	public Integer getQuizId() {
		return quizId;
	}
	public Integer getRoundDurationId() {
		return roundDurationId;
	}
	public Integer getTopologyId() {
		return topologyId;
	}
	public void setMaxPlayers(Integer maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	public void setMaxRounds(Integer maxRounds) {
		this.maxRounds = maxRounds;
	}
	public void setModeId(Integer modeId) {
		this.modeId = modeId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setQuizId(Integer quizId) {
		this.quizId = quizId;
	}
	public void setRoundDurationId(Integer roundDurationId) {
		this.roundDurationId = roundDurationId;
	}
	public void setTopologyId(Integer topologyId) {
		this.topologyId = topologyId;
	}

}
