package protocols;

import org.apache.commons.lang.builder.ToStringBuilder;

public class WritePostRequest {
	private int teamId;
	private int type;
	private String message;
	private String extra;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("teamId", teamId).append("type", type).append("extra", extra).toString();
	}
}
