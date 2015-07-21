package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class TeamHintRequest {
	private int teamId;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).toString();
	}
}
