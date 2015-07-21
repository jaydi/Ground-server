package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class StartSurveyRequest {
	private long matchId;
	private int teamId;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("matchId", matchId).append("teamId", teamId).toString();
	}
}
