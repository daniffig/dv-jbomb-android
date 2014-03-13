package network;

import java.io.Serializable;
import java.util.Vector;

public class GameSettingsInformation implements Serializable {
	
		private static final long serialVersionUID = 1L;
		
		private Vector<String> topologies = new Vector<String>();
		private Vector<String> quizzes = new Vector<String>();
		private Vector<String> modes = new Vector<String>();
		private Integer maxPlayersAllowed = 16;
		private Integer maxRoundsAllowed = 9;
		private Vector<String> roundDurations = new Vector<String>();
		
		public Vector<String> getTopologies() {
			return topologies;
		}
		public void setTopologies(Vector<String> topologies) {
			this.topologies = topologies;
		}
		public Vector<String> getQuizzes() {
			return quizzes;
		}
		public void setQuizzes(Vector<String> quizzes) {
			this.quizzes = quizzes;
		}
		public Vector<String> getModes() {
			return modes;
		}
		public void setModes(Vector<String> modes) {
			this.modes = modes;
		}
		public Integer getMaxPlayersAllowed() {
			return maxPlayersAllowed;
		}
		public void setMaxPlayersAllowed(Integer maxPlayersAllowed) {
			this.maxPlayersAllowed = maxPlayersAllowed;
		}
		public Integer getMaxRoundsAllowed() {
			return maxRoundsAllowed;
		}
		public void setMaxRoundsAllowed(Integer maxRoundsAllowed) {
			this.maxRoundsAllowed = maxRoundsAllowed;
		}
		public Vector<String> getRoundDurations() {
			return roundDurations;
		}
		public void setRoundDurations(Vector<String> roundDurations) {
			this.roundDurations = roundDurations;
		}

}
