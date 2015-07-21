package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SetScoreRequest {
	private long matchId;
	private int teamId;
	private int homeScore;
	private int awayScore;

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

	public int getHomeScore() {
		return homeScore;
	}

	public void setHomeScore(int homeScore) {
		this.homeScore = homeScore;
	}

	public int getAwayScore() {
		return awayScore;
	}

	public void setAwayScore(int awayScore) {
		this.awayScore = awayScore;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("matchId", matchId).append("teamId", teamId).append("homeScore", homeScore).append("awayScore", awayScore).toString();
	}
}
