package protocols;

import java.util.List;

public class SearchTeamNearbyResponse extends DefaultResponse {
	private List<TeamInfo> teamList;

	public List<TeamInfo> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<TeamInfo> teamList) {
		this.teamList = teamList;
	}
}
