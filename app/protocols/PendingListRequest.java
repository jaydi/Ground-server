package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

// TODO pendingUserList 로 이름 변경 
public class PendingListRequest {
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
