package protocols;

public class TeamInfoResponse extends DefaultResponse {
	private TeamInfo teamInfo;

	public TeamInfo getTeamInfo() {
		return teamInfo;
	}

	public void setTeamInfo(TeamInfo teamInfo) {
		this.teamInfo = teamInfo;
	}
}
