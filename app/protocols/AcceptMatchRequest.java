package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class AcceptMatchRequest {
	private long matchId;

	public long getMatchId() {
		return matchId;
	}

	public void setMatchId(long matchId) {
		this.matchId = matchId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("matchId", matchId).toString();
	}
}
