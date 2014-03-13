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
	public void setMaxPlayers(Integer maxPlayers) {
		this.maxPlayers = maxPlayers;
	}
	public Integer getMaxRounds() {
		return maxRounds;
	}
	public void setMaxRounds(Integer maxRounds) {
		this.maxRounds = maxRounds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTopologyId() {
		return topologyId;
	}
	public void setTopologyId(Integer topologyId) {
		this.topologyId = topologyId;
	}
	public Integer getModeId() {
		return modeId;
	}
	public void setModeId(Integer modeId) {
		this.modeId = modeId;
	}
	public Integer getRoundDurationId() {
		return roundDurationId;
	}
	public void setRoundDurationId(Integer roundDurationId) {
		this.roundDurationId = roundDurationId;
	}
	public Integer getQuizId() {
		return quizId;
	}
	public void setQuizId(Integer quizId) {
		this.quizId = quizId;
	}

}
