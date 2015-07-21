package protocols;

import java.util.List;

public class TeamListResponse extends DefaultResponse {
	private List<TeamHint> teamList;

	public List<TeamHint> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<TeamHint> teamList) {
		this.teamList = teamList;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		return sb.append("teamSize=").append(teamList.size()).toString();
	}
}
