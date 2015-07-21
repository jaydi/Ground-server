package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class InviteMemberRequest {
	private int teamId;
	private int userId;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("userId", userId).toString();
	}
}
