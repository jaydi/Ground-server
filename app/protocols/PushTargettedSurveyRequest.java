package protocols;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class PushTargettedSurveyRequest {
	private long matchId;
	private int teamId;
	private List<Integer> pushIds;

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

	public List<Integer> getPushIds() {
		return pushIds;
	}

	public void setPushIds(List<Integer> pushIds) {
		this.pushIds = pushIds;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("matchId", matchId).append("pushIds", pushIds).toString();
	}
}
