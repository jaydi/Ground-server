package protocols;

public class TeamHintResponse extends DefaultResponse {
	private TeamHint teamHint;

	public TeamHint getTeamHint() {
		return teamHint;
	}

	public void setTeamHint(TeamHint teamHint) {
		this.teamHint = teamHint;
	}
}
