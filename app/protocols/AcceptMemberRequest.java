package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AcceptMemberRequest {
	private int teamId;
	private int memberId;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("memberId", memberId).toString();
	}
}
