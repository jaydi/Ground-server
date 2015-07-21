package protocols;

import java.util.List;

public class PendingTeamListResponse extends DefaultResponse {
	private List<TeamHint> teamHintList;

	public List<TeamHint> getTeamHintList() {
		return teamHintList;
	}

	public void setTeamHintList(List<TeamHint> teamHintList) {
		this.teamHintList = teamHintList;
	}
}
