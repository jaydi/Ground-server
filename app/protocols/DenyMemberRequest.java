package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class DenyMemberRequest {
	private int teamId;
	private int memberId;

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("memberId", memberId).toString();
	}
}
