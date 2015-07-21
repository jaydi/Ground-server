package protocols;

import java.util.List;

public class MatchHistoryResponse extends DefaultResponse {
	private List<Match> matchList;

	public List<Match> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<Match> matchList) {
		this.matchList = matchList;
	}
}
