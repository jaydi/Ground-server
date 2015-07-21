package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class MatchHistoryRequest {
	private int teamId;
	private long cur;
	private boolean order;

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

	public boolean isOrder() {
		return order;
	}

	public void setOrder(boolean order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("cur", cur).append("order", order).toString();
	}

}
