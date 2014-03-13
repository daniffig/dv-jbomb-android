package network;

import android.annotation.SuppressLint;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("UseSparseArrays")
public class GameSettingsInformation implements Serializable {
	
		private static final long serialVersionUID = 1L;
		
		private Map<Integer, String> topologies = new HashMap<Integer, String>();
		private Map<Integer, String> quizzes = new HashMap<Integer, String>();
		private Map<Integer, String> modes = new HashMap<Integer, String>();
		private Integer maxPlayersAllowed = 16;
		private Integer maxRoundsAllowed = 9;
		private Map<Integer, String> roundDurations = new HashMap<Integer, String>();
		
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
		public Map<Integer, String> getTopologies() {
			return topologies;
		}
		public void setTopologies(Map<Integer, String> topologies) {
			this.topologies = topologies;
		}
		public Map<Integer, String> getQuizzes() {
			return quizzes;
		}
		public void setQuizzes(Map<Integer, String> quizzes) {
			this.quizzes = quizzes;
		}
		public Map<Integer, String> getModes() {
			return modes;
		}
		public void setModes(Map<Integer, String> modes) {
			this.modes = modes;
		}
		public Map<Integer, String> getRoundDurations() {
			return roundDurations;
		}
		public void setRoundDurations(Map<Integer, String> roundDurations) {
			this.roundDurations = roundDurations;
		}

}
