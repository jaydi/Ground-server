package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class PostListRequest {
	private int teamId;
	private long cur;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public long getCur() {
		return cur;
	}

	public void setCur(long cur) {
		this.cur = cur;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("cur", cur).toString();
	}
}
