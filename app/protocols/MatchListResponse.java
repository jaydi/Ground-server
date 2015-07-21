package protocols;

import java.util.List;

public class MatchListResponse extends DefaultResponse {
	private List<Match> matchList;

	public List<Match> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<Match> matchList) {
		this.matchList = matchList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("matchList=").append(matchList).toString();
	}
}
