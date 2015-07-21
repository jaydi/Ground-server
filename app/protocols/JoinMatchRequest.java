package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class JoinMatchRequest {
	private long matchId;
	private int teamId;
	private boolean join;

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public boolean isJoin() {
		return join;
	}

	public void setJoin(boolean join) {
		this.join = join;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("matchId", matchId).append("teamId", teamId).append("join", join).toString();
	}
}
