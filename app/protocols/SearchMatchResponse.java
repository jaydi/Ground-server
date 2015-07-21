package protocols;

import java.util.List;

public class SearchMatchResponse extends DefaultResponse {
	private List<SMatch> matchList;

	public List<SMatch> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<SMatch> matchList) {
		this.matchList = matchList;
	}
}
