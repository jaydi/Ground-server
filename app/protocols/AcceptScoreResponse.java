package protocols;

public class AcceptScoreResponse extends DefaultResponse {
	private int matchStatus;

	public int getMatchStatus() {
		return matchStatus;
	}

	public void setMatchStatus(int matchStatus) {
		this.matchStatus = matchStatus;
	}
}
