package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class CancelMatchRequest {
	private long matchId;
	private int teamId;

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("matchId", matchId).toString();
	}
}
