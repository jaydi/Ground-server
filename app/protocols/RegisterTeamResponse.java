package protocols;

public class RegisterTeamResponse extends DefaultResponse {
	private int teamId;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}
}
