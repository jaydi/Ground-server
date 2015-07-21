package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class InviteTeamRequest {
	private long matchId;
	private int homeTeamId;
	private int awayTeamId;

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	public int getHomeTeamId() {
		return homeTeamId;
	}

	public void setHomeTeamId(int homeTeamId) {
		this.homeTeamId = homeTeamId;
	}

	public int getAwayTeamId() {
		return awayTeamId;
	}

	public void setAwayTeamId(int awayTeamId) {
		this.awayTeamId = awayTeamId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("matchId", matchId).append("homeTeamId", homeTeamId).append("awayTeamId", awayTeamId).toString();
	}
}
